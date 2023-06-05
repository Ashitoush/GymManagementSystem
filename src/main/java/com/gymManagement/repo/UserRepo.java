package com.gymManagement.repo;

import com.gymManagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {

    User findByUserName(String username);
}
