package ru.job4j.todo.utility;

import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.TaskService;
import ru.job4j.todo.service.UserService;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class TimeZoneConverter {

    public static List<Task> convert(List<Task> tasks, User user) {
        var timeZoneId = user.getTimezone();
        if (timeZoneId == null) {
            timeZoneId = TimeZone.getDefault().getID();
        }
        ArrayList<Task> result = new ArrayList<>();
        for (Task task : tasks) {
            /** Преобразуем Timestamp в ZonedDateTime */
            ZonedDateTime utcZonedDateTime = task.getCreated().toInstant()
                    .atZone(ZoneId.of("UTC"));
            System.out.println("utcZonedDateTime = " + utcZonedDateTime);

            /** Меняем часовой пояс на пояс пользователя*/
            ZonedDateTime moscowZonedDateTime = utcZonedDateTime.withZoneSameInstant(ZoneId.of(timeZoneId));
            System.out.println("moscowZonedDateTime = " + moscowZonedDateTime);

            /** Форматируем вывод в нужный формат */
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = moscowZonedDateTime.format(formatter);
            System.out.println("moscowZonedDateTime = " + formattedDateTime);
            task.setCreated(Timestamp.valueOf(formattedDateTime));
            System.out.println("task = " + task.toString());
            result.add(task);
        }
        return result;
    }
}
