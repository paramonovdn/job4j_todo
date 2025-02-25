package ru.job4j.todo.service;

import ru.job4j.todo.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    Optional<Category> findById(int id);

    List<Category> findAll();

    List<Category> findAllById(List<Integer> categoriesId);
}
