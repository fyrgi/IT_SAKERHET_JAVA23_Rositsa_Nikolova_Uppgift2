package com.tasktwo.timelord.server.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "message")
public class MessageModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = false)
    private String message;

    // One Message can have many UserMessage associations
    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<UserMessageModel> userMessages = new ArrayList<>();

    // Helper methods to manage bidirectional relationship
    public void addUserMessage(UserMessageModel userMessage) {
        userMessages.add(userMessage);
        userMessage.setMessage(this);
    }

    public void removeUserMessage(UserMessageModel userMessage) {
        userMessages.remove(userMessage);
        userMessage.setMessage(null);
    }
}