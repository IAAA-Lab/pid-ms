package es.unizar.iaaa.pid.service.impl;

import es.unizar.iaaa.pid.service.ChangeService;
import es.unizar.iaaa.pid.domain.Change;
import es.unizar.iaaa.pid.repository.ChangeRepository;
import es.unizar.iaaa.pid.service.dto.ChangeDTO;
import es.unizar.iaaa.pid.service.mapper.ChangeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Change.
 */
@Service
@Transactional
public class ChangeServiceImpl implements ChangeService{

    private final Logger log = LoggerFactory.getLogger(ChangeServiceImpl.class);

    private final ChangeRepository changeRepository;

    private final ChangeMapper changeMapper;

    public ChangeServiceImpl(ChangeRepository changeRepository, ChangeMapper changeMapper) {
        this.changeRepository = changeRepository;
        this.changeMapper = changeMapper;
    }

    /**
     * Save a change.
     *
     * @param changeDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ChangeDTO save(ChangeDTO changeDTO) {
        log.debug("Request to save Change : {}", changeDTO);
        Change change = changeMapper.toEntity(changeDTO);
        change = changeRepository.save(change);
        return changeMapper.toDto(change);
    }

    /**
     *  Get all the changes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ChangeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Changes");
        return changeRepository.findAll(pageable)
            .map(changeMapper::toDto);
    }

    /**
     *  Get one change by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ChangeDTO findOne(Long id) {
        log.debug("Request to get Change : {}", id);
        Change change = changeRepository.findOne(id);
        return changeMapper.toDto(change);
    }

    /**
     *  Delete the  change by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Change : {}", id);
        changeRepository.delete(id);
    }
}
