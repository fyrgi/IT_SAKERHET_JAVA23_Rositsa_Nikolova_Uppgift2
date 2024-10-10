package com.tasktwo.timelord.server.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_message")
public class UserMessageModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private UserModel user;

    @ManyToOne
    @JoinColumn(name = "id_message", nullable = false)
    private MessageModel message;
}