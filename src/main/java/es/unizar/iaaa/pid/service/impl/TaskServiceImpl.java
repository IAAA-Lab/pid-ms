package es.unizar.iaaa.pid.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.unizar.iaaa.pid.domain.Feature;
import es.unizar.iaaa.pid.domain.Task;
import es.unizar.iaaa.pid.repository.TaskRepository;
import es.unizar.iaaa.pid.service.ChangeDTOService;
import es.unizar.iaaa.pid.service.TaskDTOService;
import es.unizar.iaaa.pid.service.dto.TaskDTO;
import es.unizar.iaaa.pid.service.mapper.TaskMapper;


/**
 * Service Implementation for managing Task.
 */
@Service
@Transactional
public class TaskServiceImpl implements TaskDTOService {

    private final Logger log = LoggerFactory.getLogger(TaskServiceImpl.class);

    private final TaskRepository taskRepository;
    
    private final ChangeDTOService changeDTOService;

    private final TaskMapper taskMapper;

    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper,
    		ChangeDTOService changeDTOService) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.changeDTOService =changeDTOService;
    }

    /**
     * Save a task.
     *
     * @param taskDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public TaskDTO save(TaskDTO taskDTO) {
        log.debug("Request to save Task : {}", taskDTO);
        Task task = taskMapper.toEntity(taskDTO);
        task = taskRepository.save(task);
        return taskMapper.toDto(task);
    }

    /**
     *  Get all the tasks.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TaskDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Tasks");
        return taskRepository.findAll(taskMapper.toPage(pageable))
            .map(taskMapper::toDto);
    }

    /**
     *  Get one task by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public TaskDTO findOne(Long id) {
        log.debug("Request to get Task : {}", id);
        Task task = taskRepository.findOne(id);
        return taskMapper.toDto(task);
    }

    /**
     *  Delete the  task by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Task : {}", id);
        taskRepository.delete(id);
    }

    /**
     *  Get all the tasks that belongs to organizations where the Principal is a member.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TaskDTO> findAllInPrincipalOrganizations(Pageable pageable) {
        log.debug("Request to get all the tasks that belongs to organizations where the Principal is a member");
        return taskRepository.findAllInPrincipalOrganizations(taskMapper.toPage(pageable))
            .map(taskMapper::toDto);
    }
    
    /**
     * Get all the task that are related with public namespaces
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TaskDTO> findAllPublic(Pageable pageable){
    	log.debug("Request to get all the tasks of public Namespaces");
    	return taskRepository.findAllPublic(taskMapper.toPage(pageable))
    			.map(taskMapper::toDto);
    }

    /**
     *  Get the "id" task that belongs to an organization where the Principal is a member.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public TaskDTO findOneInPrincipalOrganizations(Long id) {
        log.debug("Request to get Task : {}", id);
        Task task = taskRepository.findOneInPrincipalOrganizations(id);
        return taskMapper.toDto(task);
    }
    
    /**
     * Get the "id" task that belong to a public namespace
     * 
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public TaskDTO findOnePublic(Long id){
    	log.debug("Request to get Task: {}", id);
    	Task task = taskRepository.findOnePublic(id);
    	return taskMapper.toDto(task);
    }
    
    /**
     * Delete all task associate with the Namespace
     * 
     * @param idNamespace id of the Namespace to be deleted
     */
    @Override
    public void deleteAllByNamespaceId(Long namespaceId){
    	log.debug("Request to delete task associate to NamespaceId {}", namespaceId);
    	//delete all changes associated to the task
    	List<Task> listTask = taskRepository.findAllByNamespaceId(namespaceId);
    	for(Task task : listTask){
    		changeDTOService.deleteAllByTaskId(task.getId());
    		taskRepository.delete(task.getId());
    	}
    }

}
