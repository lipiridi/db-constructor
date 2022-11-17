package com.divizia.dbconstructor.dto;

import com.divizia.dbconstructor.validation.ActualPassword;
import com.divizia.dbconstructor.validation.Password;

import javax.validation.constraints.NotBlank;

@Password
public record PasswordEditDto(
        String username,
        @NotBlank(message = "Password can't be blank") @ActualPassword String currentPassword,
        @NotBlank(message = "Password can't be blank") String newPassword,
        @NotBlank(message = "Password can't be blank") String repeatPassword
) {

}
