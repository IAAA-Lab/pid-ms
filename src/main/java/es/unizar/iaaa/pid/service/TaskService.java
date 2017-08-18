package es.unizar.iaaa.pid.service;

import es.unizar.iaaa.pid.domain.Namespace;
import es.unizar.iaaa.pid.domain.Task;
import es.unizar.iaaa.pid.domain.enumeration.ProcessStatus;
import es.unizar.iaaa.pid.domain.enumeration.TaskStatus;
import es.unizar.iaaa.pid.repository.TaskRepository;
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

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public void createOrUpdateTask(Task task) {
        taskRepository.save(task);
    }

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public void changeStatus(Task task, TaskStatus error) {
        task.setStatus(error);
        taskRepository.save(task);
    }

    public void deleteAll() {
        taskRepository.deleteAll();
    }

    public List<Task> getPendingTasks(){
        return null;
    }

    public List<Task> getExecutingTasksByNamespace(Namespace namespace) {
        return taskRepository.findExecutingTaksByNamespace(namespace);
    }

    public Task findMostRecentHarvestTask(Namespace namespace) {
        return taskRepository.findMostRecentHarvestTask(namespace, ProcessStatus.HARVEST);
    }

    public Task findMostRecentValidationByIdTask(Namespace namespace) {
        return taskRepository.findMostRecentHarvestTask(namespace, ProcessStatus.VALIDATION_BY_ID);
    }

    public Task executing(Task task, Instant now) {
        task.setStatus(TaskStatus.EXECUTING);
        task.setTimestamp(now);
        return taskRepository.save(task);
    }

    public Task createOrUpdateTask(Namespace namespace, ProcessStatus type, Instant now) {
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
        return taskRepository.save(task);
    }

    public void done(Task task, Instant now) {
        task.setStatus(TaskStatus.DONE);
        task.setTimestamp(now);
    }

}
