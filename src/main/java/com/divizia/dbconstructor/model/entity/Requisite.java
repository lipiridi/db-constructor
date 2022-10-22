package com.divizia.dbconstructor.model.entity;

import com.divizia.dbconstructor.model.Updatable;
import com.divizia.dbconstructor.model.compositekeys.RequisiteId;
import com.divizia.dbconstructor.model.enums.RequisiteType;
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
@Table(name = "a_requisites")
@IdClass(RequisiteId.class)
public class Requisite implements Updatable<Requisite> {

    @Id
    @Pattern(regexp = "^\\w+$", message = "The requisite id can contain only word character [a-zA-Z0-9_]")
    private String id;

    @Id
    @ManyToOne
    @JoinColumn(name = "custom_table_id")
    private CustomTable customTable;

    @NotBlank(message = "Name can't be blank")
    private String name;

    @Enumerated(EnumType.STRING)
    private RequisiteType type;

    @Column(name = "foreign_table_id")
    private String foreignTableId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Requisite requisite = (Requisite) o;
        return id != null && Objects.equals(id, requisite.id)
                && customTable != null && Objects.equals(customTable, requisite.customTable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customTable);
    }

    @Override
    public Requisite updateAllowed(Requisite other) {
        if (!id.equals(other.id))
            return this;

        if (other.name != null && !other.name.isBlank())
            name = other.name;
        if (other.foreignTableId != null)
            foreignTableId = other.foreignTableId;

        return this;
    }
}
