package es.unizar.iaaa.pid.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import es.unizar.iaaa.pid.service.dto.TaskDTO;

/**
 * Service Interface for managing Task.
 */
public interface TaskDTOService {

    /**
     * Save a task.
     *
     * @param taskDTO the entity to save
     * @return the persisted entity
     */
    TaskDTO save(TaskDTO taskDTO);

    /**
     *  Get all the tasks.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TaskDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" task.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    TaskDTO findOne(Long id);

    /**
     *  Delete the "id" task.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     *  Get all the tasks that belongs to organizations where the Principal is a member.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TaskDTO> findAllInPrincipalOrganizations(Pageable pageable);

    /**
     * Get all the task that are related with public namespaces
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<TaskDTO> findAllPublic(Pageable pageable);
    
    /**
     *  Get the "id" task that belongs to an organization where the Principal is a member.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    TaskDTO findOneInPrincipalOrganizations(Long id);
    
    /**
     * Get the "id" task that belong to a public namespace
     * 
     * @param id the id of the entity
     * @return the entity
     */
    TaskDTO findOnePublic(Long id);
    
    /**
     * Delete all task associate with the Namespace
     * 
     * @param idNamespace id of the Namespace to be deleted
     */
    void deleteAllByNamespaceId(Long idNamespace);
}
