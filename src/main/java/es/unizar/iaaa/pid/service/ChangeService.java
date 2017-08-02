package es.unizar.iaaa.pid.service;

import es.unizar.iaaa.pid.service.dto.ChangeDTO;
import java.util.List;

/**
 * Service Interface for managing Change.
 */
public interface ChangeService {

    /**
     * Save a change.
     *
     * @param changeDTO the entity to save
     * @return the persisted entity
     */
    ChangeDTO save(ChangeDTO changeDTO);

    /**
     *  Get all the changes.
     *
     *  @return the list of entities
     */
    List<ChangeDTO> findAll();

    /**
     *  Get the "id" change.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ChangeDTO findOne(Long id);

    /**
     *  Delete the "id" change.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
