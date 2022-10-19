package com.divizia.dbconstructor.model.service;

import com.divizia.dbconstructor.model.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User saveAndFlush(User user);

    void deleteById(Long id);

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    List<User> findAll();

}
