package Project.TaskAutomationTool.service;

import Project.TaskAutomationTool.Repository.TaskRepository;
import Project.TaskAutomationTool.model.Task;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

public class TaskSchedulerService {

    @Autowired
    private TaskRepository taskRepository;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(TaskSchedulerService.class);

    public LocalDateTime calculateNextScheduledTime(Task task) {
        if (task == null || task.getFrequency() == null || task.getScheduledTime() == null) {
            logger.error("Task, Frequency or Scheduled Time is cannot be null");
            throw new IllegalStateException("Task, Frequency or Scheduled Time is cannot be null");
        }

        LocalDateTime nextScheduledTime;

        switch (task.getFrequency()) {
            case DAILY -> nextScheduledTime = task.getScheduledTime().plusDays(1);

            case WEEKLY -> nextScheduledTime = task.getScheduledTime().plusWeeks(1);

            case MONTHLY -> nextScheduledTime = task.getScheduledTime().plusMonths(1);

            case YEARLY -> nextScheduledTime = task.getScheduledTime().plusYears(1);

            default -> nextScheduledTime = task.getScheduledTime();

        }
        logger.info("Next scheduled time for task: [{}]: {}", task.getName(), task.getScheduledTime());
        return nextScheduledTime;
    }

    //Modify in the previous task
    public Task calculateAndUpdateNextScheduledTime(Task task) {
        LocalDateTime nextScheduledTime = calculateNextScheduledTime(task);
        task.setScheduledTime(nextScheduledTime);
        return taskRepository.save(task);
    }

}
