package com.tasktwo.timelord.server.controller;

import com.tasktwo.timelord.server.model.UserModel;
import com.tasktwo.timelord.server.service.JwtService;
import com.tasktwo.timelord.server.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class UserController {
    @Autowired
    public UserService userService;

    @Autowired
    JwtService jwtService;
    @Transactional
    @GetMapping("/all")
    public List<UserModel> getAllUsers(){ return userService.getAllUsers(); }


    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody Map<String, Object> credentials){
        String email = (String) credentials.get("email");
        String password = (String) credentials.get("password");
        userService.saveUserToDatabase(email, password);
        Map<String, String> response = new HashMap<>();
        response.put("message", "User created");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/logged")
    public ResponseEntity<?> getLoggedInPage(@RequestHeader("Authorization") String tokenHeader,
                                             @RequestHeader("idUser") String userId) throws ParseException {
        try{
            String token = tokenHeader.replace("Bearer ", "");

            if (jwtService.validateToken(token)) {
                String extractedEmail = jwtService.extractEmail(token);
                boolean isTokenValid = jwtService.validateToken(token);
                Optional<UserModel> user = userService.getUserByEmail(extractedEmail);
                if (user.isPresent() && user.get().getId().toString().equals(userId)) {
                    if(isTokenValid){
                        return ResponseEntity.ok("The user can log in");
                    } else {
                        return ResponseEntity.status(401).body("Invalid token or user");
                    }
                }
            }
            return ResponseEntity.status(401).body("Invalid token or user");
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Error: " + e.getMessage());
        }
    }
}
