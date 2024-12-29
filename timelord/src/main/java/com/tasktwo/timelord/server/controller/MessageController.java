package com.tasktwo.timelord.server.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.nimbusds.jose.JOSEException;
import com.tasktwo.timelord.server.DTO.MessageDTO;
import com.tasktwo.timelord.server.model.MessageModel;
import com.tasktwo.timelord.server.model.UserModel;
import com.tasktwo.timelord.server.repository.MessageRepository;
import com.tasktwo.timelord.server.service.JwtService;
import com.tasktwo.timelord.server.service.MessageService;
import com.tasktwo.timelord.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.*;

@RestController
@RequestMapping("/messages")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    MessageRepository messageRepository;
    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> createMessage(@RequestHeader("Authorization") String token,
                                                             @RequestBody MessageDTO messageDTO) throws ParseException, JOSEException {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        }
        String jwtToken = token.substring(7);
        if (!jwtService.validateToken(jwtToken)) { // Corrected token validation logic
            return ResponseEntity.status(401).body(Map.of("error", "Invalid token"));
        }

        if (jwtService.isTokenExpired(jwtToken)) {
            return ResponseEntity.status(401).body(Map.of("error", "You are logged out. Please log in again"));
        }

        String email = jwtService.extractEmail(jwtToken);
        Optional<UserModel> userOptional = userService.getUserByEmail(email);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("error", "User not found. Please log in again."));
        }
        try {
            UserModel user = userOptional.get();
            String encryptedMessage = messageService.encrypt(messageDTO.getMessage());

            MessageModel newMessage = new MessageModel();
            newMessage.setMessage(encryptedMessage);
            newMessage.setUser(user);

            messageRepository.save(newMessage);
            return ResponseEntity.ok(Map.of("status", "Message saved"));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Error creating message"));
        }
    }
    @GetMapping("/view")
    public List<String> viewTimeCapsules(@RequestHeader("Authorization") String token) throws Exception {
        if (token == null || !token.startsWith("Bearer ")) {
            return List.of("Unauthorized");
        }
        String jwtToken = token.substring(7);
        if (!jwtService.validateToken(jwtToken)) { // Corrected token validation logic
            return List.of("Invalid token");
        }

        if (jwtService.isTokenExpired(jwtToken)) {
            return List.of("You are logged out. Please log in again");
        }

        String email = jwtService.extractEmail(jwtToken);
        Optional<UserModel> userOptional = userService.getUserByEmail(email);
        if (userOptional.isPresent()) {
            UserModel user = userOptional.get();
            List<MessageModel> messages = user.getMessages();

            //Decrypt each message before returning
            return messages.stream()
                    .map(msg -> {
                        try {
                            System.out.println(messageService.decryptMessage(msg.getMessage()));
                            return messageService.decryptMessage(msg.getMessage());
                        } catch (Exception e) {
                            e.printStackTrace();
                            return "Error decrypting message";
                        }
                    }).toList();
        }
        else {
            return List.of("No messages"); }
    }
}