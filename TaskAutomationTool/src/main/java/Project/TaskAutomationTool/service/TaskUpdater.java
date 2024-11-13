package Project.TaskAutomationTool.service;

import Project.TaskAutomationTool.model.Task;

import java.util.Optional;

public class TaskUpdater {

    private final TaskService taskService;

    public TaskUpdater(TaskService taskService) {
        this.taskService = taskService;
    }

    public void updateTaskInThread(String taskName, Task updatedTask) throws InterruptedException {
        Thread updateTaskThread = new Thread(() -> {
            Optional<Task> result = taskService.updateTask(updatedTask.getId(), updatedTask);
            if (result.isPresent()) {
                System.out.println("Thread updated " + taskName + ": " + result.get());
            } else {
                System.out.print(taskName + "Not found");
            }
        });

        updateTaskThread.start();

        updateTaskThread.join();

    }
}
