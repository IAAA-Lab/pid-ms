package es.unizar.iaaa.pid.service;

import es.unizar.iaaa.pid.service.dto.PersistentIdentifierDTO;
import java.util.List;

/**
 * Service Interface for managing PersistentIdentifier.
 */
public interface PersistentIdentifierService {

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
     *  @return the list of entities
     */
    List<PersistentIdentifierDTO> findAll();

    /**
     *  Get the "id" persistentIdentifier.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    PersistentIdentifierDTO findOne(Long id);

    /**
     *  Delete the "id" persistentIdentifier.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
