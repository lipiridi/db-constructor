package com.divizia.dbconstructor.model.service;

public class IdChecker {

    //We add "c_" to all ids to signify tables and requisites as custom
    public static String checkId(String id) {
        if (id == null || id.isBlank()) return null;
        return id.startsWith("c_") ? id : "c_" + id;
    }

}
