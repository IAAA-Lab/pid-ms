package es.unizar.iaaa.pid.harvester.tasks;

import es.unizar.iaaa.pid.domain.Task;
import es.unizar.iaaa.pid.domain.enumeration.ProcessStatus;
import es.unizar.iaaa.pid.service.NamespaceService;
import es.unizar.iaaa.pid.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

abstract class AbstractTaskRunner implements TaskRunner {
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
            task = taskService.executing(task, Instant.now());
            log("executing");
        } catch (Exception e) {
            error(e);
        }
    }

    protected abstract void doTask();

    protected void doEnd() {
        try {
            namespaceService.doneTaskAndSetNextStep(task, Instant.now(), getNextStep());
            log("done");
        } catch (Exception e) {
            error(e);
        }
    }

    void log(String msg, Object... objects) {
        List<Object> l = new ArrayList<>();
        l.addAll(Arrays.asList(task.getType(), task.getId(), task.getNamespace().getNamespace()));
        l.addAll(Arrays.asList(objects));
        LOGGER.info("Task \"{}:{}\" for namespace \"{}\" : " + msg, l.toArray());
    }

    void error(Throwable e) {
        List<Object> l = new ArrayList<>();
        l.addAll(Arrays.asList(task.getType(), task.getId(), task.getNamespace().getNamespace()));
        l.addAll(Arrays.asList(e.getMessage(), e));
        LOGGER.info("Task \"{}:{}\" for namespace \"{}\" : failed due to {}", l.toArray());
    }

    protected abstract ProcessStatus getNextStep();

}
