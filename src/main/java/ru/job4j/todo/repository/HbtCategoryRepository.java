package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Repository
@AllArgsConstructor
public class HbtCategoryRepository implements CategoryStore {
    private final CrudRepository crudRepository;

    private static final Logger LOG = LoggerFactory.getLogger(HbtCategoryRepository.class.getName());

    @Override
    public Optional<Category> findById(int id) {
        try {
            return crudRepository.optional(
                    "FROM Category WHERE id = :fId", Category.class,
                    Map.of("fId", id)
            );
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Category> findAll() {
        try {
            return crudRepository.query("FROM Category", Category.class);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return new ArrayList<>();
    }

    @Override
    public List<Category> findAllById(List<Integer> categoriesId) {
        try {
            return crudRepository.query(
                    "FROM Category c WHERE c.id IN :cId", Category.class,
                    Map.of("cId", categoriesId)
            );
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return new ArrayList<>();
    }
}
