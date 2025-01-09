package ru.job4j.todo.model;


import lombok.*;

import javax.persistence.*;
import java.util.Map;

@Entity
@Table(name = "todo_user")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class User {
    public static final Map<String, String> COLUMN_MAPPING = Map.of(
            "id", "id",
            "name", "name",
            "login", "login",
            "password", "password",
            "user_zone", "timezone"
    );
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Getter
    @Setter
    private int id;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String login;
    @Getter
    @Setter
    private String password;
    @Getter
    @Setter
    @Column(name = "user_zone")
    private String timezone;
}
