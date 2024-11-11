package ru.job4j.todo.model;

import lombok.*;

import javax.persistence.*;


@Entity
@Table(name = "categories")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Getter
    @Setter
    private int id;

    @Getter
    @Setter
    private String name;
}
