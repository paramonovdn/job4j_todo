package ru.job4j.todo.repository;

import ru.job4j.todo.model.Category;
import ru.job4j.todo.model.Priority;

import java.util.List;
import java.util.Optional;

public interface CategoryStore {
    Optional<Category> findById(int id);

    List<Category> findAll();

    List<Category> findAllById(List<Integer> categoriesId);
}
