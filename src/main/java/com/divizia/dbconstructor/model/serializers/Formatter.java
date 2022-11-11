package com.divizia.dbconstructor.model.serializers;

import java.time.format.DateTimeFormatter;

public class Formatter {

    public static final String DATE_FORMAT = "dd.MM.yyyy HH:mm:ss";
    public static final String ISO_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    public static final DateTimeFormatter formatNormal = DateTimeFormatter.ofPattern(DATE_FORMAT);
    public static final DateTimeFormatter formatISO = DateTimeFormatter.ofPattern(ISO_DATE_FORMAT);

}
