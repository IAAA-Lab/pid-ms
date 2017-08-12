package es.unizar.iaaa.pid.service;

import es.unizar.iaaa.pid.PidmsApp;
import es.unizar.iaaa.pid.domain.Change;
import es.unizar.iaaa.pid.domain.Identifier;
import es.unizar.iaaa.pid.domain.Namespace;
import es.unizar.iaaa.pid.domain.Task;
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
public class ChangeServiceTest {
    @Autowired
    private TaskService taskService;

    @Autowired
    private ChangeService changeService;

    @Autowired
    private NamespaceService namespaceService;

    private Change change;


    @Before
    public void before() {
        Namespace namespace = Fixtures.namespace();
        namespaceService.createOrUpdateNamespace(namespace);

        Task task = Fixtures.task(namespace);
        taskService.createOrUpdateTask(task);

        Identifier identifier = Fixtures.identifier();

        change = Fixtures.change(identifier, task);
    }

    @After
    public void after() {
        changeService.deleteAll();
        taskService.deleteAll();
        namespaceService.deleteAll();
    }


    @Test
    public void createChange() {
        changeService.createChange(change);
        List<Change> list = changeService.findAll();

        assertEquals(1, list.size());
        assertEquals(change, list.get(0));
    }
}
