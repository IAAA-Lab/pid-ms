package es.unizar.iaaa.pid.service;

import es.unizar.iaaa.pid.service.dto.NamespaceDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Namespace.
 */
public interface NamespaceDTOService {

    /**
     * Save a namespace.
     *
     * @param namespaceDTO the entity to save
     * @return the persisted entity
     */
    NamespaceDTO save(NamespaceDTO namespaceDTO);

    /**
     *  Get all the namespaces.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<NamespaceDTO> findAll(Pageable pageable);

    /**
     *  Get the public namespaces.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<NamespaceDTO> findPublic(Pageable pageable);

    /**
     *  Get the "id" namespace.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    NamespaceDTO findOne(Long id);

    /**
     *  Get the "id" namespace if public.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    NamespaceDTO findOnePublic(Long id);

    /**
     *  Delete the "id" namespace.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

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
}
