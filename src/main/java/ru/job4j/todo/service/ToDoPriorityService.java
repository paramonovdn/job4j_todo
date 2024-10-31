package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Priority;
import ru.job4j.todo.repository.PriorityStore;

import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class ToDoPriorityService implements PriorityService {

    private final PriorityStore priorityStore;

    public Optional<Priority> findById(int id) {
        return priorityStore.findById(id);
    }
    @Override
    public List<Priority> findAll() {
        return priorityStore.findAll();
    }
}
