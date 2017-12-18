package es.unizar.iaaa.pid.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import es.unizar.iaaa.pid.domain.enumeration.Capacity;
import es.unizar.iaaa.pid.security.SecurityUtils;
import es.unizar.iaaa.pid.service.FeatureDTOService;
import es.unizar.iaaa.pid.service.NamespaceDTOService;
import es.unizar.iaaa.pid.service.OrganizationMemberDTOService;
import es.unizar.iaaa.pid.service.dto.FeatureDTO;
import es.unizar.iaaa.pid.service.dto.NamespaceDTO;
import es.unizar.iaaa.pid.service.dto.OrganizationMemberDTO;
import es.unizar.iaaa.pid.web.rest.util.HeaderUtil;
import es.unizar.iaaa.pid.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

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
     * @return the ResponseEntity with status 201 (Created) and with body the new featureDTO, or with status 400 (Bad Request) if the feature has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/features")
    @Timed
    public ResponseEntity<FeatureDTO> createFeature(@Valid @RequestBody FeatureDTO featureDTO) throws URISyntaxException {
        log.debug("REST request to save Feature : {}", featureDTO);

        if (featureDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new feature cannot already have an ID")).body(null);
        }

        //check if user have capacity to add this feature
        //get the namespace of the feature
        NamespaceDTO namespace = namespaceDTOService.findOne(featureDTO.getNamespaceId());
        
        OrganizationMemberDTO organizationMember = organizationMemberService.findOneByOrganizationInPrincipal(namespace.getOwnerId());
        
        if(organizationMember == null || organizationMember.getCapacity() == Capacity.MEMBER){
        	return ResponseEntity.status(HttpStatus.FORBIDDEN).headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "notCapacityToAddFeature", 
        			"You must be Admin or Editor of the organization of its namespace to add a Feature")).body(null);
        }
        
        //check if exist other namespace with the same id, if exist, return error
        FeatureDTO auxFeature = featureService.findOneByFeatureTypeAndSchemaPrefix(featureDTO.getFeatureType(),featureDTO.getSchemaPrefix());
        if(auxFeature != null){
        	return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "featureExist", "Exist other Feature with the same FeaturyType and SchemaPrefix")).body(null);
        }

        FeatureDTO result = featureService.save(featureDTO);
        return ResponseEntity.created(new URI("/api/features/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /features : Updates an existing feature.
     *
     * @param featureDTO the featureDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated featureDTO,
     * or with status 400 (Bad Request) if the featureDTO is not valid,
     * or with status 500 (Internal Server Error) if the namespaceDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/features")
    @Timed
    public ResponseEntity<FeatureDTO> updateNamespace(@Valid @RequestBody FeatureDTO featureDTO) throws URISyntaxException {
        log.debug("REST request to update Feature : {}", featureDTO);
        if (featureDTO.getId() == null) {
            return createFeature(featureDTO);
        }
        
        //get the Feature which exists in the database
        FeatureDTO featureDTOprevious = featureService.findOne(featureDTO.getId());
        if(featureDTOprevious == null){
        	return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "FeatureNotExist", 
        			"The Feature which want to be modified does not exist")).body(null);
        }
        
        //check if the user have capacity to modify the namespace
        //get the namespace of the feature
        NamespaceDTO namespace = namespaceDTOService.findOne(featureDTO.getNamespaceId());
        OrganizationMemberDTO organizationMember = organizationMemberService.findOneByOrganizationInPrincipal(namespace.getOwnerId());
        
        if(organizationMember == null || organizationMember.getCapacity() == Capacity.MEMBER){
        	return ResponseEntity.status(HttpStatus.FORBIDDEN).headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "notCapacityToModifyFeature", 
        			"You must be Admin or Editor of the organization of its namespace to modify a Feature")).body(null);
        }
        
        FeatureDTO result = featureService.save(featureDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, featureDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /features : get all the features.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of features in body
     */
    @GetMapping("/features")
    @Timed
    public ResponseEntity<List<FeatureDTO>> getAllFeatures(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Features");

        Page<FeatureDTO> page;
        if (SecurityUtils.isAuthenticated()) {
            page = featureService.findAllInPrincipalOrganizationsOrPublic(pageable);
        } else {
            page = featureService.findPublic(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/features");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
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
        FeatureDTO featureDTO;
        if (SecurityUtils.isAuthenticated()) {
        	featureDTO = featureService.findOneInPrincipalOrganizationsOrPublic(id);
        } else {
        	featureDTO = featureService.findOnePublic(id);
        }
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(featureDTO));
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
        
        //get the Feature which exists in the database
        FeatureDTO featureDTO = featureService.findOne(id);
        if(featureDTO == null){
        	return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "FeatureNotExist", 
        			"The Feature which want to be deleted does not exist")).body(null);
        }
        
        //check if the user have capacity to modify the namespace
        //get the namespace of the feature
        NamespaceDTO namespace = namespaceDTOService.findOne(featureDTO.getNamespaceId());
        OrganizationMemberDTO organizationMember = organizationMemberService.findOneByOrganizationInPrincipal(namespace.getOwnerId());
        
        if(organizationMember == null || organizationMember.getCapacity() == Capacity.MEMBER){
        	return ResponseEntity.status(HttpStatus.FORBIDDEN).headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "notCapacityToDeleteFeature", 
        			"You must be Admin or Editor of the organization of its namespace to delete a Feature")).body(null);
        }
        
        featureService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
