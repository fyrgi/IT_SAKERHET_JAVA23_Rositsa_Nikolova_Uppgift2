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

    private static final String SECRET = "25mla9JMKMLNuylgsaOhgk2nfoMafpPPasd$fb";
    final long TOKEN_VALIDITY = 100 * 60 * 60 * 10;
    public String generateToken(String email) throws JOSEException {
        return createToken(email);
    }

    private String createToken(String email) throws JOSEException {
        JWSSigner signer = new MACSigner(SECRET.getBytes());
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(email)
                .issueTime(new Date(System.currentTimeMillis()))
                .expirationTime(new Date(new Date().getTime() + TOKEN_VALIDITY)) // 1 hour
                .build();

        //Create the JWT with the header and claims
        JWSObject jwsObject = new JWSObject(
                new JWSHeader(JWSAlgorithm.HS256),
                new Payload(claimsSet.toJSONObject())
        );

        //System.out.println(jwsObject.getPayload());

        jwsObject.sign(signer);
        return jwsObject.serialize();
    }

    public String extractEmail(String token) throws ParseException {
        JWSObject jwsObject = JWSObject.parse(token);
        JWTClaimsSet claimsSet = JWTClaimsSet.parse(jwsObject.getPayload().toJSONObject());
        String email = claimsSet.getSubject();
        return email;
    }

    public boolean validateToken(String token)  throws JOSEException, java.text.ParseException {
        JWSObject jwsObject = JWSObject.parse(token);
        // Verify the signature
        JWSVerifier verifier = new MACVerifier(SECRET.getBytes());
        return jwsObject.verify(verifier);
    }

    //Check if the token has expired
    public boolean isTokenExpired(String token) throws java.text.ParseException {
        JWSObject jwsObject = JWSObject.parse(token);
        JWTClaimsSet claimsSet = JWTClaimsSet.parse(jwsObject.getPayload().toJSONObject());
        Date expiration = claimsSet.getExpirationTime();
        return expiration.before(new Date());
    }
}
