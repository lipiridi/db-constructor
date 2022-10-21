package com.divizia.dbconstructor.model.repo;

import com.divizia.dbconstructor.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
