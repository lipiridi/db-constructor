package com.divizia.dbconstructor.model.service.impl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class IdCheckerTest {

    @Test
    void checkId() {
        assertEquals("c_books", IdChecker.checkId("books"));
        assertEquals("c_books", IdChecker.checkId("c_books"));
        assertNull(IdChecker.checkId(null));
        assertNull(IdChecker.checkId("  "));
    }
}