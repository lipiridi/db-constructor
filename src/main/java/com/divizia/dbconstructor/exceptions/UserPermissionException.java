package com.divizia.dbconstructor.exceptions;

public class UserPermissionException extends RuntimeException {

    public UserPermissionException() {
        super("You can change only your data!");
    }

}
