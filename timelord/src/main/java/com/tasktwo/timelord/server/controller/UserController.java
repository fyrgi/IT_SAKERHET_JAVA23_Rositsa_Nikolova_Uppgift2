package com.tasktwo.timelord.server.controller;

import com.tasktwo.timelord.server.model.UserModel;
import com.tasktwo.timelord.server.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/users")
public class UserController {
    @Autowired
    public UserService userService;

    @Transactional
    @GetMapping("/all")
    public List<UserModel> getAllUsers(){ return userService.getAllUsers(); }
}
