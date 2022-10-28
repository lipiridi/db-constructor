package com.divizia.dbconstructor.model.serializers;

import java.time.format.DateTimeFormatter;

public class Formatter {

    public static final DateTimeFormatter formatNormal = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    public static final DateTimeFormatter formatISO = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

}
