package com.tasktwo.timelord.server.repository;

import com.tasktwo.timelord.server.model.MessageModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<MessageModel, Long> {
}
