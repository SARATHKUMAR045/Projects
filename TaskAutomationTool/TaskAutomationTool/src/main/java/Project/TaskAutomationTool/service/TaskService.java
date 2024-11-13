package Project.TaskAutomationTool.service;

import Project.TaskAutomationTool.Repository.TaskRepository;
import Project.TaskAutomationTool.model.Task;
import jakarta.transaction.Transactional;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    private final List<Task> taskList = new CopyOnWriteArrayList<>();

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(TaskService.class);


    //Add task
    public Task createTask(Task task) {
        if (task.getName() == null || task.getName().isEmpty()) {
            logger.error("Failed to create a task: this name empty or null");
            throw new IllegalStateException("Task Name is cannot be empty or null");
        }
        taskRepository.save(task);
        logger.info("Task Created: {}", task);
        return task;
    }

    //Get necessary task using a Pagination method this is cut out the memory usage
    public List<Task> getTaskList(int page, int size) {
        if (page <= 0 || size <= 0) {
            throw new IllegalStateException("The page and size must be above 0");
        }

        return taskRepository.findAll(PageRequest.of(page - 1, size)).getContent();
    }

    //get all task using this function
    public List<Task> getAllTask() {
        return taskRepository.findAll();
    }

    //Get Task By Id
    public Task getTaskById(Long id) {
        Optional<Task> task = taskRepository.findById(id);
        return task.orElseThrow(() -> new IllegalArgumentException("Task with Id: " + id + "Not found"));
    }

    //Get by task name
    public List<Task> getTaskByName(String name) {
        return taskRepository.findByName(name);
    }

    //Update an existing task
    public Optional<Task> updateTask(Long id, Task updatedTask) {
        Optional<Task> taskOptional = taskRepository.findById(id);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();

            task.setDescription(updatedTask.getDescription());
            task.setScheduledTime(updatedTask.getScheduledTime());
            task.setFrequency(updatedTask.getFrequency());
            taskRepository.save(task);

            return Optional.of(task);
        }
        return Optional.empty();
    }

    //Delete task by name
    @Transactional
    public void deleteTaskByName(String name) {
        List<Task> taskOptional = taskRepository.findByName(name);
        if (taskOptional.isEmpty()) {
            logger.warn("Task not found: {}", name);
        } else {
            taskOptional.forEach(taskRepository::delete);
            logger.info("Task Successfully deleted: {}", name);
        }
    }
}


