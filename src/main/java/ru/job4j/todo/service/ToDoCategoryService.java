package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.model.Priority;
import ru.job4j.todo.repository.CategoryStore;
import ru.job4j.todo.repository.PriorityStore;

import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class ToDoCategoryService implements CategoryService {
    private final CategoryStore categoryStore;

    @Override
    public Optional<Category> findById(int id) {
        return categoryStore.findById(id);
    }

    @Override
    public List<Category> findAll() {
        return categoryStore.findAll();
    }

    @Override
    public List<Category> findAllById(List<Integer> categoriesId) {
        return categoryStore.findAllById(categoriesId);
    }

}
