package es.unizar.iaaa.pid.service;

import es.unizar.iaaa.pid.service.dto.OrganizationMemberDTO;
import java.util.List;

/**
 * Service Interface for managing OrganizationMember.
 */
public interface OrganizationMemberService {

    /**
     * Save a organizationMember.
     *
     * @param organizationMemberDTO the entity to save
     * @return the persisted entity
     */
    OrganizationMemberDTO save(OrganizationMemberDTO organizationMemberDTO);

    /**
     *  Get all the organizationMembers.
     *
     *  @return the list of entities
     */
    List<OrganizationMemberDTO> findAll();

    /**
     *  Get the "id" organizationMember.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    OrganizationMemberDTO findOne(Long id);

    /**
     *  Delete the "id" organizationMember.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
