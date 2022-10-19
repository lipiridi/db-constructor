package com.divizia.dbconstructor.model.repo;

import com.divizia.dbconstructor.model.entity.CustomTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomTableRepository extends JpaRepository<CustomTable, String> {
}
