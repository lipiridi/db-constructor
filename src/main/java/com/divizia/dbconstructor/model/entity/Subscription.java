package com.divizia.dbconstructor.model.entity;

import com.divizia.dbconstructor.model.Updatable;
import com.divizia.dbconstructor.model.compositekeys.SubscriptionId;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "a_subscriptions")
@IdClass(SubscriptionId.class)
public class Subscription implements Updatable<Subscription> {

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "custom_table_id")
    private CustomTable customTable;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Subscription that = (Subscription) o;
        return user != null && Objects.equals(user, that.user)
                && customTable != null && Objects.equals(customTable, that.customTable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, customTable);
    }

    @Override
    public Subscription updateAllowed(Subscription other) {
        return this;
    }

}
