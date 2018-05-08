package es.unizar.iaaa.pid.service;

import es.unizar.iaaa.pid.service.dto.PersistentIdentifierDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Service Interface for managing PersistentIdentifier.
 */
public interface PersistentIdentifierDTOService extends DTOService<UUID, PersistentIdentifierDTO> {

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

    /**
     * Delete all persistentIdentifier associated with the feature
     *
     * @param featureId the id of the associate feature
     */
    void deleteAllByFeatureId(Long featureId);
}
