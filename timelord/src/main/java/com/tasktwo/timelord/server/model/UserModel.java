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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@ToString(exclude = "userMessages")
@EqualsAndHashCode(exclude = "userMessages")
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="email", nullable = false, unique = true)
    private String email;

    @Column(name="password",nullable = false)
    private String password;

    // One user can have many messages
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<UserMessageModel> userMessages = new ArrayList<>();

    // Helper methods to manage bidirectional relationship
    public void addUserMessage(UserMessageModel userMessage) {
        userMessages.add(userMessage);
        userMessage.setUser(this);
    }

    public void removeUserMessage(UserMessageModel userMessage) {
        userMessages.remove(userMessage);
        userMessage.setUser(null);
    }

}