package es.unizar.iaaa.pid.service.impl;

import es.unizar.iaaa.pid.domain.Feature;
import es.unizar.iaaa.pid.domain.Namespace;
import es.unizar.iaaa.pid.repository.FeatureRepository;
import es.unizar.iaaa.pid.service.ChangeDTOService;
import es.unizar.iaaa.pid.service.FeatureDTOService;
import es.unizar.iaaa.pid.service.NamespaceService;
import es.unizar.iaaa.pid.service.PersistentIdentifierDTOService;
import es.unizar.iaaa.pid.service.dto.FeatureDTO;
import es.unizar.iaaa.pid.service.mapper.FeatureMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Service Implementation for managing Feature.
 */
@Service
@Transactional
public class FeatureServiceImpl implements FeatureDTOService {

    private final Logger log = LoggerFactory.getLogger(FeatureServiceImpl.class);

    private final FeatureRepository featureRepository;

    private final FeatureMapper featureMapper;

    private final NamespaceService namespaceService;

    private final ChangeDTOService changeDTOService;

    private final PersistentIdentifierDTOService persistentIdentifierDTOService;

    public FeatureServiceImpl(FeatureRepository featureRepository, FeatureMapper featureMapper,
    		NamespaceService namespaceService, ChangeDTOService changeDTOService,
    		PersistentIdentifierDTOService persistentIdentifierDTOService) {
        this.featureRepository = featureRepository;
        this.featureMapper = featureMapper;
        this.namespaceService = namespaceService;
        this.changeDTOService = changeDTOService;
        this.persistentIdentifierDTOService = persistentIdentifierDTOService;
    }

    /**
     * Save a feature.
     *
     * @param featureDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public FeatureDTO save(FeatureDTO featureDTO) {
        log.debug("Request to save Feature : {}", featureDTO);
        Feature feature = featureMapper.toEntity(featureDTO);
        Namespace namespace = namespaceService.findById(featureDTO.getNamespaceId());
        feature.setNamespace(namespace);
        feature = featureRepository.save(feature);
        return featureMapper.toDto(feature);
    }

    /**
     *  Get all the features.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<FeatureDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FEatures");
        return featureRepository.findAll(featureMapper.toPage(pageable))
            .map(featureMapper::toDto);
    }

    /**
     *  Get the public features.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<FeatureDTO> findPublic(Pageable pageable) {
        log.debug("Request to get all Features whose Namespaces are public");
        return featureRepository.findAllByTheirPublicNamespaceIsTrue(featureMapper.toPage(pageable))
            .map(featureMapper::toDto);
    }

    /**
     *  Get one feature by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public FeatureDTO findOne(Long id) {
        log.debug("Request to get Feature : {}", id);
        Feature namespace = featureRepository.findOne(id);
        return featureMapper.toDto(namespace);
    }

    /**
     *  Get the "id" feature if public.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public FeatureDTO findOnePublic(Long id) {
        log.debug("Request to get a public Feature : {}", id);
        Feature feature = featureRepository.findByIdAndTheirPublicNamespaceIsTrue(id);
        return featureMapper.toDto(feature);
    }

    /**
     *  Delete the  feature by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Feature : {}", id);

        //borro todos los cambios asociados a la Feature
        changeDTOService.deleteAllByFeatureId(id);

        //borro todos los PIDs asociados a la Feature
        persistentIdentifierDTOService.deleteAllByFeatureId(id);

        //borro feature
        featureRepository.delete(id);
    }

    /**
     *  Get all the features whose namespace is public or belongs to organizations where the Principal is a member.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<FeatureDTO> findAllInPrincipalOrganizationsOrPublic(Pageable pageable) {
        log.debug("Request to get all Features whose namespace is public or private to the principal");
        return featureRepository.findAllByTheirNamespacePublicOrBelongsToPrincipalOrganizations(featureMapper.toPage(pageable))
            .map(featureMapper::toDto);
    }

    /**
     *  Get the "id" feature whose namespace ispublic or belongs to an organization where the Principal is a member.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public FeatureDTO findOneInPrincipalOrganizationsOrPublic(Long id) {
        log.debug("Request to get Feature : {}", id);
        Feature feature = featureRepository.findByIdAndTheirNamespacePublicOrBelongsToPrincipalOrganizations(id);
        return featureMapper.toDto(feature);
    }

    /**
     *  Get the "feature" feature
     *
     *  @param featureType of the feature entity
     *  @param schemaPrefix of the feature entity
     *  @return the entity
     */
	@Override
	public FeatureDTO findOneByFeatureTypeAndSchemaPrefix(String featureType, String schemaPrefix) {
		log.debug("Request to get Feature with featureType {} and schemaPrefix {}", featureType, schemaPrefix);
		Feature feature = featureRepository.findByFeatureTypeAndSchemaPrefix(featureType, schemaPrefix);
		return featureMapper.toDto(feature);
	}

	/**
     * Delete all features associate with the Namespace
     *
     * @param idNamespace id of the Namespace
     */
	@Override
    public void deleteAllByNamespaceId(Long namespaceId){
		log.debug("Request to delete all Features associte with Namespace {}", namespaceId);
		List<Feature> featureList = featureRepository.findAllByNamespaceId(namespaceId);
    	for(Feature feature : featureList){
    		featureRepository.delete(feature);
    	}
	}


}
