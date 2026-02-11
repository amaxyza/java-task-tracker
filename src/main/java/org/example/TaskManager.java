package org.example;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

public class TaskManager {
    Gson gson;
    File jsonFile;
    ArrayList<Task> taskList;

    int highestID;

    TaskManager() throws IOException {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class,
                        (JsonSerializer<LocalDateTime>) (src, type, context) ->
                                new JsonPrimitive(src.toString()))
                .registerTypeAdapter(LocalDateTime.class,
                        (JsonDeserializer<LocalDateTime>) (json, type, context) ->
                                LocalDateTime.parse(json.getAsString()))
                .create();
        checkJSONFile();
        highestID = initializeUIDCounter();
    }

    private void checkJSONFile() throws IOException {
        jsonFile = new File("tasks.json");
        if (!jsonFile.exists()) {
            jsonFile.createNewFile();
            taskList = new ArrayList<>();
        }
        else {
            assert gson != null;
            String jsonText = Files.readString(jsonFile.toPath());
            taskList = parseTasks(jsonText);
        }
    }

    private ArrayList<Task> parseTasks(String json) {
        if (json.isEmpty()) {
            return new ArrayList<>();
        }
        Type type = new TypeToken<ArrayList<Task>>() {}.getType();
        return gson.fromJson(json, type);
    }

    private int initializeUIDCounter() {
        int lastid = 0;
        for (var task : taskList) lastid = Math.max(lastid, task.getId());
        return  lastid;
    }
    private int nextID() {
        return ++highestID;
    }

    public void addTask(String description) {
        int id = nextID();
        taskList.add(new Task(id, description));
        IO.println("Task " + id + " added to tracker.");
    }

    public void updateTask(int _id, String _description) {
        taskList.stream()
                .filter(task -> task.getId() == _id)
                .findFirst()
                .ifPresent(task -> {
                    task.setDescription(_description);
                    IO.println("Task " + _id + " updated.");
                });
    }

    public void deleteTask(int _id) {
        boolean removed = taskList.removeIf(task -> task.getId() == _id);
        if (removed) {
            IO.println("Task " + _id + " deleted.");
        }
    }

    public void markTaskAs(String status, int _id) {
        taskList.stream()
                .filter(task -> task.getId() == _id)
                .findFirst()
                .ifPresent(task -> {
                    task.setStatus(status);
                    IO.println("Task " + _id + " marked as " + status + ".");
                });
    }

    public void saveTasksToFile() throws IOException {
        Files.writeString(jsonFile.toPath(), gson.toJson(taskList));
    }

    public void printList(String filter) {
        var filtered = switch (filter) {
            case "done", "todo", "in-progress" -> taskList.stream()
                    .filter(task -> task.getStatus().equals(filter))
                    .toList();
            default -> taskList;
        };

        if (filtered.isEmpty()) {
            IO.println("No tasks found.");
            return;
        }

        IO.println(String.format("%-5s | %-12s | %s", "ID", "Status", "Description"));
        IO.println("-".repeat(50));
        filtered.forEach(task -> IO.println(String.format("%-5d | %-12s | %s",
                task.getId(), task.getStatus(), task.getDescription())));
    }
}
