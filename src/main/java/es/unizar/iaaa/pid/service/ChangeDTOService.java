package es.unizar.iaaa.pid.service;

import es.unizar.iaaa.pid.service.dto.ChangeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Change.
 */
public interface ChangeDTOService extends DTOService<Long, ChangeDTO> {

    /**
     *  Get all the change that belongs to organizations where the Principal is a member.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ChangeDTO> findAllInPrincipalOrganizations(Pageable pageable);

    /**
     * Get all the change that Namespace is public
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ChangeDTO> findAllPublicOrganizations(Pageable pageable);

    /**
     *  Get the "id" change that belongs to an organization where the Principal is a member.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ChangeDTO findOneInPrincipalOrganizations(Long id);

    /**
     * Get the "id" change that belong a public namespace
     *
     * @param id the id of the entity
     * @return the entity
     */
    ChangeDTO findOnePublic(Long id);

    /**
     * Delete all change associated with the task
     *
     * @param taskId the id of the associate Task
     */
    void deleteAllByTaskId(Long taskId);


    /**
     * Delete all change associated with the Feature
     *
     * @param featureId the id of the associate Feature
     */
    void deleteAllByFeatureId(Long featureId);
}
