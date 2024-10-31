package ru.job4j.todo.controller;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Priority;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.PriorityService;
import ru.job4j.todo.service.TaskService;

import javax.servlet.http.HttpSession;


@AllArgsConstructor
@RequestMapping("/tasks")
@Controller
public class TaskController {
    private final TaskService taskService;
    private final PriorityService priorityService;

    @GetMapping("/create")
    public String getCreationPage(Model model) {
        model.addAttribute("priorities", priorityService.findAll());
        return "tasks/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Task task, HttpSession session, int priorityId) {
        var priority = priorityService.findById(priorityId).get();
        var user = (User) session.getAttribute("user");
        task.setPriority(priority);
        task.setUser(user);
        taskService.save(task);
        return "redirect:/";
    }

    @GetMapping("/done")
    public String getDoneTasks(Model model) {
        var result = taskService.findAllDone();
        model.addAttribute("tasks", result);
        return "tasks/list";
    }

    @GetMapping("/new")
    public String getNewTasks(Model model) {
        var result = taskService.findNewTasks();
        model.addAttribute("tasks", result);
        return "tasks/list";
    }

    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable int id) {
        var taskOptional = taskService.findById(id);
        if (taskOptional.isEmpty()) {
            model.addAttribute("message", "Задание с указанным идентификатором не найдено!");
            return "errors/404";
        }
        model.addAttribute("task", taskOptional.get());
        return "tasks/one";
    }

    @GetMapping("/delete/{id}")
    public String delete(Model model, @PathVariable int id) {
        var isDeleted = taskService.deleteById(id);
        if (!isDeleted) {
            model.addAttribute("message", "Не удалось удалить задание с указанным идентификатором!");
            return "errors/404";
        }
        return "redirect:/";
    }

    @GetMapping("/donebutton/{id}")
    public String getIsDone(Model model, @PathVariable int id) {
        var isDone = taskService.setDoneTrue(id);
        if (!isDone) {
            model.addAttribute("message", "Не удалось изменить статус задания на \"Выполнено\"!");
            return "errors/404";
        }
        return "redirect:/tasks/{id}";
    }

    @GetMapping("/update/{id}")
    public String getUpdatePage(Model model, @PathVariable int id) {
        var task = taskService.findById(id);
        if (task.isEmpty()) {
            model.addAttribute("message", "Задание с указанным идентификатором не найдено!");
            return "errors/404";
        }
        model.addAttribute("task", task.get());
        return "tasks/update";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Task task, Model model) {
        var isUpdated = taskService.update(task);
        if (!isUpdated) {
            model.addAttribute("message", "Не удалось обновить задание!");
            return "errors/404";
        }
        var updatedTask = taskService.findById(task.getId());
        model.addAttribute("task", updatedTask.get());
        return "tasks/one";
    }
}
