package com.divizia.dbconstructor.model.service.impl;

import com.divizia.dbconstructor.model.entity.User;
import com.divizia.dbconstructor.model.enums.Role;
import com.divizia.dbconstructor.model.service.UserService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceImplTest {

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private final List<User> userList = new ArrayList<>();

    @AfterAll
    void tearDown() {
        userList.forEach(x -> userService.deleteById(x.getId()));
    }

    private User getUser(String id, String password, Role role) {
        return new User(
                id,
                passwordEncoder.encode(password),
                null,
                role,
                new HashSet<>(),
                new HashSet<>());
    }

    @Test
    void saveAndFlush() {
        User user = getUser("testUser10", "1111", Role.ADMIN);
        user = userService.saveAndFlush(user);
        userList.add(user);

        assertEquals("testUser10", user.getId());
        assertNotEquals("1111", user.getPassword());

        user.updateAllowed(getUser("testUser10", "2222", Role.USER));
        userService.saveAndFlush(user);

        assertEquals("testUser10", user.getId());
        assertEquals(Role.USER, user.getRole());

        user.updateAllowed(getUser(null, "2222", Role.ADMIN));

        assertNotEquals(Role.ADMIN, user.getRole());

        user.setCustomTables(null);
        User finalUser = user;
        assertThrows(JpaSystemException.class, () -> userService.saveAndFlush(finalUser));
    }

    @Test
    void deleteById() {
        User user = getUser("testUser20", "1111", Role.ADMIN);
        user = userService.saveAndFlush(user);
        userService.deleteById(user.getId());

        assertTrue(userService.findById(user.getId()).isEmpty());
    }

    @Test
    void findById() {
        User user = getUser("testUser30", "1111", Role.ADMIN);
        user = userService.saveAndFlush(user);
        userList.add(user);

        assertTrue(userService.findById(user.getId()).isPresent());
    }

    @Test
    void findAll() {
        User user = getUser("testUser40", "1111", Role.ADMIN);
        user = userService.saveAndFlush(user);
        userList.add(user);

        assertFalse(userService.findAll().isEmpty());
    }
}