package com.gymManagement.repo;

import com.gymManagement.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepo extends JpaRepository<Authority, Long> {
    Authority findByAuthorityName(String name);
}
