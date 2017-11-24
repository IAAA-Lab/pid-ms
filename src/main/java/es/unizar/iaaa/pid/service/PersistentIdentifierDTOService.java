package es.unizar.iaaa.pid.service;

import es.unizar.iaaa.pid.service.dto.PersistentIdentifierDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Service Interface for managing PersistentIdentifier.
 */
public interface PersistentIdentifierDTOService {

    /**
     * Save a persistentIdentifier.
     *
     * @param persistentIdentifierDTO the entity to save
     * @return the persisted entity
     */
    PersistentIdentifierDTO save(PersistentIdentifierDTO persistentIdentifierDTO);

    /**
     *  Get all the persistentIdentifiers.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<PersistentIdentifierDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" persistentIdentifier.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    PersistentIdentifierDTO findOne(UUID id);

    /**
     *  Delete the "id" persistentIdentifier.
     *
     *  @param id the id of the entity
     */
    void delete(UUID id);

    /**
     *  Get all the persistentIdentifiers public or in organizations where the Principal is a memeber.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<PersistentIdentifierDTO> findAllPublicOrInPrincipalOrganizations(Pageable pageable);

    /**
     *  Get all the persistentIdentifiers public.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<PersistentIdentifierDTO> findAllPublic(Pageable pageable);
    
    /**
     *  Get the "id" persistentIdentifier public or in organizations where the Principal is a memeber.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    PersistentIdentifierDTO findOnePublicOrInPrincipalOrganizations(UUID id);
    
    /**
     *  Get the "id" persistentIdentifier public.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    PersistentIdentifierDTO findOnePublic(UUID id);
    
    /**
     * Delete all persistentIdentifier associated with the namespace
     * 
     * @param namespaceId the id of the associate namespace
     */
    void deleteAllByNamespaceId(Long namespaceId);
    
}
