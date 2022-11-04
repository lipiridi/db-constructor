package com.divizia.dbconstructor.model.service;

import com.divizia.dbconstructor.model.entity.Record;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface RecordService {

    Record saveAndFlush(Record record);

    void deleteById(String customTableId, Long recordId);

    Optional<Record> findById(String customTableId, Long recordId);

    List<Record> findAllById(String customTableId, Collection<Long> ids);

    List<Record> findAll(String customTableId);

}
