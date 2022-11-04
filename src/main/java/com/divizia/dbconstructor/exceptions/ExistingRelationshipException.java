package com.divizia.dbconstructor.exceptions;

import java.util.Collection;
import java.util.stream.Collectors;

public class ExistingRelationshipException extends RuntimeException {

    public ExistingRelationshipException(Collection<?> objects) {
        super("You can't delete this object because it's used as reference in other objects:\n"
                + objects.stream().map(Object::toString).collect(Collectors.joining("\n")));
    }

}
