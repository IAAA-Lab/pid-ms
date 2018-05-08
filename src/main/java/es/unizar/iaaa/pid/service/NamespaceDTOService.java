package es.unizar.iaaa.pid.service;

import es.unizar.iaaa.pid.service.dto.NamespaceDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Namespace.
 */
public interface NamespaceDTOService  extends DTOService<Long, NamespaceDTO> {

    /**
     *  Get the public namespaces.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<NamespaceDTO> findPublic(Pageable pageable);


    /**
     *  Get the "id" namespace if public.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    NamespaceDTO findOnePublic(Long id);


    /**
     *  Get the "namespace" namespace.
     *
     *  @param namespace the namespace of the entity
     *  @return the entity
     */
    NamespaceDTO findOneByNamespace(String namespace);

    /**
     *  Get all the nemespaces that are public or belongs to organizations where the Principal is a member.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<NamespaceDTO> findAllInPrincipalOrganizationsOrPublic(Pageable pageable);

    /**
     *  Get the "id" namepspace that is public or belongs to an organization where the Principal is a member.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    NamespaceDTO findOneInPrincipalOrganizationsOrPublic(Long id);

    /**
     * Delete all namespace associate with the organization
     *
     * @param organizationId id of the organization to be deleted
     */
    void deleteAllByOrganizationId(Long organizationId);
}
