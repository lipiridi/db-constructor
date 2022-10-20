package com.divizia.dbconstructor.model.service.impl;

import com.divizia.dbconstructor.model.compositekeys.RequisiteId;
import com.divizia.dbconstructor.model.entity.Requisite;
import com.divizia.dbconstructor.model.repo.RequisiteRepository;
import com.divizia.dbconstructor.model.service.RequisiteService;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RequisiteServiceImpl implements RequisiteService {

    private final RequisiteRepository requisiteRepository;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Requisite saveAndFlush(Requisite requisite) {

        jdbcTemplate.execute(
                String.format("alter table %s add %s %s",
                        requisite.getCustomTable().getId(),
                        requisite.getId(),
                        requisite.getType().dbName));

        return requisiteRepository.saveAndFlush(requisite);
    }

    @Override
    public void deleteById(RequisiteId requisiteId) {

        jdbcTemplate.execute(
                String.format("alter table %s drop column %s",
                        requisiteId.getCustomTable(),
                        requisiteId.getId()));

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
