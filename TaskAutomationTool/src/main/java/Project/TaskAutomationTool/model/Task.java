package Project.TaskAutomationTool.model;

import jakarta.persistence.*;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;
    private String description;
    private LocalDateTime scheduledTime;

    @Enumerated(EnumType.ORDINAL)
    private TaskFrequency frequency;

    @Column(name = "remainder_time")
    private boolean remainderTime;

    public Task(){
    }

    public Task(String name, String description, LocalDateTime scheduledTime, TaskFrequency frequency) {

        this.name = name;
        this.description = description;
        this.scheduledTime = scheduledTime;
        this.frequency = frequency;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalStateException("Task name cannot be null or empty");
        }
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(LocalDateTime scheduledTime) {
        if (scheduledTime.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Scheduled Time is not before the past");
        }
        this.scheduledTime = scheduledTime;
    }

    public TaskFrequency getFrequency() {
        return frequency;
    }

    public void setFrequency(TaskFrequency frequency) {
        this.frequency = frequency;
    }

    public void setRemainderSent(boolean remainderSent) {
        Task task = new Task();
        if (!task.getScheduledTime().isAfter(LocalDateTime.now())) {
            throw new IllegalStateException("Scheduled Not in the past");
        }
    }

    @Override
    public String toString() {
        return String.format("Task[name = '%s', description = '%s', scheduledTime = '%s', frequency = '%s']",
                name, description, scheduledTime, frequency);
    }

}
