package com.divizia.dbconstructor.model.entity;

import com.divizia.dbconstructor.model.Updatable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "a_custom_tables")
public class CustomTable implements Serializable, Updatable<CustomTable> {

    @Id
    @Pattern(regexp = "^\\w+$", message = "The table id can contain only word character [a-zA-Z0-9_]")
    private String id;

    @NotBlank(message = "Name can't be blank")
    private String name;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @OneToMany(mappedBy = "customTable", orphanRemoval = true)
    @ToString.Exclude
    @JsonIgnore
    private List<Requisite> requisites;

    @OneToMany(mappedBy = "customTable", orphanRemoval = true)
    @ToString.Exclude
    @JsonIgnore
    private List<Subscription> subscriptions;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CustomTable that = (CustomTable) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public CustomTable updateAllowed(CustomTable other) {
        if (!id.equals(other.id))
            return this;

        if (other.name != null)
            name = other.name;
        if (other.author != null)
            author = other.author;

        return this;
    }
}
