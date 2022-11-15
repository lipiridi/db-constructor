package com.divizia.dbconstructor.model.service;

import com.divizia.dbconstructor.model.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User saveAndFlush(User user);

    void deleteById(String id);

    Optional<User> findById(String id);

    List<User> findAll();

    User updatePassword(String id, String password);

}
