package com.divizia.dbconstructor.controller;

import com.divizia.dbconstructor.model.entity.Role;
import com.divizia.dbconstructor.model.entity.User;
import com.divizia.dbconstructor.model.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.net.http.HttpRequest;
import java.util.Optional;

@Controller
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping(value = "/")
    public String authorize(Model model) {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> optionalUser = userService.findByEmail(currentUserEmail);
        User user = optionalUser.orElse(new User());

        model.addAttribute("user", user);
        model.addAttribute("isAdmin", user.getRole() == Role.ADMIN);
        return "welcome";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

    @GetMapping("/welcome")
    public String getSuccessPage() {
        return "redirect:/";
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String postRegisterPage(@Valid User user, BindingResult result, Model model) {
        if (ControllerHelper.hasErrors(result, model)) return "register";

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.saveAndFlush(user);
        return "redirect:/";
    }


}