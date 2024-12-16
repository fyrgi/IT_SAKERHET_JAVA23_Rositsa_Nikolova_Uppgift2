package com.tasktwo.timelord.server.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="email", nullable = false, unique = true)
    private String email;

    @Column(name="password",nullable = false)
    private String password;

    @Column(name="dec-key", nullable = false)
    private String key;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<MessageModel> messages = new ArrayList<>();

}