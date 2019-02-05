package es.unizar.iaaa.pid.harvester.tasks;

import es.unizar.iaaa.pid.domain.Task;
import es.unizar.iaaa.pid.domain.enumeration.ProcessStatus;
import es.unizar.iaaa.pid.service.NamespaceService;
import es.unizar.iaaa.pid.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.text.MessageFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public abstract class AbstractTaskRunner implements TaskRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskRunner.class);
    protected final TaskService taskService;
    protected final NamespaceService namespaceService;
    protected Task task;

    @Autowired
    public AbstractTaskRunner(NamespaceService namespaceService, TaskService taskService) {
        this.namespaceService = namespaceService;
        this.taskService = taskService;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    @Override
    public void run() {
        doBegin();
        doTask();
        doEnd();
    }

    private void doBegin() {
        try {
            taskService.executing(task, Instant.now());
            log("executing");
        } catch (Exception e) {
            error("BEGIN", e);
            doCancel();
        }
    }

    private void doTask() {
        try {
            runTask();
        } catch (Exception e) {
            error("EXECUTION", e);
            doCancel();
        }
    }

    protected abstract void runTask();

    protected void doEnd() {
        try {
            namespaceService.doneTaskAndSetNextStep(task, getNextStep(), Instant.now());
            log("done");
        } catch (Exception e) {
            error("DONE", e);
        }
    }

    protected void doCancel() {
        try {
            namespaceService.errorTaskAndSetNextStep(task, getNextStep(), Instant.now());
            log("done");
        } catch (Exception e) {
            error("DONE", e);
        }
    }

    protected void log(String msg, Object... objects) {
        List<Object> l = new ArrayList<>();
        l.addAll(asList(task.getType(), task.getId(), task.getNamespace().getNamespace()));
        l.addAll(asList(objects));
        LOGGER.info("Task \"{}:{}\" for namespace \"{}\" : " + msg, l.toArray());
    }

    protected void error(String where, Throwable e) {
        List<Object> l = asList(task.getType(), task.getId(), task.getNamespace().getNamespace(), where, e.getMessage());
        String msg = MessageFormat.format("Task \"{}:{}\" for namespace \"{}\" : failed in {} due to {}", l.toArray());
        LOGGER.info(msg, e);
    }

    protected abstract ProcessStatus getNextStep();

    protected abstract List<ProcessStatus> getPossibleCurrentSteps();

    @EventListener(ApplicationReadyEvent.class)
    public void cancelAll() {
        for (ProcessStatus type : getPossibleCurrentSteps()) {
            LOGGER.info("Cleaning dangling tasks for {}", type);
            for (Task task : taskService.getExecutingTasksByType(type)) {
                setTask(task);
                doCancel();
            }
        }
    }
}
