package es.unizar.iaaa.pid.service;

import es.unizar.iaaa.pid.service.dto.OrganizationMemberDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<OrganizationMemberDTO> findAll(Pageable pageable);

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
