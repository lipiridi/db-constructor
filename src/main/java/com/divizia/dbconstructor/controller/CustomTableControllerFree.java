package com.divizia.dbconstructor.controller;

import com.divizia.dbconstructor.model.service.CustomTableService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@SuppressWarnings("SameReturnValue")
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
