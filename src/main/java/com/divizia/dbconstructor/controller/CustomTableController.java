package com.divizia.dbconstructor.controller;

import com.divizia.dbconstructor.model.compositeKeys.RequisiteId;
import com.divizia.dbconstructor.model.entity.CustomTable;
import com.divizia.dbconstructor.model.entity.Requisite;
import com.divizia.dbconstructor.model.entity.User;
import com.divizia.dbconstructor.model.enums.RequisiteType;
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

@SuppressWarnings("SameReturnValue")
@Controller
@RequestMapping("tables")
@PreAuthorize("hasAuthority('ADMIN')")
public class CustomTableController {

    private final CustomTableService customTableService;
    private final RequisiteService requisiteService;
    private final UserService userService;

    public CustomTableController(CustomTableService customTableService, RequisiteService requisiteService, UserService userService) {
        this.customTableService = customTableService;
        this.requisiteService = requisiteService;
        this.userService = userService;
    }

    @GetMapping("create")
    public String getCreate(Model model) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> optionalUser = userService.findById(currentUsername);

        CustomTable customTable = new CustomTable();
        customTable.setAuthor(optionalUser.orElseThrow());
        model.addAttribute("customTable", customTable);

        return "tables/create";
    }

    @PostMapping("create")
    public String postCreate(@Valid CustomTable customTable, BindingResult result, Model model) {
        if (ControllerHelper.hasErrors(result, model))
            return "tables/create";

        CustomTable customTableSaved = customTableService.saveAndFlush(customTable);

        return "redirect:/tables/edit/" + customTableSaved.getId();
    }

    @GetMapping("edit/{id}")
    public String getEdit(@PathVariable String id, Model model) {
        CustomTable customTable = customTableService.findById(id).orElseThrow();

        return getEditWithValues(model, customTable);
    }

    public String getEditWithValues(Model model, CustomTable customTable) {
        Requisite requisite = new Requisite();
        requisite.setCustomTable(customTable);
        requisite.setType(RequisiteType.VARCHAR);

        return getEditWithValues(model, customTable, requisite);
    }

    public String getEditWithValues(Model model, CustomTable customTable, Requisite requisite) {
        model.addAttribute("customTable", customTable);
        model.addAttribute("requisites", requisiteService.findByCustomTableId(customTable.getId()));
        model.addAttribute("requisiteTypes", RequisiteType.values());
        model.addAttribute("requisite", requisite);
        return "tables/edit";
    }

    @PostMapping("edit/{id}")
    public String postEdit(@PathVariable String id, @Valid CustomTable customTable, BindingResult result, Model model) {
        if (ControllerHelper.hasErrors(result, model))
            return getEditWithValues(model, customTable);

        customTableService.findById(id).ifPresent(customTable::updateAllowed);
        customTableService.saveAndFlush(customTable);

        return "redirect:/tables/all";
    }

    @PostMapping("delete/{id}")
    public String postDelete(@PathVariable String id) {
        customTableService.deleteById(id);
        return "redirect:/tables/all";
    }

    @PostMapping("{customTableId}/requisite/add")
    public String postAdd(@PathVariable String customTableId, @Valid Requisite requisite, BindingResult result, Model model) {
        if (ControllerHelper.hasErrors(result, model))
            return getEditWithValues(model, requisite.getCustomTable(), requisite);

        requisiteService.saveAndFlush(requisite);

        return "redirect:/tables/edit/" + requisite.getCustomTable().getId();
    }

    @PostMapping("{customTableId}/requisite/delete/{id}")
    public String postDelete(@PathVariable String customTableId, @PathVariable String id) {
        requisiteService.deleteById(new RequisiteId(id, customTableId));
        return "redirect:/tables/edit/" + customTableId;
    }

}
