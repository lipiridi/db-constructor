package com.divizia.dbconstructor.model.service.impl;

import com.divizia.dbconstructor.model.compositekeys.SubscriptionId;
import com.divizia.dbconstructor.model.entity.Subscription;
import com.divizia.dbconstructor.model.repo.RequisiteRepository;
import com.divizia.dbconstructor.model.repo.SubscriptionRepository;
import com.divizia.dbconstructor.model.service.SubscriptionService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    @Override
    public Subscription saveAndFlush(Subscription subscription) {
        return subscriptionRepository.saveAndFlush(subscription);
    }

    @Override
    public void deleteById(SubscriptionId subscriptionId) {
        subscriptionRepository.deleteById(subscriptionId);
    }

    @Override
    public Optional<Subscription> findById(SubscriptionId subscriptionId) {
        return subscriptionRepository.findById(subscriptionId);
    }

    @Override
    public List<Subscription> findByCustomTableId(String customTableId) {
        return subscriptionRepository.findByCustomTableId(customTableId);
    }

    @Override
    public List<Subscription> findByUserId(String userId) {
        return subscriptionRepository.findByUserId(userId);
    }

    @Override
    public List<Subscription> findAll() {
        return subscriptionRepository.findAll(Sort.by("user, customTable"));
    }

}
