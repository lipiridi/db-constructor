package com.divizia.dbconstructor.model.repo;

import com.divizia.dbconstructor.model.compositekeys.RequisiteId;
import com.divizia.dbconstructor.model.entity.Requisite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RequisiteRepository extends JpaRepository<Requisite, RequisiteId> {

    @Query("from Requisite where customTable.id = :id order by customTable.id, id")
    List<Requisite> findByCustomTableId(@Param("id") String customTableId);

}
