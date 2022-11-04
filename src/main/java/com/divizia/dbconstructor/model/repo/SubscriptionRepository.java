package com.divizia.dbconstructor.model.repo;

import com.divizia.dbconstructor.model.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    List<Subscription> findByCustomTableId(String customTableId);

    List<Subscription> findByUserId(String userId);

    void deleteByUserIdAndCustomTableId(String userId, String customTableId);

    Optional<Subscription> findByUserIdAndCustomTableId(String userId, String customTableId);

}
