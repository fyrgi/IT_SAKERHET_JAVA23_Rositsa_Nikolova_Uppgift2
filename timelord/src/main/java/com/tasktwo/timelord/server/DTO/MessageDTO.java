package com.tasktwo.timelord.server.DTO;
import com.tasktwo.timelord.server.model.UserModel;
public class MessageDTO {
    private String token;
    private String message;
    private Long idUser;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

}
