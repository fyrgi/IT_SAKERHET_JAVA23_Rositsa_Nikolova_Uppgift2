package com.tasktwo.timelord.server.dto;

import lombok.*;
@Getter
@Setter
@AllArgsConstructor
public class AuthRequestDTO {
    private String email;
    private String password;
}