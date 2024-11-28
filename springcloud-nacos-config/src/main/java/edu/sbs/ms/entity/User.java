package edu.sbs.ms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

/**
 * Stores information to identify the user.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue
    private Long id;
    private String alias;

    public User(final String userAlias) {
        this(null, userAlias);
    }
}
