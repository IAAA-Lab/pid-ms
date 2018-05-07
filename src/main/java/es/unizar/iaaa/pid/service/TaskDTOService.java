package es.unizar.iaaa.pid.service;

import es.unizar.iaaa.pid.service.dto.TaskDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Task.
 */
public interface TaskDTOService extends DTOService<Long, TaskDTO> {

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
     * @param namespaceId id of the Namespace to be deleted
     */
    void deleteAllByNamespaceId(Long namespaceId);

}
