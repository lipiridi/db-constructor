package com.divizia.dbconstructor.validation.impl;

import com.divizia.dbconstructor.exceptions.UserNotFoundException;
import com.divizia.dbconstructor.model.entity.User;
import com.divizia.dbconstructor.model.service.UserService;
import com.divizia.dbconstructor.validation.ActualPassword;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class ActualPasswordValidator implements ConstraintValidator<ActualPassword, String> {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        return userService.findById(SecurityContextHolder.getContext().getAuthentication().getName())
                .map(User::getPassword)
                .map(userPassword -> passwordEncoder.matches(password, userPassword))
                .orElseThrow(() -> new UserNotFoundException("User from security context"));
    }
}
