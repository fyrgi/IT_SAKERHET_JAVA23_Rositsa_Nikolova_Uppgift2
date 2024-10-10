package com.tasktwo.timelord.security;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
@Component
public class PasswordHasshing {
    private final String salt = "aUdnmJw@";
    public String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            byte[] hashSalt = md.digest(salt.getBytes());
            StringBuilder hexString = new StringBuilder();
            // implement salt first 64
            for(byte b : hashSalt){
                hexString.append(String.format("%02x", b));
            }
            // hash password 64 symbols
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            System.out.println(hexString.toString());
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
}
