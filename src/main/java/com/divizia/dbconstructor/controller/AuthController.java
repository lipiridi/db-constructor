package com.divizia.dbconstructor.controller;

import com.divizia.dbconstructor.model.compositekeys.RequisiteId;
import com.divizia.dbconstructor.model.entity.User;
import com.divizia.dbconstructor.model.enums.Role;
import com.divizia.dbconstructor.model.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("SameReturnValue")
@Controller
@AllArgsConstructor
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping(value = "/")
    public String authorize(Model model) {
        return "welcome";
    }

    @GetMapping("login")
    public String getLoginPage() {
        return "login";
    }

    @PostMapping ("login/setAttributes")
    public String postLoginPage(HttpSession httpSession) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        httpSession.setAttribute("userName", authentication.getName());
        httpSession.setAttribute("userIsAdmin", authentication.getAuthorities().contains(Role.ADMIN));
        return "redirect:/";
    }

    @GetMapping("welcome")
    public String getWelcomePage() {
        return "redirect:/";
    }

    @GetMapping("register")
    public String getRegisterPage(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", Role.values());
        return "register";
    }

    @PostMapping("register")
    public String postRegisterPage(@Valid User user, BindingResult result, Model model) {
        if (ControllerHelper.hasErrors(result, model) ||
                ControllerHelper.exists(
                        userService.findById(user.getId()),
                        model)) {
            model.addAttribute("roles", Role.values());
            return "register";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.saveAndFlush(user);
        return "redirect:/";
    }


}
