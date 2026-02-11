package org.example;

import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

class TaskManagerTest {

    private static final String TEST_JSON_FILE = "tasks.json";
    private TaskManager taskManager;

    @BeforeEach
    void setUp() throws IOException {
        deleteTestFile();
        taskManager = new TaskManager();
    }

    @AfterEach
    void tearDown() {
        deleteTestFile();
    }

    private void deleteTestFile() {
        File file = new File(TEST_JSON_FILE);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    void addTask_createsTaskWithCorrectDescription() {
        taskManager.addTask("Test task");

        assertEquals(1, taskManager.taskList.size());
        assertEquals("Test task", taskManager.taskList.get(0).getDescription());
    }

    @Test
    void addTask_assignsIncrementingIds() {
        taskManager.addTask("First task");
        taskManager.addTask("Second task");
        taskManager.addTask("Third task");

        assertEquals(1, taskManager.taskList.get(0).getId());
        assertEquals(2, taskManager.taskList.get(1).getId());
        assertEquals(3, taskManager.taskList.get(2).getId());
    }

    @Test
    void addTask_setsInitialStatusToTodo() {
        taskManager.addTask("Test task");

        assertEquals("todo", taskManager.taskList.get(0).getStatus());
    }

    @Test
    void updateTask_changesDescription() {
        taskManager.addTask("Original description");

        taskManager.updateTask(1, "Updated description");

        assertEquals("Updated description", taskManager.taskList.get(0).getDescription());
    }

    @Test
    void updateTask_withNonExistentId_doesNothing() {
        taskManager.addTask("Test task");

        taskManager.updateTask(999, "Should not apply");

        assertEquals("Test task", taskManager.taskList.get(0).getDescription());
    }

    @Test
    void deleteTask_removesTaskFromList() {
        taskManager.addTask("Task to delete");
        taskManager.addTask("Task to keep");

        taskManager.deleteTask(1);

        assertEquals(1, taskManager.taskList.size());
        assertEquals("Task to keep", taskManager.taskList.get(0).getDescription());
    }

    @Test
    void deleteTask_withNonExistentId_doesNothing() {
        taskManager.addTask("Test task");

        taskManager.deleteTask(999);

        assertEquals(1, taskManager.taskList.size());
    }

    @Test
    void markTaskAs_changesStatusToInProgress() {
        taskManager.addTask("Test task");

        taskManager.markTaskAs("in-progress", 1);

        assertEquals("in-progress", taskManager.taskList.get(0).getStatus());
    }

    @Test
    void markTaskAs_changesStatusToDone() {
        taskManager.addTask("Test task");

        taskManager.markTaskAs("done", 1);

        assertEquals("done", taskManager.taskList.get(0).getStatus());
    }

    @Test
    void markTaskAs_changesStatusBackToTodo() {
        taskManager.addTask("Test task");
        taskManager.markTaskAs("done", 1);

        taskManager.markTaskAs("todo", 1);

        assertEquals("todo", taskManager.taskList.get(0).getStatus());
    }

    @Test
    void markTaskAs_withNonExistentId_doesNothing() {
        taskManager.addTask("Test task");

        taskManager.markTaskAs("done", 999);

        assertEquals("todo", taskManager.taskList.get(0).getStatus());
    }

    @Test
    void saveTasksToFile_createsJsonFile() throws IOException {
        taskManager.addTask("Test task");

        taskManager.saveTasksToFile();

        File file = new File(TEST_JSON_FILE);
        assertTrue(file.exists());
        String content = Files.readString(file.toPath());
        assertTrue(content.contains("Test task"));
    }

    @Test
    void taskManager_loadsExistingTasksFromFile() throws IOException {
        taskManager.addTask("Persisted task");
        taskManager.saveTasksToFile();

        TaskManager newManager = new TaskManager();

        assertEquals(1, newManager.taskList.size());
        assertEquals("Persisted task", newManager.taskList.get(0).getDescription());
    }

    @Test
    void taskManager_continuesIdSequenceAfterReload() throws IOException {
        taskManager.addTask("First task");
        taskManager.addTask("Second task");
        taskManager.saveTasksToFile();

        TaskManager newManager = new TaskManager();
        newManager.addTask("Third task");

        assertEquals(3, newManager.taskList.get(2).getId());
    }

    @Test
    void printList_withNoFilter_printsAllTasks() {
        taskManager.addTask("Task one");
        taskManager.addTask("Task two");
        taskManager.markTaskAs("done", 2);

        ByteArrayOutputStream output = captureOutput();
        taskManager.printList("");
        String printed = output.toString();

        assertTrue(printed.contains("Task one"));
        assertTrue(printed.contains("Task two"));
    }

    @Test
    void printList_withTodoFilter_printsOnlyTodoTasks() {
        taskManager.addTask("Todo task");
        taskManager.addTask("Done task");
        taskManager.markTaskAs("done", 2);

        ByteArrayOutputStream output = captureOutput();
        taskManager.printList("todo");
        String printed = output.toString();

        assertTrue(printed.contains("Todo task"));
        assertFalse(printed.contains("Done task"));
    }

    @Test
    void printList_withDoneFilter_printsOnlyDoneTasks() {
        taskManager.addTask("Todo task");
        taskManager.addTask("Done task");
        taskManager.markTaskAs("done", 2);

        ByteArrayOutputStream output = captureOutput();
        taskManager.printList("done");
        String printed = output.toString();

        assertFalse(printed.contains("Todo task"));
        assertTrue(printed.contains("Done task"));
    }

    @Test
    void printList_withInProgressFilter_printsOnlyInProgressTasks() {
        taskManager.addTask("Todo task");
        taskManager.addTask("In progress task");
        taskManager.markTaskAs("in-progress", 2);

        ByteArrayOutputStream output = captureOutput();
        taskManager.printList("in-progress");
        String printed = output.toString();

        assertFalse(printed.contains("Todo task"));
        assertTrue(printed.contains("In progress task"));
    }

    private ByteArrayOutputStream captureOutput() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));
        return baos;
    }
}
