package com.divizia.dbconstructor.model.service.impl;

import com.divizia.dbconstructor.exceptions.ExistingRelationshipException;
import com.divizia.dbconstructor.model.entity.CustomTable;
import com.divizia.dbconstructor.model.entity.User;
import com.divizia.dbconstructor.model.repo.UserRepository;
import com.divizia.dbconstructor.model.service.CustomTableService;
import com.divizia.dbconstructor.model.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CustomTableService customTableService;

    @Override
    public User saveAndFlush(User user) {
        return userRepository.saveAndFlush(user);
    }

    @Override
    public void deleteById(String id) {
        List<CustomTable> customTables = customTableService.findByAuthorId(id);

        if (!customTables.isEmpty())
            throw new ExistingRelationshipException(customTables);

        userRepository.deleteById(id);
    }

    @Override
    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll(Sort.by("id"));
    }
}
