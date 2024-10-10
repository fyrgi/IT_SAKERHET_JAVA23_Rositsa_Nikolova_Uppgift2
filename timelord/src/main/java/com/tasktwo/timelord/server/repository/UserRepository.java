package com.tasktwo.timelord.server.repository;

import com.tasktwo.timelord.server.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserModel, Long>{
}
