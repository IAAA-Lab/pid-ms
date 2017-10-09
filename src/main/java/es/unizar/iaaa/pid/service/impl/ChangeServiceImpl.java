package es.unizar.iaaa.pid.service.impl;

import es.unizar.iaaa.pid.domain.Change;
import es.unizar.iaaa.pid.repository.ChangeRepository;
import es.unizar.iaaa.pid.service.ChangeDTOService;
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
public class ChangeServiceImpl implements ChangeDTOService {

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
        return changeRepository.findAll(changeMapper.toPage(pageable))
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

    /**
     *  Get all the change that belongs to organizations where the Principal is a member.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ChangeDTO> findAllInPrincipalOrganizations(Pageable pageable) {
        log.debug("Request to get all the change that belongs to organizations where the Principal is a member");
        return changeRepository.findAllInPrincipalOrganizations(changeMapper.toPage(pageable))
            .map(changeMapper::toDto);
    }
    
    /**
     * Get all the change that Namespace is public
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ChangeDTO> findAllPublicOrganizations(Pageable pageable){
    	log.debug("Request to get all the change of a public Namespace");
    	return changeRepository.findAllPublic(changeMapper.toPage(pageable))
    			.map(changeMapper::toDto);
    }

    /**
     *  Get the "id" change that belongs to an organization where the Principal is a member.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ChangeDTO findOneInPrincipalOrganizations(Long id) {
        log.debug("Request to get Task : {}", id);
        Change change = changeRepository.findOneInPrincipalOrganizations(id);
        return changeMapper.toDto(change);
    }
    
    /**
     * Get the "id" change that belong a public namespace
     * 
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ChangeDTO findOnePublic(Long id){
    	log.debug("Request to get Task : {}", id);
    	Change change = changeRepository.findOnePublic(id);
    	return changeMapper.toDto(change);
    }

    /**
     * Delete all change associated with the task
     * 
     * @param taskId the id of the associate Task
     */
	@Override
	public void deleteAllByTaskId(Long taskId) {
		log.debug("Request to delete all Task associate with taskId : {}",taskId);
		changeRepository.deleteAllByTaskId(taskId);
	}

}
