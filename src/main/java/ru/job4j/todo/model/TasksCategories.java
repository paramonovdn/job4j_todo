package ru.job4j.todo.model;


import lombok.*;

import javax.persistence.*;
import java.util.Map;

@Entity
@Table(name = "tasks_categories")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TasksCategories {

    public static final Map<String, String> COLUMN_MAPPING = Map.of(
            "id", "id",
            "task_id", "taskId",
            "category_id", "categoryId"
    );
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Getter
    @Setter
    private int id;
    @Getter
    @Setter
    private int taskId;
    @Getter
    @Setter
    private int categoryId;
}
