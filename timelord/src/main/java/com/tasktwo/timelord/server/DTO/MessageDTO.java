package com.tasktwo.timelord.server.DTO;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
public class MessageDTO {
    private int id;
    private String message;
    private int idUser;
}