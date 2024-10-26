package ru.job4j.todo.model;

import javax.persistence.*;

import lombok.*;



@Entity
@Table(name = "priorities")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Priority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private int position;
}