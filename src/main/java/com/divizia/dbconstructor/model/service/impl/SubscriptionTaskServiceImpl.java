package com.divizia.dbconstructor.model.service.impl;

import com.divizia.dbconstructor.model.compositekeys.SubscriptionTaskId;
import com.divizia.dbconstructor.model.entity.Subscription;
import com.divizia.dbconstructor.model.entity.SubscriptionTask;
import com.divizia.dbconstructor.model.repo.SubscriptionTaskRepository;
import com.divizia.dbconstructor.model.service.SubscriptionService;
import com.divizia.dbconstructor.model.service.SubscriptionTaskService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SubscriptionTaskServiceImpl implements SubscriptionTaskService {

    private final SubscriptionTaskRepository subscriptionTaskRepository;
    private final SubscriptionService subscriptionService;

    @Override
    public SubscriptionTask saveAndFlush(SubscriptionTask subscriptionTask) {
        return subscriptionTaskRepository.saveAndFlush(subscriptionTask);
    }

    @Override
    public void deleteById(SubscriptionTaskId subscriptionTaskId) {
        subscriptionTaskRepository.deleteById(subscriptionTaskId);
    }

    @Override
    public void deleteAll(Iterable<? extends SubscriptionTask> subscriptionTasks) {
        subscriptionTaskRepository.deleteAll(subscriptionTasks);
    }

    @Override
    public Optional<SubscriptionTask> findById(SubscriptionTaskId subscriptionTaskId) {
        return subscriptionTaskRepository.findById(subscriptionTaskId);
    }

    @Override
    public List<SubscriptionTask> findAll() {
        return subscriptionTaskRepository.findAll();
    }

    @Override
    public void subscribe(String customTableId, Long recordId) {
        List<Subscription> subscriptions = subscriptionService.findByCustomTableId(customTableId);

        subscriptionTaskRepository.saveAllAndFlush(
                subscriptions.stream().map(x -> new SubscriptionTask(x, recordId)).toList());
    }

    @Transactional
    @Override
    public void unsubscribe(String customTableId, Long recordId) {
        subscriptionTaskRepository.unsubscribe(customTableId, recordId);
    }

}
