package com.divizia.dbconstructor.model.service.impl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CustomTableIdCheckerTest {

    @Test
    void checkId() {
        CustomTableIdChecker customTableIdChecker = new CustomTableIdChecker();
        assertEquals("c_books", customTableIdChecker.checkId("books"));
        assertEquals("c_books", customTableIdChecker.checkId("c_books"));
        assertNull(customTableIdChecker.checkId(null));
        assertNull(customTableIdChecker.checkId("  "));
    }
}