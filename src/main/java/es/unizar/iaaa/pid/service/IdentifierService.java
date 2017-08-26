package es.unizar.iaaa.pid.service;

import es.unizar.iaaa.pid.service.dto.IdentifierDTO;
import java.util.List;

/**
 * Service Interface for managing Identifier.
 */
public interface IdentifierService {

    /**
     * Save a identifier.
     *
     * @param identifierDTO the entity to save
     * @return the persisted entity
     */
    IdentifierDTO save(IdentifierDTO identifierDTO);

    /**
     *  Get all the identifiers.
     *
     *  @return the list of entities
     */
    List<IdentifierDTO> findAll();

    /**
     *  Get the "id" identifier.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    IdentifierDTO findOne(Long id);

    /**
     *  Delete the "id" identifier.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
