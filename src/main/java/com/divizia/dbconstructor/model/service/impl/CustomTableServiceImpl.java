package com.divizia.dbconstructor.model.service.impl;

import com.divizia.dbconstructor.model.entity.CustomTable;
import com.divizia.dbconstructor.model.repo.CustomTableRepository;
import com.divizia.dbconstructor.model.service.CustomTableService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomTableServiceImpl implements CustomTableService {

    private final CustomTableRepository customTableRepository;

    public CustomTableServiceImpl(CustomTableRepository customTableRepository) {
        this.customTableRepository = customTableRepository;
    }

    @Override
    public CustomTable saveAndFlush(CustomTable customTable) {
        return customTableRepository.saveAndFlush(customTable);
    }

    @Override
    public void deleteById(String id) {
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
