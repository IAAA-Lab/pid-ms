package es.unizar.iaaa.pid.service.impl;

import es.unizar.iaaa.pid.service.IdentifierService;
import es.unizar.iaaa.pid.domain.Identifier;
import es.unizar.iaaa.pid.repository.IdentifierRepository;
import es.unizar.iaaa.pid.service.dto.IdentifierDTO;
import es.unizar.iaaa.pid.service.mapper.IdentifierMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Identifier.
 */
@Service
@Transactional
public class IdentifierServiceImpl implements IdentifierService{

    private final Logger log = LoggerFactory.getLogger(IdentifierServiceImpl.class);

    private final IdentifierRepository identifierRepository;

    private final IdentifierMapper identifierMapper;

    public IdentifierServiceImpl(IdentifierRepository identifierRepository, IdentifierMapper identifierMapper) {
        this.identifierRepository = identifierRepository;
        this.identifierMapper = identifierMapper;
    }

    /**
     * Save a identifier.
     *
     * @param identifierDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public IdentifierDTO save(IdentifierDTO identifierDTO) {
        log.debug("Request to save Identifier : {}", identifierDTO);
        Identifier identifier = identifierMapper.toEntity(identifierDTO);
        identifier = identifierRepository.save(identifier);
        return identifierMapper.toDto(identifier);
    }

    /**
     *  Get all the identifiers.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<IdentifierDTO> findAll() {
        log.debug("Request to get all Identifiers");
        return identifierRepository.findAll().stream()
            .map(identifierMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get one identifier by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public IdentifierDTO findOne(Long id) {
        log.debug("Request to get Identifier : {}", id);
        Identifier identifier = identifierRepository.findOne(id);
        return identifierMapper.toDto(identifier);
    }

    /**
     *  Delete the  identifier by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Identifier : {}", id);
        identifierRepository.delete(id);
    }
}
