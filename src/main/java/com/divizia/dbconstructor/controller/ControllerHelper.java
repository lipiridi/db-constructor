package com.divizia.dbconstructor.controller;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ControllerHelper {
    public static boolean hasErrors(BindingResult result, Model model) {

        if (!result.hasErrors()) {
            model.addAttribute("errorList", new ArrayList<String>());
            return false;
        }

        model.addAttribute("errorList", result.getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList()));

        return true;

    }

}
