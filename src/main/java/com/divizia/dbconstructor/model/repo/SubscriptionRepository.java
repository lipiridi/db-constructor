package com.divizia.dbconstructor.model.repo;

import com.divizia.dbconstructor.model.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    @Query("from Subscription where customTable.id = :id order by customTable.id, user.id")
    List<Subscription> findByCustomTableId(@Param("id") String customTableId);

    @Query("from Subscription where user.id = :id order by user.id, customTable.id")
    List<Subscription> findByUserId(@Param("id") String userId);

    @Modifying
    @Query("delete from Subscription where user.id = :userId and customTable.id = :customTableId")
    void deleteByUserAndCustomTableId(@Param("userId") String userId, @Param("customTableId") String customTableId);

    @Query("from Subscription where user.id = :userId and customTable.id = :customTableId")
    Optional<Subscription> findByUserAndCustomTableId(@Param("userId") String userId, @Param("customTableId") String customTableId);

}
