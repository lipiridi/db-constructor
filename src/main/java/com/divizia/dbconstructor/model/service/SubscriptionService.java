package com.divizia.dbconstructor.model.service;

import com.divizia.dbconstructor.model.compositekeys.RequisiteId;
import com.divizia.dbconstructor.model.compositekeys.SubscriptionId;
import com.divizia.dbconstructor.model.entity.Requisite;
import com.divizia.dbconstructor.model.entity.Subscription;

import java.util.List;
import java.util.Optional;

public interface SubscriptionService {

    Subscription saveAndFlush(Subscription subscription);

    void deleteById(SubscriptionId subscriptionId);

    Optional<Subscription> findById(SubscriptionId subscriptionId);

    List<Subscription> findByCustomTableId(String customTableId);

    List<Subscription> findByUserId(String userId);

    List<Subscription> findAll();

}
