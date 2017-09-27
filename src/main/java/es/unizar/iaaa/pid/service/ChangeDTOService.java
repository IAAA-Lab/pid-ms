package es.unizar.iaaa.pid.service;

import es.unizar.iaaa.pid.service.dto.ChangeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Change.
 */
public interface ChangeDTOService {

    /**
     * Save a change.
     *
     * @param changeDTO the entity to save
     * @return the persisted entity
     */
    ChangeDTO save(ChangeDTO changeDTO);

    /**
     *  Get all the changes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ChangeDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" change.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ChangeDTO findOne(Long id);

    /**
     *  Delete the "id" change.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     *  Get all the change that belongs to organizations where the Principal is a member.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ChangeDTO> findAllInPrincipalOrganizations(Pageable pageable);

    /**
     *  Get the "id" change that belongs to an organization where the Principal is a member.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ChangeDTO findOneInPrincipalOrganizations(Long id);
    
    /**
     * Delete all change associated with the task
     * 
     * @param taskId the id of the associate Task
     */
    void deleteAllByTaskId(Long taskId);
}
