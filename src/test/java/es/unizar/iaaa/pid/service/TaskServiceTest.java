package es.unizar.iaaa.pid.service;

import es.unizar.iaaa.pid.PidmsApp;
import es.unizar.iaaa.pid.domain.Namespace;
import es.unizar.iaaa.pid.domain.Task;
import es.unizar.iaaa.pid.domain.enumeration.TaskStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PidmsApp.class)
@Transactional
public class TaskServiceTest {
    @Autowired
    private TaskService taskService;

    @Autowired
    private NamespaceService namespaceService;

    private Task task;
    private Namespace ns;

    @Before
    public void before() {
        ns = Fixtures.namespace();
        namespaceService.createOrUpdateNamespace(ns);

        task = Fixtures.task(ns);
        taskService.createOrUpdateTask(task);
    }

    @After
    public void after() {
        taskService.deleteAll();
        namespaceService.deleteAll();
    }


    @Test
    public void changeTaskStatus() {
        taskService.changeStatus(task, TaskStatus.ERROR);

        List<Task> list = taskService.findAll();
        assertEquals(TaskStatus.ERROR, list.get(0).getStatus());
    }

    @Test
    public void getExecutingTaskByNamespace(){
        taskService.changeStatus(task, TaskStatus.EXECUTING);

        List<Task> list = taskService.getExecutingTasksByNamespace(ns);

        assertEquals(1,list.size());
    }

    @Test
    public void createTask() {
        List<Task> list = taskService.findAll();

        assertEquals(1, list.size());
        assertEquals(task, list.get(0));
    }}
