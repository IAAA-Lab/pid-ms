package es.unizar.iaaa.pid.web.rest;

import com.codahale.metrics.annotation.Timed;
import es.unizar.iaaa.pid.domain.enumeration.Capacity;
import es.unizar.iaaa.pid.service.FeatureDTOService;
import es.unizar.iaaa.pid.service.NamespaceDTOService;
import es.unizar.iaaa.pid.service.OrganizationMemberDTOService;
import es.unizar.iaaa.pid.service.dto.FeatureDTO;
import es.unizar.iaaa.pid.service.dto.NamespaceDTO;
import es.unizar.iaaa.pid.service.dto.OrganizationMemberDTO;
import es.unizar.iaaa.pid.web.rest.util.ControllerUtil;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;
import java.util.function.Supplier;

/**
 * REST controller for managing Namespace.
 */
@RestController
@RequestMapping("/api")
public class FeatureResource {

    private final Logger log = LoggerFactory.getLogger(FeatureResource.class);

    private static final String ENTITY_NAME = "feature";

    private final FeatureDTOService featureService;
    private final OrganizationMemberDTOService organizationMemberService;
    private final NamespaceDTOService namespaceDTOService;

    public FeatureResource(FeatureDTOService featureService, OrganizationMemberDTOService organizationMemberService,
    		NamespaceDTOService namespaceDTOService) {
        this.featureService = featureService;
        this.organizationMemberService = organizationMemberService;
        this.namespaceDTOService = namespaceDTOService;
    }

    /**
     * POST  /features : Create a new feature.
     *
     * @param featureDTO the featureDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new featureDTO
     * , or with status 400 (Bad Request) if the feature has already an ID
     */
    @PostMapping("/features")
    @Timed
    public ResponseEntity<FeatureDTO> createFeature(UriComponentsBuilder uriBuilder, @Valid @RequestBody FeatureDTO featureDTO) {
        log.debug("REST request to save Feature : {}", featureDTO);
        return ControllerUtil
            .with(ENTITY_NAME, uriBuilder.path("/api/features/{id}"), featureService)
            .badRequestWhen(duplicatedFeature(featureDTO),
                "namespaceExist",
                "Exist other Feature with the same FeatureType and SchemaPrefix")
            .forbidWhen(notAnAdminOrEditor(featureDTO))
            .doPost(featureDTO);
    }

    /**
     * PUT  /features : Updates an existing feature.
     *
     * @param featureDTO the featureDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated featureDTO,
     * or with status 404 (Not Found) if the DTO is not found,
     * or with status 403 (Forbidden) if the you are not an administrator of the organization
     */
    @PutMapping("/features/{id}")
    @Timed
    public ResponseEntity<FeatureDTO> updateFeature(@PathVariable Long id, @Valid @RequestBody FeatureDTO featureDTO) {
        log.debug("REST request to update Feature : {}", featureDTO);
        return ControllerUtil
            .with(ENTITY_NAME, featureService)
            .badRequestWhen(duplicatedFeature(featureDTO),
                "namespaceExist",
                "Exist other Feature with the same FeatureType and SchemaPrefix")
            .forbidWhen(notAnAdminOrEditor(id))
            .doPut(id, featureDTO);
    }

    /**
     * GET  /features : get all the features.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of features in body
     */
    @GetMapping("/features")
    @Timed
    public ResponseEntity<List<FeatureDTO>> getAllFeatures(UriComponentsBuilder uriBuilder, @ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Features");
        return ControllerUtil
            .with(uriBuilder.path("/api/features"), featureService)
            .list(featureService::findPublic)
            .listAuthenticated(featureService::findAllInPrincipalOrganizationsOrPublic)
            .doGet(pageable);
    }

    /**
     * GET  /features/:id : get the "id" feature.
     *
     * @param id the id of the namespaceDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the namespaceDTO, or with status 404 (Not Found)
     */
    @GetMapping("/features/{id}")
    @Timed
    public ResponseEntity<FeatureDTO> getFeature(@PathVariable Long id) {
        log.debug("REST request to get Feature : {}", id);
        return ControllerUtil
            .with(ENTITY_NAME, featureService)
            .get(featureService::findOnePublic)
            .getAuthenticated(featureService::findOneInPrincipalOrganizationsOrPublic)
            .doGet(id);
    }

    /**
     * DELETE  /features/:id : delete the "id" feature.
     *
     * @param id the id of the featureDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/features/{id}")
    @Timed
    public ResponseEntity<Void> deleteFeature(@PathVariable Long id) {
        log.debug("REST request to delete Feature : {}", id);
        return ControllerUtil
            .with(ENTITY_NAME, featureService)
            .forbidWhen(notAnAdminOrEditor(id))
            .doDelete(id);
    }

    private Supplier<Boolean> duplicatedFeature(FeatureDTO featureDTO) {
        return () -> {
            FeatureDTO aux = featureService.findOneByFeatureTypeAndSchemaPrefix(featureDTO.getFeatureType(),featureDTO.getSchemaPrefix());
            return aux != null && aux.getId().longValue() != featureDTO.getId().longValue();
        };
    }

    private Supplier<Boolean> notAnAdminOrEditor(FeatureDTO featureDTO) {
        return () -> {
            NamespaceDTO namespace = namespaceDTOService.findOne(featureDTO.getNamespaceId());
            OrganizationMemberDTO organizationMember = organizationMemberService.findOneByOrganizationInPrincipal(namespace.getOwnerId());
            return organizationMember == null || 
            		(organizationMember.getCapacity() != Capacity.ADMIN && 
            		organizationMember.getCapacity() != Capacity.EDITOR);
        };
    }

    private Supplier<Boolean> notAnAdminOrEditor(Long id) {
        return () -> {
        	FeatureDTO feature = featureService.findOne(id);
        	return notAnAdminOrEditor(feature).get();
        };
    }
}
