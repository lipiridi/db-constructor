package com.divizia.dbconstructor.model.service;

import com.divizia.dbconstructor.model.entity.CustomTable;

import java.util.List;
import java.util.Optional;

public interface CustomTableService {

    CustomTable saveAndFlush(CustomTable customTable);

    void deleteById(String id);

    Optional<CustomTable> findById(String id);

    Optional<CustomTable> findByIdWithRequisites(String id);

    List<CustomTable> findAll();

    List<CustomTable> findAllWithRequisites();

    List<CustomTable> findByAuthorId(String userId);

}
