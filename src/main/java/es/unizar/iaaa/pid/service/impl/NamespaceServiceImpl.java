package es.unizar.iaaa.pid.service.impl;

import es.unizar.iaaa.pid.service.NamespaceService;
import es.unizar.iaaa.pid.domain.Namespace;
import es.unizar.iaaa.pid.repository.NamespaceRepository;
import es.unizar.iaaa.pid.service.dto.NamespaceDTO;
import es.unizar.iaaa.pid.service.mapper.NamespaceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Namespace.
 */
@Service
@Transactional
public class NamespaceServiceImpl implements NamespaceService{

    private final Logger log = LoggerFactory.getLogger(NamespaceServiceImpl.class);

    private final NamespaceRepository namespaceRepository;

    private final NamespaceMapper namespaceMapper;

    public NamespaceServiceImpl(NamespaceRepository namespaceRepository, NamespaceMapper namespaceMapper) {
        this.namespaceRepository = namespaceRepository;
        this.namespaceMapper = namespaceMapper;
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
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<NamespaceDTO> findAll() {
        log.debug("Request to get all Namespaces");
        return namespaceRepository.findAll().stream()
            .map(namespaceMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
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
     *  Delete the  namespace by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Namespace : {}", id);
        namespaceRepository.delete(id);
    }
}
