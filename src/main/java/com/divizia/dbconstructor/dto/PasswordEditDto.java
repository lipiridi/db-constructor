package com.divizia.dbconstructor.dto;

import com.divizia.dbconstructor.validation.Password;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
@Password
public class PasswordEditDto {

    String username;

    @NotBlank(message = "Password can't be blank")
    String currentPassword;

    @NotBlank(message = "Password can't be blank")
    String newPassword;

    @NotBlank(message = "Password can't be blank")
    String repeatPassword;

}
