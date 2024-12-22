package com.tasktwo.timelord.server.DTO;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
public class MessageDTO {
    private Long id;
    private String message;
    private Long idUser;
}