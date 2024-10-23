package ru.job4j.todo.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;


@Entity
@Table(name = "tasks")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Getter
    @Setter
    private int id;
    @Getter
    @Setter
    private String title;
    @Getter
    @Setter
    private String description;
    @Getter
    @Setter
    private Timestamp created = Timestamp.valueOf(LocalDateTime.now());
    @Getter
    @Setter
    private boolean done;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @Getter
    @Setter
    private User user;

}
