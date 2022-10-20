package com.divizia.dbconstructor.exceptions;

public class RecordNotFoundException extends RuntimeException {

    public RecordNotFoundException(String customTableId, Long id) {
        super("Could not find record in table " + customTableId + " with id "+ id);
    }

}
