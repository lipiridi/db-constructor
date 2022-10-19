package com.divizia.dbconstructor.controller.api;

import com.divizia.dbconstructor.exceptions.UserNotFoundException;
import com.divizia.dbconstructor.model.entity.User;
import com.divizia.dbconstructor.model.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/users")
@PreAuthorize("hasAuthority('ADMIN')")
public class UserRestController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    UserRestController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    ResponseEntity<Map<String, Object>> all() {
        Map<String, Object> answer = new HashMap<>();
        answer.put("result", "OK");
        answer.put("users", userService.findAll());

        return new ResponseEntity<>(answer, HttpStatus.OK);
    }

    @GetMapping("{id}")
    ResponseEntity<Map<String, Object>> findById(@PathVariable Long id) {
        Map<String, Object> answer = new HashMap<>();
        Optional<User> optionalUser = userService.findById(id);

        if (optionalUser.isEmpty()) {
            answer.put("result", "Error");
            answer.put("message", new UserNotFoundException(id).getMessage());
            return new ResponseEntity<>(answer, HttpStatus.BAD_REQUEST);
        } else {
            answer.put("result", "OK");
            answer.put("user", optionalUser.get());
            return new ResponseEntity<>(answer, HttpStatus.OK);
        }
    }

    @PostMapping
    ResponseEntity<Map<String, Object>> create(@Valid @RequestBody User user, BindingResult result) {
        Map<String, Object> answer = new HashMap<>();

        if (result.hasErrors()) {
            answer.put("result", "Error");
            answer.put("message", result.getAllErrors().stream().map(x -> x.getDefaultMessage() + "; ").collect(Collectors.joining()));
            return new ResponseEntity<>(answer, HttpStatus.BAD_REQUEST);
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            answer.put("result", "OK");
            answer.put("user", userService.saveAndFlush(user));
            return new ResponseEntity<>(answer, HttpStatus.CREATED);
        }
    }

    @PutMapping("{id}")
    ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @Valid @RequestBody User user, BindingResult result) {
        Map<String, Object> answer = new HashMap<>();

        if (result.hasErrors()) {
            answer.put("result", "Error");
            answer.put("message", result.getAllErrors().stream().map(x -> x.getDefaultMessage() + "; ").collect(Collectors.joining()));
            return new ResponseEntity<>(answer, HttpStatus.BAD_REQUEST);
        } else {
            user.setId(id);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            answer.put("result", "OK");
            answer.put("user", userService.saveAndFlush(user));
            return new ResponseEntity<>(answer, HttpStatus.OK);
        }
    }

    @DeleteMapping("{id}")
    ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        Map<String, Object> answer = new HashMap<>();
        Optional<User> optionalUser = userService.findById(id);

        if (optionalUser.isEmpty()) {
            answer.put("result", "Error");
            answer.put("message", new UserNotFoundException(id).getMessage());
            return new ResponseEntity<>(answer, HttpStatus.BAD_REQUEST);
        } else {
            userService.deleteById(id);
            answer.put("result", "OK");
            return new ResponseEntity<>(answer, HttpStatus.ACCEPTED);
        }
    }

}
