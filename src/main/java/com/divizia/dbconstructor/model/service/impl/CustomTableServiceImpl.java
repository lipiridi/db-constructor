package com.divizia.dbconstructor.model.service.impl;

import com.divizia.dbconstructor.model.entity.CustomTable;
import com.divizia.dbconstructor.model.repo.CustomTableRepository;
import com.divizia.dbconstructor.model.service.CustomTableService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class CustomTableServiceImpl implements CustomTableService {

    private final CustomTableRepository customTableRepository;
    private final JdbcTemplate jdbcTemplate;
    private final CustomTableIdChecker customTableIdChecker;

    @Override
    public CustomTable saveAndFlush(CustomTable customTable) {
        customTable.setId(customTableIdChecker.checkId(customTable.getId()));

        String query = String.format("create table if not exists %1$s (" +
                                "id bigserial constraint %1$s_pkey primary key," +
                                "update_time timestamp default current_timestamp not null)",
                        customTable.getId());
        log.info(query);
        jdbcTemplate.execute(query);

        return customTableRepository.saveAndFlush(customTable);
    }

    @Override
    public void deleteById(String id) {
        id = customTableIdChecker.checkId(id);

        String query = String.format("drop table if exists %s", id);
        log.info(query);
        jdbcTemplate.execute(query);

        customTableRepository.deleteById(id);
    }

    @Override
    public Optional<CustomTable> findById(String id) {
        id = customTableIdChecker.checkId(id);

        return customTableRepository.findById(id);
    }

    @Override
    public List<CustomTable> findAll() {
        return customTableRepository.findAll(Sort.by("id"));
    }

}
