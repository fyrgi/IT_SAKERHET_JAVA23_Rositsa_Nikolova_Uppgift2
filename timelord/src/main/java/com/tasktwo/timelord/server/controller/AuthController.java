package com.tasktwo.timelord.server.controller;

import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import com.tasktwo.timelord.security.JwtAuthenticationFilter;
import com.tasktwo.timelord.server.model.UserModel;
import com.tasktwo.timelord.server.service.JwtService;
import com.tasktwo.timelord.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/authenticate")
    public Map<String, String> authenticate(@RequestBody Map<String, Object> credentials) {
        String email = (String) credentials.get("email");
        String password = (String) credentials.get("password");

        Optional<UserModel> foundUser = userService.login(email, password);
        if (foundUser.isPresent()) {
            try {
                String jwt = jwtService.generateToken(email); // Generate the JWT
                return Map.of("token", jwt); // Return the token
            } catch (Exception e) {
                return Map.of("error", "Error generating token");
            }
        } else {
            return Map.of("error", "Invalid credentials");
        }
    }

}
