package com.divizia.dbconstructor.model.service.impl;

import com.divizia.dbconstructor.model.entity.CustomTable;
import com.divizia.dbconstructor.model.repo.CustomTableRepository;
import com.divizia.dbconstructor.model.service.CustomTableService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CustomTableServiceImpl implements CustomTableService {

    private final CustomTableRepository customTableRepository;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public CustomTable saveAndFlush(CustomTable customTable) {

        if (!customTable.getId().startsWith("c_"))
            customTable.setId("c_" + customTable.getId());

        jdbcTemplate.execute(
                String.format("create table if not exists %1$s (id bigserial constraint %1$s_pkey primary key)",
                        customTable.getId()));

        return customTableRepository.saveAndFlush(customTable);
    }

    @Override
    public void deleteById(String id) {

        jdbcTemplate.execute(
                String.format("drop table if exists %s", id));

        customTableRepository.deleteById(id);
    }

    @Override
    public Optional<CustomTable> findById(String id) {
        return customTableRepository.findById(id);
    }

    @Override
    public List<CustomTable> findAll() {
        return customTableRepository.findAll(Sort.by("id"));
    }
}
