package com.divizia.dbconstructor.model.service;

import com.divizia.dbconstructor.model.compositekeys.SubscriptionTaskId;
import com.divizia.dbconstructor.model.entity.SubscriptionTask;

import java.util.List;
import java.util.Optional;

public interface SubscriptionTaskService {

    SubscriptionTask saveAndFlush(SubscriptionTask subscriptionTask);

    void deleteById(SubscriptionTaskId subscriptionTaskId);

    void deleteAll(Iterable<? extends SubscriptionTask> subscriptionTasks);

    Optional<SubscriptionTask> findById(SubscriptionTaskId subscriptionTaskId);

    List<SubscriptionTask> findAll();

    void subscribe(String customTableId, Long recordId);

    void unsubscribe(String customTableId, Long recordId);

}
