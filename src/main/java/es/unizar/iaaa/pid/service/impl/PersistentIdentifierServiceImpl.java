package es.unizar.iaaa.pid.service.impl;

import es.unizar.iaaa.pid.service.PersistentIdentifierDTOService;
import es.unizar.iaaa.pid.domain.PersistentIdentifier;
import es.unizar.iaaa.pid.repository.PersistentIdentifierRepository;
import es.unizar.iaaa.pid.service.dto.PersistentIdentifierDTO;
import es.unizar.iaaa.pid.service.mapper.PersistentIdentifierMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing PersistentIdentifier.
 */
@Service
@Transactional
public class PersistentIdentifierServiceImpl implements PersistentIdentifierDTOService {

    private final Logger log = LoggerFactory.getLogger(PersistentIdentifierServiceImpl.class);

    private final PersistentIdentifierRepository persistentIdentifierRepository;

    private final PersistentIdentifierMapper persistentIdentifierMapper;

    public PersistentIdentifierServiceImpl(PersistentIdentifierRepository persistentIdentifierRepository, PersistentIdentifierMapper persistentIdentifierMapper) {
        this.persistentIdentifierRepository = persistentIdentifierRepository;
        this.persistentIdentifierMapper = persistentIdentifierMapper;
    }

    /**
     * Save a persistentIdentifier.
     *
     * @param persistentIdentifierDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public PersistentIdentifierDTO save(PersistentIdentifierDTO persistentIdentifierDTO) {
        log.debug("Request to save PersistentIdentifier : {}", persistentIdentifierDTO);
        PersistentIdentifier persistentIdentifier = persistentIdentifierMapper.toEntity(persistentIdentifierDTO);
        persistentIdentifier = persistentIdentifierRepository.save(persistentIdentifier);
        return persistentIdentifierMapper.toDto(persistentIdentifier);
    }

    /**
     *  Get all the persistentIdentifiers.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PersistentIdentifierDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PersistentIdentifiers");
        return persistentIdentifierRepository.findAll(pageable)
            .map(persistentIdentifierMapper::toDto);
    }

    /**
     *  Get one persistentIdentifier by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public PersistentIdentifierDTO findOne(Long id) {
        log.debug("Request to get PersistentIdentifier : {}", id);
        PersistentIdentifier persistentIdentifier = persistentIdentifierRepository.findOne(id);
        return persistentIdentifierMapper.toDto(persistentIdentifier);
    }

    /**
     *  Delete the  persistentIdentifier by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete PersistentIdentifier : {}", id);
        persistentIdentifierRepository.delete(id);
    }
}
