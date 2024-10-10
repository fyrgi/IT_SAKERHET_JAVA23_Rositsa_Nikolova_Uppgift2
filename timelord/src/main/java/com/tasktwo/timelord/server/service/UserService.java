package com.tasktwo.timelord.server.service;

import com.tasktwo.timelord.security.PasswordHashing;
import com.tasktwo.timelord.server.model.UserModel;
import com.tasktwo.timelord.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;
    private PasswordHashing passwordHashing;

    // JWT hanteras här

    @Autowired
    public UserService(UserRepository userRepository, PasswordHashing passwordHashing) {
        this.userRepository = userRepository;
        this.passwordHashing = passwordHashing;
    }

    public List<UserModel> getAllUsers(){ return userRepository.findAll();}

    public Optional<UserModel> getUserById(Long userId){ return userRepository.findById(userId); }

    public UserModel saveUser(UserModel user) {
        // Hash the password before saving
        String hashedPassword = passwordHashing.hashPassword(user.getPassword());
        user.setPassword(hashedPassword);
        userRepository.save(user);
        return user;
    }

    public void deleteUser(Long userId){
        userRepository.deleteById(userId);
    }
}
