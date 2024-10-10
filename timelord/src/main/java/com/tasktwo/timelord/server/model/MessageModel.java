package com.tasktwo.timelord.server.model;

import jakarta.persistence.*;
import lombok.*;

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

    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL) // Explicitly mention the mapped field
    private List<UserMessageModel> userMessages;
}