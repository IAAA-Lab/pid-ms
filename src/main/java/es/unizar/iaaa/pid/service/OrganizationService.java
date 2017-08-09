package es.unizar.iaaa.pid.service;

import es.unizar.iaaa.pid.service.dto.OrganizationDTO;
import java.util.List;

/**
 * Service Interface for managing Organization.
 */
public interface OrganizationService {

    /**
     * Save a organization.
     *
     * @param organizationDTO the entity to save
     * @return the persisted entity
     */
    OrganizationDTO save(OrganizationDTO organizationDTO);

    /**
     *  Get all the organizations.
     *
     *  @return the list of entities
     */
    List<OrganizationDTO> findAll();

    /**
     *  Get the "id" organization.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    OrganizationDTO findOne(Long id);

    /**
     *  Delete the "id" organization.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
