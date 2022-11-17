package com.divizia.dbconstructor.validation;

import com.divizia.dbconstructor.validation.impl.ActualPasswordValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ActualPasswordValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ActualPassword {

    String message() default "Please, input your actual password here";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
