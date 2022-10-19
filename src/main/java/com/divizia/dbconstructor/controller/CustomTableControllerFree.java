package com.divizia.dbconstructor.controller;

import com.divizia.dbconstructor.model.entity.CustomTable;
import com.divizia.dbconstructor.model.entity.User;
import com.divizia.dbconstructor.model.service.CustomTableService;
import com.divizia.dbconstructor.model.service.RequisiteService;
import com.divizia.dbconstructor.model.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("tables")
public class CustomTableControllerFree {

    private final CustomTableService customTableService;


    public CustomTableControllerFree(CustomTableService customTableService) {
        this.customTableService = customTableService;
    }

    @GetMapping("all")
    public String getAll(Model model) {
        model.addAttribute("tables", customTableService.findAll());
        return "tables/all";
    }

}
