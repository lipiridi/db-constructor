package com.divizia.dbconstructor.controller;

import com.divizia.dbconstructor.model.entity.User;
import com.divizia.dbconstructor.model.enums.Role;
import com.divizia.dbconstructor.model.service.CustomTableService;
import com.divizia.dbconstructor.model.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@SuppressWarnings("SameReturnValue")
@Controller
@RequestMapping("tables")
@AllArgsConstructor
public class CustomTableControllerFree {

    private final CustomTableService customTableService;
    private final UserService userService;

    @GetMapping("all")
    public String getAll(Model model) {
        model.addAttribute("tables", customTableService.findAllWithRequisites());
        return "tables/all";
    }

}
