package com.divizia.dbconstructor.model.service.impl;

import com.divizia.dbconstructor.model.compositeKeys.RequisiteId;
import com.divizia.dbconstructor.model.entity.Requisite;
import com.divizia.dbconstructor.model.repo.RequisiteRepository;
import com.divizia.dbconstructor.model.service.RequisiteService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RequisiteServiceImpl implements RequisiteService {

    private final RequisiteRepository requisiteRepository;

    public RequisiteServiceImpl(RequisiteRepository requisiteRepository) {
        this.requisiteRepository = requisiteRepository;
    }

    @Override
    public Requisite saveAndFlush(Requisite requisite) {
        return requisiteRepository.saveAndFlush(requisite);
    }

    @Override
    public void deleteById(RequisiteId requisiteId) {
        requisiteRepository.deleteById(requisiteId);
    }

    @Override
    public Optional<Requisite> findById(RequisiteId requisiteId) {
        return requisiteRepository.findById(requisiteId);
    }

    @Override
    public List<Requisite> findByCustomTableId(String customTableId) {
        return requisiteRepository.findByCustomTableId(customTableId);
    }

    @Override
    public List<Requisite> findAll() {
        return requisiteRepository.findAll();
    }
}
