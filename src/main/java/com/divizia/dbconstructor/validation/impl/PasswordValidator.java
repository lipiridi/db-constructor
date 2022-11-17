package com.divizia.dbconstructor.validation.impl;

import com.divizia.dbconstructor.dto.PasswordEditDto;
import com.divizia.dbconstructor.validation.Password;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, PasswordEditDto> {

    @Override
    public boolean isValid(PasswordEditDto passwordDto, ConstraintValidatorContext constraintValidatorContext) {

        return !passwordDto.currentPassword().equals(passwordDto.newPassword())
                && passwordDto.newPassword().equals(passwordDto.repeatPassword());

    }
}
