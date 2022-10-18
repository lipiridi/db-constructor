package com.divizia.dbconstructor.controller;

import com.divizia.dbconstructor.model.entity.Role;
import com.divizia.dbconstructor.model.entity.User;
import com.divizia.dbconstructor.model.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("all")
    public String getAll(Model model) {
        model.addAttribute("users", userService.findAll());
        return "users/all";
    }

    @GetMapping("/edit/{id}")
    public String getEdit(@PathVariable long id, Model model) {
        model.addAttribute("user", userService.findById(id).orElseThrow());
        model.addAttribute("roles", Role.values());
        return "users/edit";
    }

    @PostMapping("/edit/{id}")
    public String postEdit(@PathVariable long id, @Valid User user, BindingResult result, Model model) {
        if (ControllerHelper.hasErrors(result, model)) {
            model.addAttribute("roles", Role.values());
            return "users/edit";
        }

        User userInDb = userService.findById(id).orElse(new User());
        if (!userInDb.getPassword().equals(user.getPassword()))
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.saveAndFlush(user);

        return "redirect:/users/all";
    }

    @PostMapping("/delete/{id}")
    public String postDelete(@PathVariable long id) {
        userService.deleteById(id);
        return "redirect:/users/all";
    }

}
