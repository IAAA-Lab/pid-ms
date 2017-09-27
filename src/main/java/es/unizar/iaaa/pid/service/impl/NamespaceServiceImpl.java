package es.unizar.iaaa.pid.service.impl;

import es.unizar.iaaa.pid.service.NamespaceDTOService;
import es.unizar.iaaa.pid.service.PersistentIdentifierDTOService;
import es.unizar.iaaa.pid.service.TaskDTOService;
import es.unizar.iaaa.pid.domain.Namespace;
import es.unizar.iaaa.pid.repository.NamespaceRepository;
import es.unizar.iaaa.pid.service.dto.NamespaceDTO;
import es.unizar.iaaa.pid.service.mapper.NamespaceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Namespace.
 */
@Service
@Transactional
public class NamespaceServiceImpl implements NamespaceDTOService {

    private final Logger log = LoggerFactory.getLogger(NamespaceServiceImpl.class);

    private final NamespaceRepository namespaceRepository;
    
    private final TaskDTOService taskDTOService;
    
    private final PersistentIdentifierDTOService persistentIdentifierDTOService;

    private final NamespaceMapper namespaceMapper;

    public NamespaceServiceImpl(NamespaceRepository namespaceRepository, NamespaceMapper namespaceMapper,
    		TaskDTOService taskDTOService, PersistentIdentifierDTOService persistentIdentifierDTOService) {
        this.namespaceRepository = namespaceRepository;
        this.namespaceMapper = namespaceMapper;
        this.taskDTOService = taskDTOService;
        this.persistentIdentifierDTOService = persistentIdentifierDTOService;
    }

    /**
     * Save a namespace.
     *
     * @param namespaceDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public NamespaceDTO save(NamespaceDTO namespaceDTO) {
        log.debug("Request to save Namespace : {}", namespaceDTO);
        Namespace namespace = namespaceMapper.toEntity(namespaceDTO);
        namespace = namespaceRepository.save(namespace);
        return namespaceMapper.toDto(namespace);
    }

    /**
     *  Get all the namespaces.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<NamespaceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Namespaces");
        return namespaceRepository.findAll(namespaceMapper.toPage(pageable))
            .map(namespaceMapper::toDto);
    }

    /**
     *  Get the public namespaces.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<NamespaceDTO> findPublic(Pageable pageable) {
        log.debug("Request to get all Namespaces");
        return namespaceRepository.findAllByPublicNamespaceIsTrue(namespaceMapper.toPage(pageable))
            .map(namespaceMapper::toDto);
    }

    /**
     *  Get one namespace by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public NamespaceDTO findOne(Long id) {
        log.debug("Request to get Namespace : {}", id);
        Namespace namespace = namespaceRepository.findOne(id);
        return namespaceMapper.toDto(namespace);
    }

    /**
     *  Get the "id" namespace if public.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public NamespaceDTO findOnePublic(Long id) {
        log.debug("Request to get Namespace : {}", id);
        Namespace namespace = namespaceRepository.findByIdAndPublicNamespaceIsTrue(id);
        return namespaceMapper.toDto(namespace);
    }

    /**
     *  Delete the  namespace by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Namespace : {}", id);
        
        //delete the task asociate to the Namespace
        taskDTOService.deleteAllByNamespaceId(id);
        
        //delete all persistentIdentifiers associated to the Namespace
        persistentIdentifierDTOService.deleteAllByNamespaceId(id);
        namespaceRepository.delete(id);
    }

    /**
     *  Get the "namespace" namespace.
     *
     *  @param namespace the namespace of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
	public NamespaceDTO findOneByNamespace(String namespace) {
		log.debug("Request to get Namespace: {}", namespace);
		Namespace namespaceObject  = namespaceRepository.findOneByNamespace(namespace);
		return namespaceMapper.toDto(namespaceObject);
	}

    /**
     *  Get all the nemespaces that are public or belongs to organizations where the Principal is a member.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<NamespaceDTO> findAllInPrincipalOrganizationsOrPublic(Pageable pageable) {
        log.debug("Request to get all public Namespaces or private to the principal");
        return namespaceRepository.findAllPublicOrBelongsToPrincipalOrganizations(namespaceMapper.toPage(pageable))
            .map(namespaceMapper::toDto);
    }

    /**
     *  Get the "id" namepspace that is public or belongs to an organization where the Principal is a member.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public NamespaceDTO findOneInPrincipalOrganizationsOrPublic(Long id) {
        log.debug("Request to get Namespace : {}", id);
        Namespace namespace = namespaceRepository.findByIdAndPublicOrBelongsToPrincipalOrganizations(id);
        return namespaceMapper.toDto(namespace);
    }

}
