package com.divizia.dbconstructor.model.service;

import com.divizia.dbconstructor.model.entity.Record;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RecordServiceTest {

    @Autowired
    private RecordService recordService;

    @Test
    void saveAndFlush() {
        Optional<Record> record = recordService.findById("c_chairs", 1L);
        record.get().setId(0L);
        Record record1 = recordService.saveAndFlush(record.get());
        System.out.println(record1.getId());
    }

    @Test
    void deleteById() {
    }

    @Test
    void findById() {
        Optional<Record> record = recordService.findById("c_books", 2L);
        System.out.println(record.get());
    }

    @Test
    void findAll() {
    }
}