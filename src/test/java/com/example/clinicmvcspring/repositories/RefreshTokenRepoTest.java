package com.example.clinicmvcspring.repositories;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import com.example.clinicmvcspring.models.AppUser;
import com.example.clinicmvcspring.models.RefreshToken;
import com.example.clinicmvcspring.models.Role;



@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) //dont use H2
public class RefreshTokenRepoTest {

@Autowired
private RefreshTokenRepo refreshTokenRepo;
@Autowired
private UserRepo userRepo;


//naming convention: ClassName_MethodName_ExpectedBehavior
@Test
public void RefreshTokenRepo_Save_ReturnSavedRefreshToken ()
{
    //1 ARRANGE*********************
    AppUser user = new AppUser();//every token needs a user so i need a dummy user to test

    //dummy values
    user.setUsername("testUsername");
    user.setPassword("testPassword");
    user.setRole(Role.DOCTOR);
    AppUser savedUser = userRepo.save(user); // save first

    RefreshToken refreshToken =  new RefreshToken();
    refreshToken.setToken("fake-test-token");
    refreshToken.setUser(savedUser);
    refreshToken.setExpiryDate(new Timestamp(System.currentTimeMillis() + 100000));
    
    //2 ACT********************
    
    RefreshToken savedRefreshToken = refreshTokenRepo.save(refreshToken);




    //3 ASSERT*****************
assertNotNull(savedRefreshToken);
assertTrue(savedRefreshToken.getId() > 0);
}

    
}
