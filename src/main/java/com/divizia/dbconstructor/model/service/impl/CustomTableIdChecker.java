package com.divizia.dbconstructor.model.service.impl;

import org.springframework.stereotype.Component;

@Component
public class CustomTableIdChecker {

    //We add "c_" to all ids to signify tables as custom
    public String checkId(String id) {
        if (id == null || id.isBlank()) return null;
        return id.startsWith("c_") ? id : "c_" + id;
    }

}
