package com.example.clinicmvcspring.controllers;

import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.clinicmvcspring.dtos.AuthResponse;
import com.example.clinicmvcspring.dtos.ErrorResponseDTO;
import com.example.clinicmvcspring.dtos.LoginRequest;
import com.example.clinicmvcspring.dtos.PaginatedListDTO;
import com.example.clinicmvcspring.dtos.RefreshRequestDTO;
import com.example.clinicmvcspring.dtos.RefreshResponseDTO;
import com.example.clinicmvcspring.dtos.SignupRequest;
import com.example.clinicmvcspring.models.AppUser;
import com.example.clinicmvcspring.models.CustomUserDetails;
import com.example.clinicmvcspring.models.Doctor;
import com.example.clinicmvcspring.models.RefreshToken;
import com.example.clinicmvcspring.models.Role;
import com.example.clinicmvcspring.services.AppUserDetailsService;
import com.example.clinicmvcspring.services.DoctorService;
import com.example.clinicmvcspring.services.JwtService;
import com.example.clinicmvcspring.services.RefreshTokenService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User & Authentication Management", description = "Endpoints for user signup, authentication, token refresh, and logout")
public class UserController {
    private final AppUserDetailsService userService;
    private final DoctorService doctorService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public UserController(AppUserDetailsService userService, DoctorService doctorService,
            AuthenticationManager authenticationManager, JwtService jwtService,
            RefreshTokenService refreshTokenService) {
        this.userService = userService;
        this.doctorService = doctorService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    @GetMapping("")
    @Operation(summary = "Get All Users", description = "Retrieve a paginated list of registered system users, [Requires Role: ADMIN]")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = PaginatedListDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized, Missing or invalid JWT token", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden, Requires ADMIN role", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<?> getAllUsers(
            @Parameter(description = "Page index starting from zero", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of users per page (1-50)", example = "5") @RequestParam(defaultValue = "5") int size) {

        if (page < 0) {
            ErrorResponseDTO error = new ErrorResponseDTO("Page Number can't be negative", 400);
            return ResponseEntity.status(400).body(error);
        }
        if (size <= 0 || size > 50) {
            ErrorResponseDTO error = new ErrorResponseDTO("Size must be between 1 and 50", 400);
            return ResponseEntity.status(400).body(error);
        }
        Pageable pageable = PageRequest.of(page, size);

        Page<AppUser> userPage = userService.getAllUsers(pageable);
        long total = userPage.getTotalElements();
        PaginatedListDTO<AppUser> response = new PaginatedListDTO<>(userPage.getContent(), page, size, total);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signup")
    @Operation(summary = "Register A New User", description = "Register a new user account and optionally link to an existing doctor profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request (e.g. Doctor ID not found or Doctor already has a user account)")
    })
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest request) {

        // If it's a DOCTOR perform all validations
        Doctor doctorToLink = null;
        if (request.getRole() == Role.DOCTOR) {
            if (request.getForeignId() != null) {
                Optional<Doctor> docOp = doctorService.getDoctorByID(request.getForeignId());

                if (docOp.isEmpty()) {
                    return new ResponseEntity<>(
                            Map.of(
                                    "status", 400,
                                    "error", "Bad Request",
                                    "message", "Doctor with ID " + request.getForeignId() + " not found."),
                            HttpStatus.BAD_REQUEST);
                }

                doctorToLink = docOp.get();
                if (doctorToLink.getUserId() != null) {
                    return new ResponseEntity<>(
                            Map.of(
                                    "status", 400,
                                    "error", "Bad Request",
                                    "message",
                                    "Doctor with ID " + request.getForeignId() + " already has a user account."),
                            HttpStatus.BAD_REQUEST);
                }
            }
        }

        // All validations passed
        AppUser newUser = new AppUser();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(request.getPassword());
        newUser.setRole(request.getRole());
        newUser.setEnabled(true);

        AppUser savedUser = userService.addUser(newUser);

        // Link the doctor to the saved user
        if (doctorToLink != null) {
            doctorToLink.setUserId(savedUser.getId());
            doctorService.updateDoctorById(doctorToLink.getId(), doctorToLink);
        }

        return new ResponseEntity<>(
                Map.of(
                        "status", 201,
                        "message", "User registered successfully!"),
                HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @Operation(summary = "User Login", description = "Authenticate user credentials and return a JWT access token and refresh token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authentication successful", content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "401", description = "Invalid username or password", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);// setting the auth to get username when
                                                                             // auditing

        final UserDetails userDetailsToLogin = userService.loadUserByUsername(request.getUsername());
        final String token = jwtService.generateToken(userDetailsToLogin);
        final RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetailsToLogin.getUsername());

        return ResponseEntity.ok(new AuthResponse(token, (CustomUserDetails) userDetailsToLogin, refreshToken));
    }

    @PostMapping("/logout")
    @Operation(summary = "User Logout", description = "Invalidate JWT access token and delete refresh token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logged out successfully"),
            @ApiResponse(responseCode = "404", description = "Refresh token not found", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<?> logout(
            @Parameter(description = "Authorization header containing Bearer JWT token", example = "Bearer eyJhbGciOi...") @RequestHeader("Authorization") String authHeader,
            @RequestBody RefreshRequestDTO request) {

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // Remove "Bearer "

            Optional<RefreshToken> refToken = refreshTokenService.findByToken(request.getRefreshToken()); // get the
            if (refToken.isEmpty()) {
                return ResponseEntity.status(404)
                        .body(new ErrorResponseDTO("This Refresh Token was not found", 404));
            }

            refreshTokenService.deleteToken(refToken.get());
            jwtService.invalidateToken(token); // Add to blacklist
        }

        return ResponseEntity.ok(Map.of("message", "Logged out successfully!"));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh Access Token", description = "Exchange a valid refresh token for a new JWT access token and new refresh token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tokens refreshed successfully", content = @Content(schema = @Schema(implementation = RefreshResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Refresh token is expired", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Refresh token not found", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<?> refresh(@RequestBody RefreshRequestDTO request) {

        Optional<RefreshToken> refToken = refreshTokenService.findByToken(request.getRefreshToken()); // get the
        if (refToken.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(new ErrorResponseDTO("This Refresh Token was not found", 404));
        }

        try {
            refreshTokenService.verifyExpiration(refToken.get());// verify that the refresh token is not expired
        } catch (RuntimeException e) {
            return ResponseEntity.status(403)
                    .body(new ErrorResponseDTO(e.getMessage(), 403));
        }

        AppUser user = refToken.get().getUser();
        final UserDetails userDetailsToRefresh = userService.loadUserByUsername(user.getUsername());
        final String token = jwtService.generateToken(userDetailsToRefresh);
        refreshTokenService.deleteToken(refToken.get());
        RefreshToken newRefToken = refreshTokenService.createRefreshToken(user.getUsername());
        return ResponseEntity.ok(new RefreshResponseDTO(token, newRefToken.getPlainTextToken()));

    }
}
