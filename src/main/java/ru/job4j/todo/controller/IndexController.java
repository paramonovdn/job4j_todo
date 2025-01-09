package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.TaskService;
import ru.job4j.todo.utility.TimeZoneConverter;

import javax.servlet.http.HttpSession;


@AllArgsConstructor
@Controller
public class IndexController {
    private final TaskService taskService;

    @GetMapping({"/", "/index"})
    public String getAll(Model model, HttpSession session) {
        var allTasks = taskService.findAll();
        var user = (User) session.getAttribute("user");
        var result = TimeZoneConverter.convert(allTasks, user);
        model.addAttribute("tasks", result);
        return "tasks/list";
    }

}