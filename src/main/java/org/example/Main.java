package org.example;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Error: Too few arguments.");
            return;
        }

        try {
            TaskManager taskManager = new TaskManager();
            switch(args[0]) {
                case "add" -> taskManager.addTask(args[1]);
                case "update" -> taskManager.updateTask(Integer.parseInt(args[1]), args[2]);
                case "delete" -> taskManager.deleteTask(Integer.parseInt(args[1]));
                case "mark-in-progress" -> taskManager.markTaskAs("in-progress",  Integer.parseInt(args[1]));
                case "mark-done" ->  taskManager.markTaskAs("done",  Integer.parseInt(args[1]));
                case "list" -> listWithFilters(taskManager, args);
                case "--version" -> System.out.println("0.0.1");

            }

            taskManager.saveTasksToFile();
        }
        catch (Exception e) {
            System.out.println("Error: ");
            e.printStackTrace();
        }
    }

    public static void listWithFilters(TaskManager tm, String[] args) {
        tm.printList(args.length == 1 ? "" : args[1]);
    }

}
