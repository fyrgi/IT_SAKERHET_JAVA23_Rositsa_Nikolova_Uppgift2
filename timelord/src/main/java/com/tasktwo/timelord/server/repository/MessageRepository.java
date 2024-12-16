package com.tasktwo.timelord.server.repository;

import com.tasktwo.timelord.server.model.MessageModel;
import com.tasktwo.timelord.server.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<MessageModel, Long> {
    List<MessageModel> getMessagesByUser(UserModel user);
}
