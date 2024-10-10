package com.tasktwo.timelord.server.repository;

import com.tasktwo.timelord.server.model.UserMessageModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserMessageRepository extends JpaRepository<UserMessageModel, Long> {
}
