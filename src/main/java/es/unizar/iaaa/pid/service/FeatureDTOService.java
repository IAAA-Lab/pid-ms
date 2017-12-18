package es.unizar.iaaa.pid.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import es.unizar.iaaa.pid.service.dto.FeatureDTO;

/**
 * Service Interface for managing Feature.
 */
public interface FeatureDTOService {

    /**
     * Save a feature.
     *
     * @param featureDTO the entity to save
     * @return the persisted entity
     */
    FeatureDTO save(FeatureDTO featureDTO);

    /**
     *  Get all the features.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<FeatureDTO> findAll(Pageable pageable);

    /**
     *  Get the public features.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<FeatureDTO> findPublic(Pageable pageable);

    /**
     *  Get the "id" feature.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    FeatureDTO findOne(Long id);

    /**
     *  Get the "id" feature if public.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    FeatureDTO findOnePublic(Long id);

    /**
     *  Delete the "id" feature.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     *  Get the "feature" feature
     *
     *  @param featureType of the feature entity
     *  @param schemaPrefix of the feature entity
     *  @return the entity
     */
    FeatureDTO findOneByFeatureTypeAndSchemaPrefix(String featureType, String schemaPrefix);

    /**
     *  Get all the feature that belong to a public Namespace or a Namespace which belongs to organizations where the Principal is a member.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<FeatureDTO> findAllInPrincipalOrganizationsOrPublic(Pageable pageable);

    /**
     *  Get the "id" feature that belong to a public Namespace or a Namespace which belongs to organizations where the Principal is a member.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    FeatureDTO findOneInPrincipalOrganizationsOrPublic(Long id);
    
    /**
     * Delete all features associate with the Namespace
     * 
     * @param idNamespace id of the Namespace
     */
    void deleteAllByNamespaceId(Long namespaceId);
    
}
