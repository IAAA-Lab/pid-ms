package es.unizar.iaaa.pid.service;

import es.unizar.iaaa.pid.service.dto.TaskDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
     *  Get the "id" task that belongs to an organization where the Principal is a member.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    TaskDTO findOneInPrincipalOrganizations(Long id);
    
    /**
     * Delete all task associate with the Namespace
     * 
     * @param idNamespace id of the Namespace to be deleted
     */
    void deleteAllByNamespaceId(Long idNamespace);
}
