package ru.job4j.todo.repository;

import ru.job4j.todo.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskStore {
    Optional<Task> save(Task task);

    boolean deleteById(int id);

    boolean update(Task task);

    Optional<Task> findById(int id);

    List<Task> findAll();

    List<Task> findAllDone();

    List<Task> findNewTasks();
    boolean setDoneTrue(int id);
}
