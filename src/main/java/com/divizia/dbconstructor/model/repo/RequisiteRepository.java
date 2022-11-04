package com.divizia.dbconstructor.model.repo;

import com.divizia.dbconstructor.model.compositekeys.RequisiteId;
import com.divizia.dbconstructor.model.entity.Requisite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequisiteRepository extends JpaRepository<Requisite, RequisiteId> {

    List<Requisite> findByCustomTableId(String customTableId);

}
