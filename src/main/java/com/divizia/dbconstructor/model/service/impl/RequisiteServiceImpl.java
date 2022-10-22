package com.divizia.dbconstructor.model.service.impl;

import com.divizia.dbconstructor.model.compositekeys.RequisiteId;
import com.divizia.dbconstructor.model.entity.Requisite;
import com.divizia.dbconstructor.model.enums.RequisiteType;
import com.divizia.dbconstructor.model.repo.RequisiteRepository;
import com.divizia.dbconstructor.model.service.RequisiteService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class RequisiteServiceImpl implements RequisiteService {

    private final RequisiteRepository requisiteRepository;
    private final JdbcTemplate jdbcTemplate;
    private final CustomTableIdChecker customTableIdChecker;

    @Override
    public Requisite saveAndFlush(Requisite requisite) {
        requisite.setForeignTableId(customTableIdChecker.checkId(requisite.getForeignTableId()));

        String query = String.format("alter table %s add %s %s",
                        requisite.getCustomTable().getId(),
                        requisite.getId(),
                        requisite.getType().dbName);

        if (requisite.getType() == RequisiteType.FOREIGN) {
            String fkQuery = String.format("alter table %s add constraint %s_%s_id_fkey foreign key (%s) references %s " +
                            "on update cascade on delete cascade",
                    requisite.getCustomTable().getId(),
                    requisite.getCustomTable().getId(),
                    requisite.getForeignTableId(),
                    requisite.getId(),
                    requisite.getForeignTableId());

            query = query + "; " + fkQuery ;
        }

        log.info(query);
        jdbcTemplate.execute(query);

        return requisiteRepository.saveAndFlush(requisite);
    }

    @Override
    public void deleteById(RequisiteId requisiteId) {
        requisiteId.setCustomTable(customTableIdChecker.checkId(requisiteId.getCustomTable()));

        String query = String.format("alter table %s drop column %s",
                        requisiteId.getCustomTable(),
                        requisiteId.getId());
        log.info(query);
        jdbcTemplate.execute(query);

        requisiteRepository.deleteById(requisiteId);
    }

    @Override
    public Optional<Requisite> findById(RequisiteId requisiteId) {
        requisiteId.setCustomTable(customTableIdChecker.checkId(requisiteId.getCustomTable()));

        return requisiteRepository.findById(requisiteId);
    }

    @Override
    public List<Requisite> findByCustomTableId(String customTableId) {
        customTableId = customTableIdChecker.checkId(customTableId);

        return requisiteRepository.findByCustomTableId(customTableId);
    }

    @Override
    public List<Requisite> findAll() {
        return requisiteRepository.findAll();
    }
}
