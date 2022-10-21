package com.divizia.dbconstructor.model.enums;

public enum RequisiteType {

    STRING("varchar"),
    BOOLEAN("boolean"),
    INTEGER("integer"),
    LONG("bigint"),
    DOUBLE("double precision"),
    FOREIGN("bigint");

    public final String dbName;

    RequisiteType(String dbName) {
        this.dbName = dbName;
    }

    public static String toStringForDB(Object o) {
        if (o == null)
            return "null";
        if (o instanceof String)
            return "'" + o + "'";

        return o.toString();
    }

}
