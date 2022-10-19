package com.divizia.dbconstructor.controller;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
