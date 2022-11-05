package com.divizia.dbconstructor.controller;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ControllerHelper {
    public static boolean hasErrors(BindingResult result, Model model) {
        model.addAttribute("errorList", new ArrayList<String>());

        if (!result.hasErrors()) return false;

        model.addAttribute("errorList",
                result.getAllErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.toList()));

        return true;
    }

    @SuppressWarnings({"rawtypes", "OptionalUsedAsFieldOrParameterType"})
    public static boolean exists(Optional optional, Model model) {
        model.addAttribute("errorList", new ArrayList<String>());

        if (optional.isEmpty()) return false;

        model.addAttribute("errorList",
                List.of(optional.get().getClass().getSimpleName()
                        + " with such data already exists!"));

        return true;
    }

}
