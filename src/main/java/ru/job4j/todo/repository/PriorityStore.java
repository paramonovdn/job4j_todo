package ru.job4j.todo.repository;

import ru.job4j.todo.model.Priority;
import ru.job4j.todo.model.Task;

import java.util.List;
import java.util.Optional;

public interface PriorityStore {

    Optional<Priority> findById(int id);
    List<Priority> findAll();
}
