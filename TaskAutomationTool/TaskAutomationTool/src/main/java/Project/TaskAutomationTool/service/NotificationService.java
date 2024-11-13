package Project.TaskAutomationTool.service;

import Project.TaskAutomationTool.Repository.TaskRepository;
import Project.TaskAutomationTool.model.Email;
import Project.TaskAutomationTool.model.Task;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    private EmailService emailService;

    @Autowired
    private TaskRepository taskRepository;


    public void checkAndSendEmail() {
        List<Task> tasksToNotify = taskRepository.findByRemainderTimeBefore(LocalDateTime.now());
        for (Task task : tasksToNotify) {
            try {
                Email email = new Email();
                String emailBody = String.format("Remainder: your task is %s, and scheduled at %s",
                        task.getName(), task.getScheduledTime());

                emailService.sendEmail(email.getRecipient(), "Task Remainder", emailBody);

                task.setRemainderSent(true);
                taskRepository.save(task);
            } catch (Exception e) {
                logger.error("Failed to send remainder for task: [{}], {}", task.getName(), e.getMessage());
            }

        }
    }

}
