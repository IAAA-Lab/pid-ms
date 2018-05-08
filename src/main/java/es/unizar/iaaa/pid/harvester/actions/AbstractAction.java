package es.unizar.iaaa.pid.harvester.actions;

import es.unizar.iaaa.pid.config.harvester.Events;
import es.unizar.iaaa.pid.config.harvester.States;
import es.unizar.iaaa.pid.domain.Namespace;
import es.unizar.iaaa.pid.domain.Task;
import es.unizar.iaaa.pid.domain.enumeration.ItemStatus;
import es.unizar.iaaa.pid.domain.enumeration.ProcessStatus;
import es.unizar.iaaa.pid.harvester.tasks.TaskRunner;
import es.unizar.iaaa.pid.service.NamespaceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.statemachine.action.Action;

import java.time.Instant;
import java.util.List;


abstract class AbstractAction implements Action<States,Events> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractAction.class);

    protected final NamespaceService namespaceService;

    private final TaskExecutor executor;

    private final ApplicationContext context;

    AbstractAction(ApplicationContext context, TaskExecutor executor, NamespaceService namespaceService) {
        this.context = context;
        this.executor = executor;
        this.namespaceService = namespaceService;
    }

    void doNextTask(List<Namespace> list, ProcessStatus nextStatus, String taskName, Instant now) {
        for(Namespace namespace : list) {
            Task task;
            try {
                task = namespaceService.nextTask(namespace.getId(), nextStatus, now);
            } catch(Exception e) {
                LOGGER.error("Namespace \"{}\" cannot be processed due to exception: {}", namespace.getNamespace(), e.getMessage(), e);
                continue;
            }
            TaskRunner taskRunner = (TaskRunner) context.getBean(taskName);
            taskRunner.setTask(task);
            LOGGER.info("Task \"{}:{}\" for namespace \"{}\" : enqueued", task.getType(), task.getId(), namespace.getNamespace());
            executor.execute(taskRunner);
        }
    }

    void doFirstTask(List<Namespace> list, ProcessStatus nextStatus, ItemStatus itemStatus, Instant now) {
        for(Namespace namespace : list) {
            Task task;
            try {

                task = namespaceService.firstTask(namespace.getId(), nextStatus, itemStatus, now);
            } catch(Exception e) {
                LOGGER.error("Namespace \"{}\" cannot be processed due to exception: {}", namespace.getNamespace(), e.getMessage(), e);
                continue;
            }
            LOGGER.info("Task \"{}:{}\" for namespace \"{}\" : done", task.getType(), task.getId(), namespace.getNamespace());
        }
    }
}
