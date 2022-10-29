package com.divizia.dbconstructor.model.repo;

import com.divizia.dbconstructor.model.compositekeys.RequisiteId;
import com.divizia.dbconstructor.model.compositekeys.SubscriptionTaskId;
import com.divizia.dbconstructor.model.entity.Requisite;
import com.divizia.dbconstructor.model.entity.SubscriptionTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubscriptionTaskRepository extends JpaRepository<SubscriptionTask, SubscriptionTaskId> {

}
