package Project.TaskAutomationTool.service;

import Project.TaskAutomationTool.Repository.TaskRepository;
import Project.TaskAutomationTool.model.Task;
import Project.TaskAutomationTool.model.TaskFrequency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
public class TaskRunner implements CommandLineRunner {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;

    @Override
    public void run(String... args) throws Exception {

        Task task1 = new Task("Task 1", "Description 1", LocalDateTime.now().plusSeconds(10), TaskFrequency.DAILY);
        Task task2 = new Task("Task 2", "Description 2", LocalDateTime.now().plusMinutes(20), TaskFrequency.WEEKLY);
        Task task3 = new Task("Task 3", "Description 3", LocalDateTime.now().plusHours(2), TaskFrequency.MONTHLY);

        ExecutorService executorService = Executors.newFixedThreadPool(4);

        // Task 1
        executorService.submit(() -> {
            try {
                if (task1.getScheduledTime().isBefore(LocalDateTime.now())) {
                    System.out.println("Task 1 Scheduled time is in the past, skipping creation");
                } else {
                    taskService.createTask(task1);
                    System.out.println("Thread added: " + task1.getName());
                }
            } catch (Exception e) {
                System.out.println("Error in thread for task 1: " + task1.getName() + " - " + e.getMessage());
            }
        });

        // Task 2
        executorService.submit(() -> {
            try {
                if (task2.getScheduledTime().isBefore(LocalDateTime.now())) {
                    System.out.println("Task 2 Scheduled time is in the past, skipping creation");
                } else {
                    taskService.createTask(task2);
                    System.out.println("Thread added: " + task2.getName());
                }
            } catch (Exception e) {
                System.out.println("Error in thread for task 2: " + task2.getName() + " - " + e.getMessage());
            }
        });

        // Task 3
        executorService.submit(() -> {
            try {
                if (task3.getScheduledTime().isBefore(LocalDateTime.now())) {
                    System.out.println("Task 3 Scheduled time is in the past, skipping creation");
                } else {
                    taskService.createTask(task3);
                    System.out.println("Thread added: " + task3.getName());
                }
            } catch (Exception e) {
                System.out.println("Error in thread for task 3: " + task3.getName() + " - " + e.getMessage());
            }
        });

        // Deleting Task 3
        executorService.submit(() -> {
            try {
                if (taskService.getTaskByName("Task 3").isEmpty()) {
                    System.out.println("Task 3 does not exist, cannot delete");
                } else {
                    taskService.deleteTaskByName("Task 3");
                    System.out.println("Thread deleted the task: Task 3");
                }
            } catch (Exception e) {
                System.out.println("Error in thread for Delete: Task 3 - " + e.getMessage());
            }
        });

        // Shutdown executor service
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }
}
