package com.tasktwo.timelord.server.controller;

import com.tasktwo.timelord.server.model.UserModel;
import com.tasktwo.timelord.server.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/users")
public class UserController {
    @Autowired
    public UserService userService;

    @Transactional
    @GetMapping("/all")
    public List<UserModel> getAllUsers(){ return userService.getAllUsers(); }

    @PostMapping("/login")
    public ResponseEntity<String> login(String email, String password){
        userService.login(email, password);
        return ResponseEntity.ok("User logged in");
    }

    @PostMapping("/create")
    public ResponseEntity<String> createUser(String email, String password){
        userService.saveUser(email, password);
        return ResponseEntity.ok("User created");
    }


}
