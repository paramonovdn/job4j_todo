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
        model.addAttribute("task", taskOptional.get());
        return "tasks/one";
    }

    @GetMapping("/delete/{id}")
    public String delete(Model model, @PathVariable int id) {
        taskService.deleteById(id);
        return "redirect:/";
    }

    @GetMapping("/donebutton/{id}")
    public String getIsDone(Model model, @PathVariable int id) {
        var task = taskService.findById(id).get();
        task.setDone(true);
        taskService.update(task);
        var doneTask = taskService.findById(id).get();
        model.addAttribute("task", doneTask);
        return "tasks/one";
    }

    @GetMapping("/update/{id}")
    public String getUpdatePage(Model model, @PathVariable int id) {
        var task = taskService.findById(id).get();
        model.addAttribute("task", task);
        return "tasks/update";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Task task, Model model) {
        taskService.update(task);
        var updateTask = taskService.findById(task.getId()).get();
        model.addAttribute("task", updateTask);
        return "/tasks/one";
    }
}
