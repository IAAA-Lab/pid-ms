package es.unizar.iaaa.pid.service.impl;

import es.unizar.iaaa.pid.domain.Feature;
import es.unizar.iaaa.pid.domain.PersistentIdentifier;
import es.unizar.iaaa.pid.repository.PersistentIdentifierRepository;
import es.unizar.iaaa.pid.service.PersistentIdentifierDTOService;
import es.unizar.iaaa.pid.service.dto.PersistentIdentifierDTO;
import es.unizar.iaaa.pid.service.mapper.PersistentIdentifierMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.UUID;


/**
 * Service Implementation for managing PersistentIdentifier.
 */
@Service
@Transactional
public class PersistentIdentifierServiceImpl implements PersistentIdentifierDTOService {

    private final Logger log = LoggerFactory.getLogger(PersistentIdentifierServiceImpl.class);

    private final PersistentIdentifierRepository persistentIdentifierRepository;

    private final PersistentIdentifierMapper persistentIdentifierMapper;

    private final EntityManager entityManager;

    public PersistentIdentifierServiceImpl(PersistentIdentifierRepository persistentIdentifierRepository,
                                           PersistentIdentifierMapper persistentIdentifierMapper,
                                           EntityManager entityManager) {
        this.persistentIdentifierRepository = persistentIdentifierRepository;
        this.persistentIdentifierMapper = persistentIdentifierMapper;
        this.entityManager = entityManager;
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
        persistentIdentifier.autoId();
        persistentIdentifier.setFeature(entityManager.getReference(Feature.class, persistentIdentifierDTO.getFeatureId()));
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
        return persistentIdentifierRepository.findAll(persistentIdentifierMapper.toPage(pageable))
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
    public PersistentIdentifierDTO findOne(UUID id) {
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
    public void delete(UUID id) {
        log.debug("Request to delete PersistentIdentifier : {}", id);
        persistentIdentifierRepository.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PersistentIdentifierDTO> findAllPublicOrInPrincipalOrganizations(Pageable pageable) {
        log.debug("Request to get all PersistentIdentifiers public or in Principal's organization");
        return persistentIdentifierRepository.findAllPublicOrInPrincipalOrganizations(persistentIdentifierMapper.toPage(pageable))
            .map(persistentIdentifierMapper::toDto);
    }

    /**
     *  Get all the persistentIdentifiers public.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PersistentIdentifierDTO> findAllPublic(Pageable pageable){
    	log.debug("Request to get all PersistentIdentifiers public");
    	return persistentIdentifierRepository.findAllPublic(persistentIdentifierMapper.toPage(pageable))
                .map(persistentIdentifierMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public PersistentIdentifierDTO findOnePublicOrInPrincipalOrganizations(UUID id) {
        log.debug("Request to get PersistentIdentifier public or in Principal's organization: {}", id);
        PersistentIdentifier persistentIdentifier = persistentIdentifierRepository.findOnePublicOrInPrincipalOrganizations(id);
        return persistentIdentifierMapper.toDto(persistentIdentifier);
    }

    /**
     *  Get the "id" persistentIdentifier public.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public PersistentIdentifierDTO findOnePublic(UUID id){
    	log.debug("Request to get PersistentIdentifier public: {}", id);
    	PersistentIdentifier persistentIdentifier = persistentIdentifierRepository.findOnePublic(id);
        return persistentIdentifierMapper.toDto(persistentIdentifier);
    }

    /**
     * Delete all persistentIdentifier associated with the namespace
     *
     * @param namespaceId the id of the associate namespace
     */
    @Override
    public void deleteAllByNamespaceId(Long namespaceId){
    	log.debug("Request to delete All PersistentIdentifiers associated with the namespace: {}", namespaceId);
    	persistentIdentifierRepository.deleteAllByNamespaceId(namespaceId);
    }

    /**
     * Delete all persistentIdentifier associated with the feature
     *
     * @param featureId the id of the associate feature
     */
    @Override
    public void deleteAllByFeatureId(Long featureId){
    	log.debug("Request to delte All PersistentIdentifiers associated with the feature: {}", featureId);
    	persistentIdentifierRepository.deleteAllByFeatureId(featureId);
    }
}
