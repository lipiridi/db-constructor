package com.divizia.dbconstructor.model.service;

import com.divizia.dbconstructor.model.entity.Subscription;

import java.util.List;
import java.util.Optional;

public interface SubscriptionService {

    Subscription saveAndFlush(Subscription subscription);

    void deleteById(Long subscriptionId);

    void deleteByUserIdAndCustomTableId(String userId, String customTableId);

    Optional<Subscription> findById(Long subscriptionId);

    Optional<Subscription> findByUserIdAndCustomTableId(String userId, String customTableId);

    List<Subscription> findByCustomTableId(String customTableId);

    List<Subscription> findByUserId(String userId);

    List<Subscription> findAll();

}
