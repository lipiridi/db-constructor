package com.divizia.dbconstructor.validation.impl;

import com.divizia.dbconstructor.dto.PasswordEditDto;
import com.divizia.dbconstructor.exceptions.UserNotFoundException;
import com.divizia.dbconstructor.model.entity.User;
import com.divizia.dbconstructor.model.service.UserService;
import com.divizia.dbconstructor.validation.Password;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class PasswordValidator implements ConstraintValidator<Password, PasswordEditDto> {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean isValid(PasswordEditDto passwordDto, ConstraintValidatorContext constraintValidatorContext) {

        return userService.findById(passwordDto.username())
                .map(User::getPassword)
                .map(password -> passwordEncoder.matches(passwordDto.currentPassword(), password)
                        && !passwordDto.currentPassword().equals(passwordDto.newPassword())
                        && passwordDto.newPassword().equals(passwordDto.repeatPassword())
                )
                .orElseThrow(() -> new UserNotFoundException(passwordDto.username()));

    }
}
