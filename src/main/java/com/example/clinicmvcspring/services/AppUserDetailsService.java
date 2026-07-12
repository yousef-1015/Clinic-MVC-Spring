package com.example.clinicmvcspring.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.clinicmvcspring.models.AppUser;

import com.example.clinicmvcspring.repositories.UserRepo;

@Service
public class AppUserDetailsService implements UserDetailsService {

    private final UserRepo repo;
    private final PasswordEncoder passwordEncoder;

    public AppUserDetailsService(UserRepo repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;

    }

    public List<AppUser> getAllUsers() {
        return repo.findAll();
    }

    public Optional<AppUser> getUserById(int id) {
        return repo.findById(id);
    }

    public AppUser addUser(AppUser appUser) {
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));

        return repo.save(appUser);
    }

    public void deleteUser(AppUser user) {
        repo.delete(user);
    }

    public void deleteUserByID(int id) {
        repo.deleteById(id);
    }

    public AppUser updateUserById(int id, AppUser user) {
        user.setId(id);
        return repo.save(user);
    }

    public Page<AppUser> getAllUsers(Pageable pageable) {
        return repo.findAll(pageable);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

}
