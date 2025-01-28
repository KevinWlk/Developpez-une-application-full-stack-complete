//package com.openclassrooms.mddapi.services;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.HashMap;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//public class JwtServiceTest {
//
//    @Autowired
//    private JwtService jwtService;
//
//    @Test
//    public void testGenerateAndValidateToken() {
//        String username = "testuser@example.com";
//
//        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
//                username,
//                "password",
//                List.of()
//        );
//
//        String token = jwtService.generateToken(userDetails);
//
//        assertNotNull(token);
//        assertTrue(jwtService.isTokenValid(token, userDetails));
//        assertEquals(username, jwtService.extractUsername(token));
//    }
//
//    @Test
//    public void testExpiredToken() throws InterruptedException {
//        String username = "testuser@example.com";
//
//        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
//                username,
//                "password",
//                List.of()
//        );
//
//        // Token with 1 millisecond expiration
//        String token = jwtService.buildToken(new HashMap<>(), userDetails, 1);
//
//        Thread.sleep(2); // Wait for the token to expire
//        assertTrue(jwtService.isTokenExpired(token));
//    }
//}
