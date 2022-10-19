package com.divizia.dbconstructor.model.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "custom_tables")
public class CustomTable implements Serializable {

    @Id
    @Pattern(regexp = "^[a-z]*[a-z_]*[a-z]$", message = "The table id must start/end with lowercase letters and all space must be replaced with '_'")
    private String id;

    @NotBlank(message = "Name can't be blank")
    private String name;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @OneToMany(mappedBy = "customTable", orphanRemoval = true)
    @ToString.Exclude
    private Set<Requisite> requisites;

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
}
