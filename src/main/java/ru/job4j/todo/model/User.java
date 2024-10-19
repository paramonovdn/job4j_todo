package ru.job4j.todo.model;


import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "todo_user")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class User {
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
}
