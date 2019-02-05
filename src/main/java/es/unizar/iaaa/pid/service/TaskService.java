package es.unizar.iaaa.pid.service;

import es.unizar.iaaa.pid.domain.Namespace;
import es.unizar.iaaa.pid.domain.Task;
import es.unizar.iaaa.pid.domain.enumeration.ProcessStatus;
import es.unizar.iaaa.pid.domain.enumeration.TaskStatus;
import es.unizar.iaaa.pid.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

/**
 * Service class for managing tasks.
 */
@Service
@Transactional
public class TaskService {

    private final Logger log = LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    void createTask(Task task) {
        taskRepository.save(task);
    }

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public void changeStatus(Task task, TaskStatus status) {
        log.debug("Change status of Task {} to Status {}", task, status);
        task.setStatus(status);
        taskRepository.save(task);
    }

    public void deleteAll() {
        log.debug("Delete all Tasks");
        taskRepository.deleteAll();
    }

    public List<Task> getExecutingTasksByNamespace(Namespace namespace) {
        return taskRepository.findExecutingTasksByNamespace(namespace);
    }

    public List<Task> getExecutingTasksByType(ProcessStatus type) {
        return taskRepository.findExecutingTasksByType(type);
    }

    public Task findMostRecentHarvestTask(Namespace namespace) {
        return taskRepository.findMostRecentHarvestTask(namespace, ProcessStatus.HARVEST);
    }

    public Task findMostRecentValidationByIdTask(Namespace namespace) {
        return taskRepository.findMostRecentHarvestTask(namespace, ProcessStatus.VALIDATION_BY_ID);
    }

    public void executing(Task task, Instant now) {
        log.debug("Change status of Task {} to Status {} at Instant ", task, TaskStatus.EXECUTING, now);
        task.setStatus(TaskStatus.EXECUTING);
        task.setTimestamp(now);
        taskRepository.save(task);
    }

    public Task createTask(Namespace namespace, ProcessStatus type, Instant now) {
        Task task = new Task();
        task.setNamespace(namespace);
        switch (type) {
            case VALIDATION_BEGIN:
                task.setStatus(TaskStatus.DONE);
                break;
            default:
                task.setStatus(TaskStatus.IN_QUEUE);
        }
        task.setTimestamp(now);
        task.setType(type);
        taskRepository.save(task);
        log.debug("Created Task {}", task);
        return task;
    }

    public void done(Task task, Instant now) {
        log.debug("Change status of Task {} to Status {} at Instant ", task, TaskStatus.DONE, now);
        task.setStatus(TaskStatus.DONE);
        task.setTimestamp(now);
        taskRepository.save(task);
    }

    public void error(Task task, Instant now) {
        log.debug("Change status of Task {} to Status {} at Instant ", task, TaskStatus.ERROR, now);
        task.setStatus(TaskStatus.ERROR);
        task.setTimestamp(now);
        taskRepository.save(task);
    }
}
