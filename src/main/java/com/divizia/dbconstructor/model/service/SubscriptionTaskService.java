package com.divizia.dbconstructor.model.service;

import com.divizia.dbconstructor.model.compositekeys.RequisiteId;
import com.divizia.dbconstructor.model.compositekeys.SubscriptionTaskId;
import com.divizia.dbconstructor.model.entity.Requisite;
import com.divizia.dbconstructor.model.entity.SubscriptionTask;

import java.util.List;
import java.util.Optional;

public interface SubscriptionTaskService {

    SubscriptionTask saveAndFlush(SubscriptionTask subscriptionTask);

    void deleteById(SubscriptionTaskId subscriptionTaskId);

    Optional<SubscriptionTask> findById(SubscriptionTaskId subscriptionTaskId);

    List<SubscriptionTask> findAll();

}
