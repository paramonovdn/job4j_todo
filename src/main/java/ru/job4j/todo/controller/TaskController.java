package ru.job4j.todo.controller;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.service.TaskService;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;


@AllArgsConstructor
@RequestMapping("/tasks")
@Controller
public class TaskController {
    private final TaskService taskService;

    @GetMapping("/create")
    public String getCreationPage() {
        return "tasks/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Task task) {
        taskService.save(task);
        return "redirect:/";
    }

    @GetMapping("/done")
    public String getDoneTasks(Model model) {
        var result = new ArrayList<>();
        var allTasks = taskService.findAll();
        for (Task task : allTasks) {
            if (task.isDone()) {
                result.add(task);
            }
        }
        model.addAttribute("tasks", result);
        return "tasks/list";
    }

    @GetMapping("/new")
    public String getNewTasks(Model model) {
        var now = Timestamp.valueOf(LocalDateTime.now()).getTime();
        var result = new ArrayList<>();
        var allTasks = taskService.findAll();
        for (Task task : allTasks) {
            if (now - task.getCreated().getTime() <= 86400000) {
                result.add(task);
            }
        }
        model.addAttribute("tasks", result);
        return "tasks/list";
    }


    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable int id) {
        var taskOptional = taskService.findById(id);
        if (taskOptional.isEmpty()) {
            model.addAttribute("message", "Задача с указанным id не найдена.");
            return "errors/404";
        }
        model.addAttribute("task", taskOptional.get());
        return "tasks/one";
    }

   /* @GetMapping("/update")
    public String update(@ModelAttribute Task task, Model model) {
        try {
            var isUpdated = candidateService.update(candidate, new FileDto(file.getOriginalFilename(), file.getBytes()));
            if (!isUpdated) {
                model.addAttribute("message", "Резюме с указанным идентификатором не найдено");
                return "errors/404";
            }
            return "redirect:/candidates";
        } catch (Exception exception) {
            model.addAttribute("message", exception.getMessage());
            return "errors/404";
        }
    }*/
}
