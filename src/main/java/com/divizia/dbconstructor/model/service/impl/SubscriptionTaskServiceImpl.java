package com.divizia.dbconstructor.model.service.impl;

import com.divizia.dbconstructor.model.compositekeys.SubscriptionTaskId;
import com.divizia.dbconstructor.model.entity.SubscriptionTask;
import com.divizia.dbconstructor.model.repo.SubscriptionTaskRepository;
import com.divizia.dbconstructor.model.service.SubscriptionTaskService;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class SubscriptionTaskServiceImpl implements SubscriptionTaskService {

    private final SubscriptionTaskRepository subscriptionTaskRepository;

    @Override
    public SubscriptionTask saveAndFlush(SubscriptionTask subscriptionTask) {
        return subscriptionTaskRepository.saveAndFlush(subscriptionTask);
    }

    @Override
    public void deleteById(SubscriptionTaskId subscriptionTaskId) {
        subscriptionTaskRepository.deleteById(subscriptionTaskId);
    }

    @Override
    public Optional<SubscriptionTask> findById(SubscriptionTaskId subscriptionTaskId) {
        return subscriptionTaskRepository.findById(subscriptionTaskId);
    }

    @Override
    public List<SubscriptionTask> findAll() {
        return subscriptionTaskRepository.findAll();
    }

}
