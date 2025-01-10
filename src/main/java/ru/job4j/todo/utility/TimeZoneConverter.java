package ru.job4j.todo.utility;

import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class TimeZoneConverter {

    public static List<Task> convert(List<Task> tasks, User user) {
        ArrayList<Task> result = new ArrayList<>();
        for (Task task : tasks) {
            result.add(convert(task, user));
        }
        return result;
    }

    public static Task convert(Task task, User user) {
        var userTimezone = user.getTimezone();
        if (userTimezone == null) {
            userTimezone = TimeZone.getDefault().getID();
        }
        var timestamp = task.getCreated();
        var localDateTime = timestamp.toLocalDateTime();
        var zonedDateTime = localDateTime.atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of(userTimezone));
        var taskTimeStamp = Timestamp.valueOf(zonedDateTime.toLocalDateTime());
        task.setCreated(taskTimeStamp);
        return task;
    }
}
