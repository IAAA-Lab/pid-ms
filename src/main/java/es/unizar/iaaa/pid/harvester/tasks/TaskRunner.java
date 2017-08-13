package es.unizar.iaaa.pid.harvester.tasks;

import es.unizar.iaaa.pid.domain.Task;

public interface TaskRunner extends Runnable {
    void setTask(Task task);
}
