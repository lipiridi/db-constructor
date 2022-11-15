package com.divizia.dbconstructor.validation;

import com.divizia.dbconstructor.validation.impl.PasswordValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PasswordValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Password {

    String message() default "Please, check your password inputs";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
