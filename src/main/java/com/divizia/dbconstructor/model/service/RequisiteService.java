package com.divizia.dbconstructor.model.service;

import com.divizia.dbconstructor.model.compositekeys.RequisiteId;
import com.divizia.dbconstructor.model.entity.Requisite;

import java.util.List;
import java.util.Optional;

public interface RequisiteService {

    Requisite saveAndFlush(Requisite requisite);

    void deleteById(RequisiteId requisiteId);

    Optional<Requisite> findById(RequisiteId requisiteId);

    List<Requisite> findByCustomTableId(String customTableId);

    List<Requisite> findAll();

}
