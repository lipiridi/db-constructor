package com.divizia.dbconstructor.controller;

import com.divizia.dbconstructor.exceptions.UserNotFoundException;
import com.divizia.dbconstructor.model.entity.User;
import com.divizia.dbconstructor.model.enums.Role;
import com.divizia.dbconstructor.model.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.Optional;

@SuppressWarnings("SameReturnValue")
@Controller
@RequestMapping("users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("all")
    public String getAll(Model model) {
        model.addAttribute("users", userService.findAll());
        return "users/all";
    }

    @GetMapping("edit/{id}")
    public String getEdit(@PathVariable String id, Model model) {
        model.addAttribute("user", userService.findById(id).orElseThrow(() -> new UserNotFoundException(id)));
        model.addAttribute("roles", Role.values());
        return "users/edit";
    }

    @PostMapping("edit/{id}")
    public String postEdit(@PathVariable String id, @Valid User user, BindingResult result, Model model) {
        if (ControllerHelper.hasErrors(result, model)) {
            model.addAttribute("roles", Role.values());
            return "users/edit";
        }

        Optional<User> foundInDB = userService.findById(id);
        if (foundInDB.isPresent())
            user = foundInDB.get().updateAllowed(user);

        userService.saveAndFlush(user);

        return "redirect:/users/all";
    }

    @PostMapping("delete/{id}")
    public String postDelete(@PathVariable String id) {
        userService.deleteById(id);
        return "redirect:/users/all";
    }

}
