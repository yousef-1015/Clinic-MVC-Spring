package com.example.clinicmvcspring.repositories;

import java.sql.Timestamp;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.clinicmvcspring.models.AppUser;
import com.example.clinicmvcspring.models.RefreshToken;
import com.example.clinicmvcspring.models.Role;

@DataJpaTest
// @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
// //dont use H2
@ActiveProfiles("test")
public class RefreshTokenRepoTest {

    @Autowired
    private RefreshTokenRepo refreshTokenRepo;
    @Autowired
    private UserRepo userRepo;

    // naming convention: ClassName_MethodName_ExpectedBehavior
    @Test
    public void RefreshTokenRepo_Save_ReturnSavedRefreshToken() {
        // 1 ARRANGE*********************
        AppUser user = new AppUser();// every token needs a user so i need a dummy user to test

        // dummy values
        user.setUsername("testUsername");
        user.setPassword("testPassword");
        user.setRole(Role.DOCTOR);
        AppUser savedUser = userRepo.save(user); // save first

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken("fake-test-token");
        refreshToken.setUser(savedUser);
        refreshToken.setExpiryDate(new Timestamp(System.currentTimeMillis() + 100000));

        // 2 ACT********************

        RefreshToken savedRefreshToken = refreshTokenRepo.save(refreshToken);

        // 3 ASSERT*****************
        Assertions.assertNotNull(savedRefreshToken);
        Assertions.assertTrue(savedRefreshToken.getId() > 0);
    }

    @Test
    public void RefreshTokenRepo_findByTOKEN_ReturnDesiredRefreshToken() {
        // ARRANGE
        AppUser user = new AppUser();
        user.setUsername("testUsername");
        user.setPassword("testPassword");
        user.setRole(Role.DOCTOR);
        AppUser savedUser = userRepo.save(user);
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken("fake-test-token");
        refreshToken.setUser(savedUser);
        refreshToken.setExpiryDate(new Timestamp(System.currentTimeMillis() + 100000));
        RefreshToken savedRefreshToken = refreshTokenRepo.save(refreshToken);

        // ACT

        Optional<RefreshToken> fetchedToken = refreshTokenRepo.findByToken(savedRefreshToken.getToken());

        // ASSERT

        Assertions.assertTrue(fetchedToken.isPresent()); // i actually got a token (not empty)

        Assertions.assertEquals(savedRefreshToken.getId(), fetchedToken.get().getId()); // did i fetch the same token i
                                                                                        // saved

    }
}
