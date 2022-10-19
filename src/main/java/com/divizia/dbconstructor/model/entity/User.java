package com.divizia.dbconstructor.model.entity;

import com.divizia.dbconstructor.model.Updatable;
import com.divizia.dbconstructor.model.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@Entity
@Table(name = "a_users")
public class User implements Updatable<User> {

    @Id
    @Pattern(regexp = "^\\w+$", message = "Username can contain only word character [a-zA-Z0-9_]")
    private String id;

    @NotBlank(message = "Password can't be blank")
    @ToString.Exclude
    private String password;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Role can't be null")
    private Role role;

    @OneToMany(mappedBy = "author", orphanRemoval = true)
    @ToString.Exclude
    @JsonIgnore
    private Set<CustomTable> customTables;

    public User() {
        this.role = Role.USER;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public void updateAllowed(User other) {
        if (!password.equals(other.getPassword()))
            password = new BCryptPasswordEncoder(12).encode(password);

        customTables = other.getCustomTables();
    }

}
