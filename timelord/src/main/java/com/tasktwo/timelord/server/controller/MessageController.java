package com.tasktwo.timelord.server.controller;

import com.sun.tools.jconsole.JConsoleContext;
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
import sun.security.krb5.internal.ccache.MemoryCredentialsCache;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public ResponseEntity<Map<String, String>> createMessage(@RequestBody MessageDTO payload) {
        try {
            Optional<UserModel> userOptional = userService.getUserById(payload.getIdUser());

            if (userOptional.isPresent()) {
                UserModel user = userOptional.get();
                String encryptedMessage = messageService.encrypt(payload.getMessage(), user.getKey());

                MessageModel newMessage = new MessageModel();
                newMessage.setMessage(encryptedMessage);
                newMessage.setUser(user);

                messageRepository.save(newMessage);
                return ResponseEntity.ok(Map.of("status", "Message saved"));
            } else {
                return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Error creating message"));
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<String>> listMessages(@RequestHeader("Authorization") String token,
                                                     @RequestHeader("idUser") String userId) {
        try {
            Optional<UserModel> userOptional = userService.getUserById(Long.valueOf(userId));

            if (userOptional.isEmpty()) {
                return ResponseEntity.status(401).body(Collections.emptyList());
            }

            List<MessageModel> messages = messageRepository.getMessagesByUser(userOptional.get());
            List<String> decryptedMessages = messages.stream()
                    .map(message -> {
                        try {
                            return messageService.decrypt(message.getMessage(), userOptional.get().getKey());
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(decryptedMessages);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Collections.emptyList());
        }
    }
}