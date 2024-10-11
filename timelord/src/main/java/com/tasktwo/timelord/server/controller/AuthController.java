package com.tasktwo.timelord.server.controller;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.tasktwo.timelord.server.model.UserModel;
import com.tasktwo.timelord.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@RestController
public class AuthController {
    // Define the secret key used for signing JWTs. It should be base64 encoded and at least 256 bits long.
    private static final String SECRET = "RmV2dDJDZzJ5MkVma1B4R3lNdE1qYzBHRnBzYklBUTA=";

    @Autowired
    private UserService userService;
    @PostMapping("/authenticate")
    public Map<String, String> authenticate(@RequestBody Map<String, Object> credentials) throws JOSEException {
        // Extract username and password from the request body.
        String email = (String) credentials.get("email");
        String password = (String) credentials.get("password");
        // attempt login with the provided details
        Optional<UserModel> foundUser = userService.login(email, password);

        if (foundUser.isPresent()) {
            // Create a signer using HMAC and your secret key. HMAC algorithm
            JWSSigner signer = new MACSigner(SECRET);
            // Create the claims for the JWT, such as subject, issuer, and expiration time.
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject("admin")
                    .issuer("https://timelordTask2.se")
                    .expirationTime(new Date(new Date().getTime() + 60 * 1000 * 60)) // JWT with 1 hour validity
                    .build();
            // Create a new JWT with the specified header and claims.
            SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
            // Sign the JWT with your signer.
            signedJWT.sign(signer);
            // Serialize the JWT to a compact, URL-safe string.
            String jwt = signedJWT.serialize();
            System.out.println(jwt);
            // Return the JWT in a response map.
            return Map.of("token", jwt);
        } else {
            // Return an error message if authentication fails.
            return Map.of("error", "Invalid credentials");
        }
    }

    @GetMapping("/protected")
    public Map<String, Object> getProtectedData(@RequestHeader(name = "Authorization") String authHeader) {
        // Check if the Authorization header is present and formatted correctly.
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // Extract the token from the Authorization header.
            String token = authHeader.substring(7); // Remove "Bearer " prefix

            try {
                // Parse the JWT from the token string.
                SignedJWT signedJWT = SignedJWT.parse(token);
                // Create a verifier using your secret key to check the signature.
                JWSVerifier verifier = new MACVerifier(SECRET);
                // Verify the token's signature and check if it has not expired.
                if (signedJWT.verify(verifier) && new Date().before(signedJWT.getJWTClaimsSet().getExpirationTime())) {
                    // Return the protected data if the token is valid.
                    return Map.of("data", "This is protected data.");
                } else {
                    // Return an error if the token is invalid or expired.
                    return Map.of("error", "Invalid or expired JWT");
                }
            } catch (ParseException | JOSEException e) {
                // Handle parsing and verification errors.
                return Map.of("error", "Error parsing or verifying JWT");
            }
        }
        // Return an error if the Authorization header is missing or not in the correct format.
        return Map.of("error", "No authorization header found");
    }
}
