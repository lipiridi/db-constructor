package com.divizia.dbconstructor.model.service.impl;

import com.divizia.dbconstructor.model.entity.CustomTable;
import com.divizia.dbconstructor.model.entity.Record;
import com.divizia.dbconstructor.model.entity.Requisite;
import com.divizia.dbconstructor.model.enums.RequisiteType;
import com.divizia.dbconstructor.model.service.CustomTableService;
import com.divizia.dbconstructor.model.service.RecordService;
import com.divizia.dbconstructor.model.service.RequisiteService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RecordServiceImplTest {

    @Autowired
    private RecordService recordService;
    @Autowired
    private CustomTableService customTableService;
    @Autowired
    private RequisiteService requisiteService;

    @BeforeAll
    void setUp() {
        CustomTable customTable = customTableService.saveAndFlush(
                new CustomTable("test_table", "Test table", null, null));
        CustomTable customTable2 = customTableService.saveAndFlush(
                new CustomTable("test_foreign_table", "Test foreign table", null, null));

        requisiteService.saveAndFlush(
                new Requisite("name", customTable, "Name", RequisiteType.STRING,  null));
        requisiteService.saveAndFlush(
                new Requisite("price", customTable, "Price", RequisiteType.DOUBLE,  null));
        requisiteService.saveAndFlush(
                new Requisite("foreign_table", customTable, "Foreign", RequisiteType.FOREIGN,  "test_foreign_table"));

        recordService.saveAndFlush(new Record(null, "test_foreign_table", null, null));
        recordService.saveAndFlush(new Record(null, "test_foreign_table", null, null));
        recordService.saveAndFlush(new Record(null, "test_foreign_table", null, null));
        recordService.saveAndFlush(new Record(null, "test_foreign_table", null, null));
    }

    @AfterAll
    void tearDown() {
        customTableService.deleteById("test_table");
        customTableService.deleteById("test_foreign_table");
    }

    @Test
    void saveAndFlush() {
        Record record = new Record(1L, "test_table", null, null);
        record = recordService.saveAndFlush(record);
        assertEquals(1L, record.getId());
        assertNotNull(record.getUpdateTime());

        HashMap<String, Object> requisites = new HashMap<>();
        requisites.put("name", "Harry Potter");
        requisites.put("price", 5.5);
        requisites.put("foreign_table", 1);
        record.setRequisiteValueMap(requisites);
        recordService.saveAndFlush(record);

        record = getFromDB(record.getCustomTableId(), record.getId());
        assertEquals("Harry Potter", record.getRequisiteValueMap().get("name"));
        assertEquals(5.5D, record.getRequisiteValueMap().get("price"));
        assertEquals(1L, record.getRequisiteValueMap().get("foreign_table"));

        record.getRequisiteValueMap().remove("price");
        recordService.saveAndFlush(record);
        record = getFromDB(record.getCustomTableId(), record.getId());
        assertEquals(5.5D, record.getRequisiteValueMap().get("price"));

        record.getRequisiteValueMap().put("price", null);
        recordService.saveAndFlush(record);
        record = getFromDB(record.getCustomTableId(), record.getId());
        assertNull(record.getRequisiteValueMap().get("price"));

        record.getRequisiteValueMap().put("foreign_table", 9);
        Record finalRecord = record;
        assertThrows(DataIntegrityViolationException.class, () -> recordService.saveAndFlush(finalRecord));
    }

    Record getFromDB(String customTableId, Long id) {
        Optional<Record> recordInDB = recordService.findById(customTableId, id);
        assertTrue(recordInDB.isPresent());
        return recordInDB.get();
    }

    @Test
    void deleteById() {
        Record record = new Record(5L, "test_table", null, null);
        recordService.saveAndFlush(record);
        recordService.deleteById(record.getCustomTableId(), record.getId());

        assertTrue(recordService.findById(record.getCustomTableId(), record.getId()).isEmpty());
    }

    @Test
    void findById() {
        Record record = new Record(7L, "test_table", null, null);
        recordService.saveAndFlush(record);

        assertTrue(recordService.findById(record.getCustomTableId(), record.getId()).isPresent());
    }

    @Test
    void findAll() {
        Record record = new Record(9L, "test_table", null, null);
        recordService.saveAndFlush(record);

        assertFalse(recordService.findAll("c_test_table").isEmpty());
        assertFalse(recordService.findAll("test_table").isEmpty());
        assertTrue(recordService.findAll("test_tableeeeee").isEmpty());
    }
}