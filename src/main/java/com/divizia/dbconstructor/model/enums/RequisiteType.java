package com.divizia.dbconstructor.model.enums;

import java.time.LocalDateTime;

public enum RequisiteType {

    STRING("varchar"),
    BOOLEAN("boolean"),
    INTEGER("integer"),
    LONG("bigint"),
    DOUBLE("double precision"),
    LOCAL_DATE_TIME("timestamp"),
    FOREIGN("bigint");

    public final String dbName;

    RequisiteType(String dbName) {
        this.dbName = dbName;
    }

    public static String toStringForDB(Object o) {
        if (o == null)
            return "null";
        if (o instanceof String || o instanceof LocalDateTime)
            return "'" + o + "'";

        return o.toString();
    }

}
