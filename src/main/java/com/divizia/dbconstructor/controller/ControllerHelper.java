package com.divizia.dbconstructor.controller;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;

public class ControllerHelper {
    public static boolean hasErrors(BindingResult result, Model model) {

        if (!result.hasErrors()) {
            model.addAttribute("errorList", new ArrayList<String>());
            return false;
        }

        List<String> stringErrors = new ArrayList<>();

        for (ObjectError objectError : result.getAllErrors()) {
            stringErrors.add(objectError.getDefaultMessage());
        }

        model.addAttribute("errorList", stringErrors);

        return true;

    }

}
