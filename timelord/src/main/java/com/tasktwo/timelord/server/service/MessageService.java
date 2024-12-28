package com.tasktwo.timelord.server.service;

import com.tasktwo.timelord.server.model.MessageModel;
import com.tasktwo.timelord.server.model.UserModel;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

@Service
public class MessageService {
    private final String SECRET_KEY = "vYbIsCw1g6Gi63Ec0gnx3w==";

    public static SecretKey stringToAESKey(String keyString) throws Exception {
        // Key is hashed to 128-bit (16 bytes), otherwise gets fail
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] keyBytes = sha.digest(keyString.getBytes(StandardCharsets.UTF_8));
        keyBytes = Arrays.copyOf(keyBytes, 16);

        return new SecretKeySpec(keyBytes, "AES");
    }
    public String encrypt(String message) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(message.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    //Decrypts an encrypted message using AES decryption
    public String decryptMessage(String encryptedMessage) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedMessage));
        return new String(decryptedBytes);
    }

    private SecretKey convertKey(String base64Key) {
        byte[] decodedKey = Base64.getDecoder().decode(base64Key);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }

    public void saveMessage(String userId, String message){
        MessageModel messageModel = new MessageModel();
        //secretKey = user
        //String decryptedMessage = decrypt()
    }
}