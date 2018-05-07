package es.unizar.iaaa.pid.web.rest;

import com.codahale.metrics.annotation.Timed;
import es.unizar.iaaa.pid.service.TaskDTOService;
import es.unizar.iaaa.pid.service.dto.TaskDTO;
import es.unizar.iaaa.pid.web.rest.util.ControllerUtil;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;
import java.util.function.Supplier;

/**
 * REST controller for managing Task.
 */
@RestController
@RequestMapping("/api")
public class TaskResource {

    private final Logger log = LoggerFactory.getLogger(TaskResource.class);

    private static final String ENTITY_NAME = "task";

    private final TaskDTOService taskService;

    public TaskResource(TaskDTOService taskService) {
        this.taskService = taskService;
    }

    /**
     * POST  /tasks : Create a new task.
     *
     * @param taskDTO the taskDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new taskDTO, or with status 400 (Bad Request) if the task has already an ID
     */
    @PostMapping("/tasks")
    @Timed
    public ResponseEntity<TaskDTO> createTask(UriComponentsBuilder uriBuilder, @Valid @RequestBody TaskDTO taskDTO) {
        log.debug("REST request to save Task : {}", taskDTO);
        return ControllerUtil
            .with(ENTITY_NAME, uriBuilder.path("/api/tasks/{id}"), taskService)
            .forbidWhen(notallowed())
            .doPost(taskDTO);
    }

    /**
     * PUT  /tasks/:id : get the "id" task.
     *
     * @param id the id of the taskDTO to update
     * @param taskDTO the taskDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated taskDTO,
     * or with status 404 (Not Found) if the taskDTO is not valid,
     */
    @PutMapping("/tasks/{id}")
    @Timed
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, @Valid @RequestBody TaskDTO taskDTO) {
        log.debug("REST request to update Task : {}", taskDTO);
        return ControllerUtil
            .with(ENTITY_NAME, taskService)
            .forbidWhen(notallowed())
            .doPut(id, taskDTO);
    }

    /**
     * GET  /tasks : get all the tasks.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of tasks in body
     */
    @GetMapping("/tasks")
    @Timed
    public ResponseEntity<List<TaskDTO>> getAllTasks(UriComponentsBuilder uriBuilder, @ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Tasks");
        return ControllerUtil
            .with(uriBuilder.path("/api/tasks"), taskService)
            .list(taskService::findAllPublic)
            .listAuthenticated(taskService::findAllInPrincipalOrganizations)
            .doGet(pageable);
    }

    /**
     * GET  /tasks/:id : get the "id" task.
     *
     * @param id the id of the taskDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the taskDTO, or with status 404 (Not Found)
     */
    @GetMapping("/tasks/{id}")
    @Timed
    public ResponseEntity<TaskDTO> getTask(@PathVariable Long id) {
        log.debug("REST request to get Task : {}", id);
        return ControllerUtil
            .with(taskService)
            .get(taskService::findOnePublic)
            .getAuthenticated(taskService::findOneInPrincipalOrganizations)
            .doGet(id);
    }

    /**
     * DELETE  /tasks/:id : delete the "id" task.
     *
     * @param id the id of the taskDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tasks/{id}")
    @Timed
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        log.debug("REST request to delete Task : {}", id);
        return ControllerUtil
            .with(ENTITY_NAME, taskService)
            .forbidWhen(notallowed())
            .doDelete(id);
    }

    private Supplier<Boolean> notallowed() {
        return () -> {
        	return true;
        };
    }
}
