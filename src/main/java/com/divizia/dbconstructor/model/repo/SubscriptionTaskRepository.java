package com.divizia.dbconstructor.model.repo;

import com.divizia.dbconstructor.model.compositekeys.SubscriptionTaskId;
import com.divizia.dbconstructor.model.entity.SubscriptionTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SubscriptionTaskRepository extends JpaRepository<SubscriptionTask, SubscriptionTaskId> {

    @Modifying
    @Query("delete from SubscriptionTask where recordId = :recordId and " +
            "subscription in (select s from Subscription s where s.customTable.id = :customTableId)")
    void unsubscribe(@Param("customTableId") String customTableId, @Param("recordId") Long recordId);

}
