package com.divizia.dbconstructor.model.repo;

import com.divizia.dbconstructor.model.compositekeys.RequisiteId;
import com.divizia.dbconstructor.model.compositekeys.SubscriptionId;
import com.divizia.dbconstructor.model.entity.Requisite;
import com.divizia.dbconstructor.model.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, SubscriptionId> {

    @Query("from Subscription where customTable.id = :id order by customTable.id, user.id")
    List<Subscription> findByCustomTableId(@Param("id") String customTableId);

    @Query("from Subscription where user.id = :id order by user.id, customTable.id")
    List<Subscription> findByUserId(@Param("id") String userId);

}
