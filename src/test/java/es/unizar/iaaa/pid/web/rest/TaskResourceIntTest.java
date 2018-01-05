package es.unizar.iaaa.pid.web.rest;

import es.unizar.iaaa.pid.PidmsApp;
import es.unizar.iaaa.pid.domain.Namespace;
import es.unizar.iaaa.pid.domain.Task;
import es.unizar.iaaa.pid.service.NamespaceDTOService;
import es.unizar.iaaa.pid.service.OrganizationDTOService;
import es.unizar.iaaa.pid.service.TaskDTOService;
import es.unizar.iaaa.pid.service.dto.TaskDTO;
import es.unizar.iaaa.pid.service.mapper.NamespaceMapper;
import es.unizar.iaaa.pid.service.mapper.OrganizationMapper;
import es.unizar.iaaa.pid.service.mapper.TaskMapper;
import es.unizar.iaaa.pid.web.rest.errors.ExceptionTranslator;
import es.unizar.iaaa.pid.web.rest.util.MvcResultUtils;
import es.unizar.iaaa.pid.web.rest.util.TestUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static es.unizar.iaaa.pid.web.rest.util.ResourceFixtures.*;
import static es.unizar.iaaa.pid.web.rest.util.HeaderUtil.ERROR_ID_ALREADY_EXIST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
/**
 * Test class for the TaskResource REST controller.
 *
 * @see TaskResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PidmsApp.class)
@Transactional
@WithMockUser(username=UserResourceIntTest.DEFAULT_LOGIN,password=UserResourceIntTest.DEFAULT_PASSWORD)
public class TaskResourceIntTest extends LoggedUser {

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private TaskDTOService taskDTOService;

    @Autowired
    private OrganizationDTOService organizationDTOService;

    @Autowired
    private OrganizationMapper organizationMapper;

    @Autowired
    private NamespaceDTOService namespaceDTOService;

    @Autowired
    private NamespaceMapper namespaceMapper;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTaskMockMvc;

    private Namespace namespace;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TaskResource taskResource = new TaskResource(taskDTOService);
        this.restTaskMockMvc = MockMvcBuilders.standaloneSetup(taskResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        namespace = namespace();
        Long organizationId = organizationDTOService.save(organizationMapper.toDto(namespace.getOwner())).getId();

        namespace.getOwner().setId(organizationId);
        linkOrganizationToLoggedUser(namespace.getOwner());

        Long namespaceId = namespaceDTOService.save(namespaceMapper.toDto(namespace)).getId();
        namespace.setId(namespaceId);
    }

    @Test
    public void createTask() throws Exception {
        // Create the Task
        TaskDTO taskDTO = taskMapper.toDto(task(namespace));

        MvcResult result = restTaskMockMvc.perform(post("/api/tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taskDTO)))
            .andExpect(status().isCreated()).andReturn();

        // Extract current id from Location
        Long id = MvcResultUtils.extractIdFromLocation(result);

        // Validate the Task in the database
        TaskDTO testTask = taskDTOService.findOne(id);
        assertThat(testTask).isNotNull();
        assertThat(testTask.getTimestamp()).isEqualTo(DEFAULT_TIMESTAMP);
        assertThat(testTask.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testTask.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testTask.getNumErrors()).isEqualTo(DEFAULT_NUM_ERRORS);
    }

    @Test
    public void createTaskWithIdMustFail() throws Exception {
        // Create the Task with an existing ID
        Task task = task(namespace);
        task.setId(1L);
        TaskDTO taskDTO = taskMapper.toDto(task);

        // An entity with an  ID cannot be created, so this API call must fail
        restTaskMockMvc.perform(post("/api/tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taskDTO)))
            .andExpect(status().isBadRequest())
            .andExpect(header().string(ERROR_HEADER,ERROR_ID_ALREADY_EXIST));
    }

    @Test
    public void checkTimestampIsRequired() throws Exception {
        Task task = task(namespace);
        // set the field null
        task.setTimestamp(null);
        TaskDTO taskDTO = taskMapper.toDto(task);

        restTaskMockMvc.perform(post("/api/tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taskDTO)))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void checkTypeIsRequired() throws Exception {
        Task task = task(namespace);
        // set the field null
        task.setType(null);
        TaskDTO taskDTO = taskMapper.toDto(task);

        restTaskMockMvc.perform(post("/api/tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taskDTO)))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void checkStatusIsRequired() throws Exception {
        Task task = task(namespace);
        // set the field null
        task.setStatus(null);
        // Create the Task, which fails.
        TaskDTO taskDTO = taskMapper.toDto(task);

        restTaskMockMvc.perform(post("/api/tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taskDTO)))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void getAllTasks() throws Exception {
        // Initialize the database
        Task task = task(namespace);
        TaskDTO taskDTO = taskMapper.toDto(task);
    	taskDTO = taskDTOService.save(taskDTO);

        // Get all the taskList
        restTaskMockMvc.perform(get("/api/tasks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taskDTO.getId().intValue())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].numErrors").value(hasItem(DEFAULT_NUM_ERRORS)));
    }

    @Test
    public void getTask() throws Exception {
        // Initialize the database
        Task task = task(namespace);
        TaskDTO taskDTO = taskMapper.toDto(task);
        taskDTO = taskDTOService.save(taskDTO);

        // Get the task
        restTaskMockMvc.perform(get("/api/tasks/{id}", taskDTO.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(taskDTO.getId().intValue()))
            .andExpect(jsonPath("$.timestamp").value(DEFAULT_TIMESTAMP.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.numErrors").value(DEFAULT_NUM_ERRORS));
    }

    @Test
    public void getNonExistingTask() throws Exception {
        // Get the task
        restTaskMockMvc.perform(get("/api/tasks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateTask() throws Exception {
        // Initialize the database
        Task task = task(namespace);
        TaskDTO taskDTO = taskMapper.toDto(task);
        taskDTO = taskDTOService.save(taskDTO);

        // Update the task
        TaskDTO updatedTask = taskDTOService.findOne(taskDTO.getId());
        updatedTask.setTimestamp(UPDATED_TIMESTAMP);
        updatedTask.setType(UPDATED_TYPE);
        updatedTask.setStatus(UPDATED_STATUS);
        updatedTask.setNumErrors(UPDATED_NUM_ERRORS);

        restTaskMockMvc.perform(put("/api/tasks/{id}", updatedTask.getId())
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTask)))
            .andExpect(status().isOk());

        // Validate the Task in the database
        TaskDTO testTask = taskDTOService.findOne(updatedTask.getId());
        assertThat(testTask.getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);
        assertThat(testTask.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testTask.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTask.getNumErrors()).isEqualTo(UPDATED_NUM_ERRORS);
    }

    @Test
    public void updateNonExistingTaskFails() throws Exception {
        // Create the Task
        Task task = task(namespace);
        Long id = Long.MAX_VALUE;
        TaskDTO taskDTO = taskMapper.toDto(task);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTaskMockMvc.perform(put("/api/tasks/{id}", id)
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taskDTO)))
            .andExpect(status().isNotFound());
    }

    @Test
    public void deleteTask() throws Exception {
        // Initialize the database
        Task task = task(namespace);
        TaskDTO taskDTO = taskMapper.toDto(task);
        taskDTO = taskDTOService.save(taskDTO);

        restTaskMockMvc.perform(get("/api/tasks/{id}", taskDTO.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        restTaskMockMvc.perform(delete("/api/tasks/{id}", taskDTO.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        restTaskMockMvc.perform(get("/api/tasks/{id}", taskDTO.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNotFound());
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Task.class);
        Task task1 = new Task();
        task1.setId(1L);
        Task task2 = new Task();
        task2.setId(task1.getId());
        assertThat(task1).isEqualTo(task2);
        task2.setId(2L);
        assertThat(task1).isNotEqualTo(task2);
        task1.setId(null);
        assertThat(task1).isNotEqualTo(task2);
    }

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TaskDTO.class);
        TaskDTO taskDTO1 = new TaskDTO();
        taskDTO1.setId(1L);
        TaskDTO taskDTO2 = new TaskDTO();
        assertThat(taskDTO1).isNotEqualTo(taskDTO2);
        taskDTO2.setId(taskDTO1.getId());
        assertThat(taskDTO1).isEqualTo(taskDTO2);
        taskDTO2.setId(2L);
        assertThat(taskDTO1).isNotEqualTo(taskDTO2);
        taskDTO1.setId(null);
        assertThat(taskDTO1).isNotEqualTo(taskDTO2);
    }

    @Test
    public void testEntityFromId() {
        assertThat(taskMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(taskMapper.fromId(null)).isNull();
    }
}
