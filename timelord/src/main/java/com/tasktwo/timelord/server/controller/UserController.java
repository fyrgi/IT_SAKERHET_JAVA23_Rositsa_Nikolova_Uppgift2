package com.tasktwo.timelord.server.controller;

import com.tasktwo.timelord.server.model.UserModel;
import com.tasktwo.timelord.server.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class UserController {
    @Autowired
    public UserService userService;

    @Transactional
    @GetMapping("/all")
    public List<UserModel> getAllUsers(){ return userService.getAllUsers(); }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(String email, String password){
        userService.login(email, password);
        Map<String, String> response = new HashMap<>();
        response.put("message", "User logged in");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody Map<String, Object> credentials){
        String email = (String) credentials.get("email");
        String password = (String) credentials.get("password");
        userService.saveUserToDatabase(email, password);
        Map<String, String> response = new HashMap<>();
        response.put("message", "User created");
        return ResponseEntity.ok(response);
    }


}
