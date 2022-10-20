package com.divizia.dbconstructor.model.enums;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Map;

public enum RequisiteType {

    STRING("varchar"),
    BOOLEAN("boolean"),
    INTEGER("integer"),
    LONG("bigint"),
    DOUBLE("double precision");

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
