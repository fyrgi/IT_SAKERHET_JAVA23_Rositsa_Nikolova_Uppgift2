package com.tasktwo.timelord.server.service;

import com.tasktwo.timelord.security.PasswordHashing;
import com.tasktwo.timelord.server.controller.AuthController;
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
    public Optional<UserModel> getUserByEmail(String email){ return userRepository.findByEmail(email); }
    public UserModel saveUser(UserModel user) {
        // Hash the password before saving
        String hashedPassword = passwordHashing.hashPassword(user.getPassword());
        user.setPassword(hashedPassword);
        userRepository.save(user);
        return user;
    }

    public Optional<UserModel> login(String email, String password){
        Optional<UserModel> foundUser = null;
        try {
            foundUser = userRepository.findByEmail(email);
            System.out.println("The email " + email + " The user" + foundUser );
        } catch (Exception e) {
            System.out.println(e);
        }
        if(foundUser.isPresent()){
            UserModel user = foundUser.get();
            String hashedPassword = passwordHashing.hashPassword(password);
            if (password.equals(user.getPassword())) {
                System.out.println(hashedPassword);
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }
    public void deleteUser(Long userId){
        userRepository.deleteById(userId);
    }
}
