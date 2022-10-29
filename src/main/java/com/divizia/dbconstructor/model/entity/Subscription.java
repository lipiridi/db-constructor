package com.divizia.dbconstructor.model.entity;

import com.divizia.dbconstructor.model.Updatable;
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
@Table(name = "a_subscriptions", uniqueConstraints={@UniqueConstraint(columnNames = {"user_id", "custom_table_id"})})
public class Subscription implements Updatable<Subscription> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "custom_table_id")
    private CustomTable customTable;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Subscription that = (Subscription) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public Subscription updateAllowed(Subscription other) {
        if (!id.equals(other.id))
            return this;

        if (other.user != null)
            user = other.user;
        if (other.customTable != null)
            customTable = other.customTable;

        return this;
    }

}
