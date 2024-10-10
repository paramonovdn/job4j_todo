package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import ru.job4j.todo.model.Task;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import ru.job4j.todo.repository.Store;

@Service
@AllArgsConstructor
public class TaskService implements ServiceToDo {

    private final Store taskStore;
    @Override
    public Optional<Task> save(Task task) {
        return taskStore.save(task);
    }

    @Override
    public boolean deleteById(int id) {
        return taskStore.deleteById(id);
    }

    @Override
    public boolean update(Task task) {
        return taskStore.update(task);
    }

    @Override
    public Optional<Task> findById(int id) {
        return taskStore.findById(id);
    }

    @Override
    public List<Task> findAll() {
        return taskStore.findAll();
    }
}
