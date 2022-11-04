package com.divizia.dbconstructor.model.service.impl;

import com.divizia.dbconstructor.model.entity.Subscription;
import com.divizia.dbconstructor.model.repo.SubscriptionRepository;
import com.divizia.dbconstructor.model.service.SubscriptionService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void deleteById(Long id) {
        subscriptionRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void deleteByUserIdAndCustomTableId(String userId, String customTableId) {
        subscriptionRepository.deleteByUserIdAndCustomTableId(userId, customTableId);
    }

    @Override
    public Optional<Subscription> findById(Long id) {
        return subscriptionRepository.findById(id);
    }

    @Override
    public Optional<Subscription> findByUserIdAndCustomTableId(String userId, String customTableId) {
        return subscriptionRepository.findByUserIdAndCustomTableId(userId, customTableId);
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
        return subscriptionRepository.findAll(Sort.by("user").and(Sort.by("customTable")));
    }

}
