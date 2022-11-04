package com.divizia.dbconstructor.model.repo;

import com.divizia.dbconstructor.model.entity.CustomTable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CustomTableRepository extends JpaRepository<CustomTable, String> {

    @EntityGraph(attributePaths = {"author", "requisites"})
    @Query("from CustomTable where id = :id")
    Optional<CustomTable> findByIdWithRequisites(@Param("id") String id);

    @EntityGraph(attributePaths = {"author", "requisites"})
    @Query("from CustomTable order by id")
    List<CustomTable> findAllWithRequisites();

    List<CustomTable> findByAuthorId(String userId);

}
