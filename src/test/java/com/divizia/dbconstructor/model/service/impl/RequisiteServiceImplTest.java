package com.divizia.dbconstructor.model.service.impl;

import com.divizia.dbconstructor.model.entity.Record;
import com.divizia.dbconstructor.model.service.RecordService;
import com.divizia.dbconstructor.model.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RequisiteServiceImplTest {

    @Autowired
    private RecordService recordService;
    private final List<Record> recordList = new ArrayList<>();

    @AfterAll
    void tearDown() {
        recordList.forEach(x -> recordService.deleteById(x.getCustomTableId(), x.getId()));
    }

    @Test
    void saveAndFlush() {
    }

    @Test
    void deleteById() {
    }

    @Test
    void findById() {
    }

    @Test
    void findByCustomTableId() {
    }

    @Test
    void findAll() {
    }
}