package es.unizar.iaaa.pid.service;

import es.unizar.iaaa.pid.service.dto.OrganizationMemberDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing OrganizationMember.
 */
public interface OrganizationMemberDTOService extends DTOService<Long, OrganizationMemberDTO> {

    /**
     *  Get all the organizationMembers that belongs to organizations where the Principal is a member.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<OrganizationMemberDTO> findAllInPrincipalOrganizations(Pageable pageable);

    /**
     *  Get the organizationMember that belongs to a specific organization where the Principal is a member.
     *
     *  @param id the organization id
     *  @return the list of entities
     */
    OrganizationMemberDTO findOneByOrganizationInPrincipal(Long id);
    /**
     *  Get the "id" organizationMember that belongs to an organization where the Principal is a member.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    OrganizationMemberDTO findOneInPrincipalOrganizations(Long id);

    /**
     * Delete all organizationMembers associate with the organization
     *
     * @param id id of the organization to be deleted
     */
    void deleteAllByOrganizationId(Long id);
}
