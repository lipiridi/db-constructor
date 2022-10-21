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
import javax.websocket.server.PathParam;
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
    ResponseEntity<Map<String, Object>> all(@PathParam("id") String id) {
        if (id != null)
            return findById(id);

        Map<String, Object> answer = new HashMap<>();
        answer.put("result", "OK");
        answer.put("entity", userService.findAll());

        return new ResponseEntity<>(answer, HttpStatus.OK);
    }

    ResponseEntity<Map<String, Object>> findById(String id) {
        Map<String, Object> answer = new HashMap<>();
        Optional<User> optionalUser = userService.findById(id);

        if (optionalUser.isEmpty()) {
            answer.put("result", "Error");
            answer.put("message", new UserNotFoundException(id).getMessage());
            return new ResponseEntity<>(answer, HttpStatus.BAD_REQUEST);
        } else {
            answer.put("result", "OK");
            answer.put("entity", optionalUser.get());
            return new ResponseEntity<>(answer, HttpStatus.OK);
        }
    }

    @PostMapping
    ResponseEntity<Map<String, Object>> save(@Valid @RequestBody User user, BindingResult result) {
        Map<String, Object> answer = new HashMap<>();

        if (result.hasErrors()) {
            answer.put("result", "Error");
            answer.put("message", result.getAllErrors().stream().map(x -> x.getDefaultMessage() + "; ").collect(Collectors.joining()));
            return new ResponseEntity<>(answer, HttpStatus.BAD_REQUEST);
        } else {
            Optional<User> foundInDB = userService.findById(user.getId());
            if (foundInDB.isPresent())
                user = foundInDB.get().updateAllowed(user);
            else
                user.setPassword(passwordEncoder.encode(user.getPassword()));


            answer.put("result", "OK");
            answer.put("entity", userService.saveAndFlush(user));
            return new ResponseEntity<>(answer, HttpStatus.CREATED);
        }
    }

    @DeleteMapping
    ResponseEntity<Map<String, Object>> delete(@PathParam("id") String id) {
        Map<String, Object> answer = new HashMap<>();
        Optional<User> optionalUser = Optional.empty();
        if (id != null)
            optionalUser = userService.findById(id);

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
