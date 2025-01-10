package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.CategoryService;
import ru.job4j.todo.service.PriorityService;
import ru.job4j.todo.service.TaskService;
import ru.job4j.todo.utility.TimeZoneConverter;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;



@AllArgsConstructor
@RequestMapping("/tasks")
@Controller
public class TaskController {
    private final TaskService taskService;
    private final PriorityService priorityService;

    private final CategoryService categoryService;

    @GetMapping("/create")
    public String getCreationPage(Model model) {
        model.addAttribute("priorities", priorityService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        return "tasks/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Task task, HttpSession session, @RequestParam List<Integer> categoriesId) {
        var currentTaskCategories = categoryService.findAllById(categoriesId);
        task.setCategories(currentTaskCategories);
        var user = (User) session.getAttribute("user");
        task.setUser(user);
        taskService.save(task);
        return "redirect:/";
    }

    @GetMapping("/done")
    public String getDoneTasks(Model model, HttpSession session) {
        ArrayList<Task> doneTasks = new ArrayList<>();
        var allTasks = taskService.findAll();
        var user = (User) session.getAttribute("user");
        for (Task task : allTasks) {
            if (task.isDone()) {
                doneTasks.add(task);
            }
        }
        var result = TimeZoneConverter.convert(doneTasks, user);
        model.addAttribute("tasks", result);
        return "tasks/list";
    }

    @GetMapping("/new")
    public String getNewTasks(Model model, HttpSession session) {
        ArrayList<Task> result = new ArrayList<>();
        var allTasks = taskService.findAll();
        var user = (User) session.getAttribute("user");

        /** Поиск крайней точки для обозначения свежей задачи(UTC минус 24 часа*/
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now(ZoneId.of("UTC")));
        /** Преобразуем в ZonedDateTime*/
        ZonedDateTime zdt = timestamp.toInstant().atZone(ZoneId.systemDefault());
        /** Вычитаем 24 часа*/
        ZonedDateTime newZdt = zdt.minusHours(24);
        /** Преобразуем обратно в Timestamp. Получаем абсолютное время минус 24 часа*/
        Timestamp newTimestamp = Timestamp.valueOf(newZdt.toLocalDateTime());

        for (Task task : allTasks) {
            if (task.getCreated().after(newTimestamp)) {
                result.add(task);
            }
        }
        var tasksWithNewTime = TimeZoneConverter.convert(result, user);
        model.addAttribute("tasks", tasksWithNewTime);
        return "tasks/list";
    }

    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable int id, HttpSession session) {
        var taskOptional = taskService.findById(id);
        if (taskOptional.isEmpty()) {
            model.addAttribute("message", "Задание с указанным идентификатором не найдено!");
            return "errors/404";
        }
        var user = (User) session.getAttribute("user");
        var task = TimeZoneConverter.convert(taskOptional.get(), user);
        model.addAttribute("task", task);
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
    public String update(@ModelAttribute Task task, Model model, @RequestParam List<Integer> categoriesId) {
        var categories = categoryService.findAllById(categoriesId);
        task.setCategories(categories);
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
