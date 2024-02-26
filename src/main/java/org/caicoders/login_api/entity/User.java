package org.caicoders.login_api.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@Table
@Data
public class User implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 30, nullable = false, unique = true)
    private String name;

    @Column(length = 30, nullable = false, unique = true)
    private String email;

    @Column(length = 30, nullable = false)
    private String password;

    public User() {
    }

    public User(String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    @Override
    public String toString() {
        return """
                User [id=%d, name=%s, email=%s, password=%s]
                """.formatted(id, name, email, password);
    }

    public boolean overlaps(User user) {
        return this.name.equals(user.name) && this.email.equals(user.email);
    }
}
