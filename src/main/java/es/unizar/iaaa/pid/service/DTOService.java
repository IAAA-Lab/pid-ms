package es.unizar.iaaa.pid.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DTOService<ID, T> {

    /**
     * Save a DTO.
     *
     * @param dto the DTO to save
     * @return the persisted entity
     */
    T save(T dto);


    /**
     *  Get the DTO with "id".
     *
     *  @param id the id of the DTO
     *  @return the entity
     */
    T findOne(ID id);

    /**
     *  Get all DTOs.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<T> findAll(Pageable pageable);


    /**
     *  Delete the DTO with "id" .
     *
     *  @param id the id of the DTO
     */
    void delete(ID id);
}
