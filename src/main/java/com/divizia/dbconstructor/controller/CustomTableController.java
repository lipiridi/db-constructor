package com.divizia.dbconstructor.controller;

import com.divizia.dbconstructor.model.compositekeys.RequisiteId;
import com.divizia.dbconstructor.model.entity.CustomTable;
import com.divizia.dbconstructor.model.entity.Requisite;
import com.divizia.dbconstructor.model.entity.User;
import com.divizia.dbconstructor.model.enums.RequisiteType;
import com.divizia.dbconstructor.model.service.CustomTableService;
import com.divizia.dbconstructor.model.service.RequisiteService;
import com.divizia.dbconstructor.model.service.UserService;
import lombok.AllArgsConstructor;
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
import java.util.List;
import java.util.Optional;

@SuppressWarnings("SameReturnValue")
@Controller
@RequestMapping("tables")
@PreAuthorize("hasAuthority('ADMIN')")
@AllArgsConstructor
public class CustomTableController {

    private final CustomTableService customTableService;
    private final RequisiteService requisiteService;
    private final UserService userService;

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
        if (ControllerHelper.hasErrors(result, model) ||
                ControllerHelper.exists(
                        customTableService.findById(customTable.getId()),
                        model))
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
        requisite.setType(RequisiteType.STRING);

        return getEditWithValues(model, customTable, requisite);
    }

    public String getEditWithValues(Model model, CustomTable customTable, Requisite requisite) {
        model.addAttribute("customTable", customTable);
        model.addAttribute("requisites", requisiteService.findByCustomTableId(customTable.getId()));
        model.addAttribute("requisiteTypes", RequisiteType.values());
        model.addAttribute("requisite", requisite);
        model.addAttribute("otherTables", customTableService.findAll().stream().filter(x -> !x.equals(customTable)).toList());
        return "tables/edit";
    }

    @PostMapping("edit/{id}")
    public String postEdit(@PathVariable String id, @Valid CustomTable customTable, BindingResult result, Model model) {
        if (ControllerHelper.hasErrors(result, model))
            return getEditWithValues(model, customTable);

        Optional<CustomTable> foundInDB = customTableService.findById(id);
        if (foundInDB.isPresent())
            customTable = foundInDB.get().updateAllowed(customTable);

        customTableService.saveAndFlush(customTable);

        return "redirect:/tables/all";
    }

    @PostMapping("delete/{id}")
    public String postDelete(@PathVariable String id) {
        customTableService.deleteById(id);
        return "redirect:/tables/all";
    }

    @PostMapping("requisite/add")
    public String postAdd(@Valid Requisite requisite, BindingResult result, Model model) {
        if (ControllerHelper.hasErrors(result, model) ||
                ControllerHelper.exists(
                        requisiteService.findById(
                                new RequisiteId(
                                        requisite.getId(),
                                        requisite.getCustomTable().getId())),
                        model))
            return getEditWithValues(model, requisite.getCustomTable(), requisite);
        if (requisite.getType() == RequisiteType.FOREIGN && requisite.getForeignTableId().isEmpty()) {
            model.addAttribute("errorList", List.of("You didn't choose foreign table!"));
            return getEditWithValues(model, requisite.getCustomTable(), requisite);
        }

        requisiteService.saveAndFlush(requisite);

        return "redirect:/tables/edit/" + requisite.getCustomTable().getId();
    }

    @PostMapping("requisite/delete/{customTableId}/{id}")
    public String postDelete(@PathVariable String customTableId, @PathVariable String id) {
        requisiteService.deleteById(new RequisiteId(id, customTableId));
        return "redirect:/tables/edit/" + customTableId;
    }

}
