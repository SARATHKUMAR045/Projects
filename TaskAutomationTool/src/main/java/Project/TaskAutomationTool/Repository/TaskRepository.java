package Project.TaskAutomationTool.Repository;

import Project.TaskAutomationTool.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByName(String name);

    @Query("SELECT t FROM Task t WHERE t.scheduledTime <= :now AND t.remainderTime = false")
    List<Task> findByRemainderTimeBefore(@Param("now")LocalDateTime now);
}
