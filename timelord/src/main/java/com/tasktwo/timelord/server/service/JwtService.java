package com.tasktwo.timelord.server.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtService {

    private static final String SECRET = "25mla9JMKMLNuylgsaOhgk2nfoMafpPPasd$fb"; // Use an environment variable in production
    public String generateToken(String email) throws JOSEException {
        return createToken(email);
    }

    private String createToken(String email) throws JOSEException {
        JWSSigner signer = new MACSigner(SECRET);
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(email) // Set the subject to the email
                .issuer("https://example.com")
                .expirationTime(new Date(new Date().getTime() + 60 * 1000 * 60)) // 1 hour
                .build();

        // Create the JWT
        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
        signedJWT.sign(signer);
        return signedJWT.serialize();
    }

    public String extractEmail(String token) throws ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        String email = signedJWT.getJWTClaimsSet().getSubject();
        return email;
    }

    public boolean validateToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            // Verify the signature
            JWSVerifier verifier = new MACVerifier(SECRET);
            return signedJWT.verify(verifier);
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception for debugging
            return false;
        }
    }
}
