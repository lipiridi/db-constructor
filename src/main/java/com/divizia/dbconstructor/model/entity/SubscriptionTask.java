package com.divizia.dbconstructor.model.entity;

import com.divizia.dbconstructor.model.Updatable;
import com.divizia.dbconstructor.model.compositekeys.SubscriptionTaskId;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "a_subscription_tasks")
@IdClass(SubscriptionTaskId.class)
public class SubscriptionTask implements Updatable<SubscriptionTask> {

    @Id
    @ManyToOne
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;

    @Id
    @Column(name = "record_id")
    private Long recordId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SubscriptionTask that = (SubscriptionTask) o;
        return subscription != null && Objects.equals(subscription, that.subscription)
                && recordId != null && Objects.equals(recordId, that.recordId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subscription, recordId);
    }

    @Override
    public SubscriptionTask updateAllowed(SubscriptionTask other) {
        return this;
    }

}
