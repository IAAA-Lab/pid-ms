package es.unizar.iaaa.pid.web.rest;

import java.util.List;
import java.util.function.Supplier;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.codahale.metrics.annotation.Timed;

import es.unizar.iaaa.pid.domain.enumeration.Capacity;
import es.unizar.iaaa.pid.service.ChangeDTOService;
import es.unizar.iaaa.pid.service.FeatureDTOService;
import es.unizar.iaaa.pid.service.NamespaceDTOService;
import es.unizar.iaaa.pid.service.OrganizationMemberDTOService;
import es.unizar.iaaa.pid.service.dto.ChangeDTO;
import es.unizar.iaaa.pid.service.dto.FeatureDTO;
import es.unizar.iaaa.pid.service.dto.NamespaceDTO;
import es.unizar.iaaa.pid.service.dto.OrganizationMemberDTO;
import es.unizar.iaaa.pid.web.rest.util.ControllerUtil;
import io.swagger.annotations.ApiParam;

/**
 * REST controller for managing Change.
 */
@RestController
@RequestMapping("/api")
public class ChangeResource {

    private final Logger log = LoggerFactory.getLogger(ChangeResource.class);

    private static final String ENTITY_NAME = "change";

    private final ChangeDTOService changeService;
    private final FeatureDTOService featureService;
    private final NamespaceDTOService namespaceService;
    private final OrganizationMemberDTOService organizationMemberService;

    public ChangeResource(ChangeDTOService changeService, FeatureDTOService featureService, 
    		NamespaceDTOService namespaceService, OrganizationMemberDTOService organizationMemberService) {
        this.changeService = changeService;
        this.featureService = featureService;
        this.namespaceService = namespaceService;
        this.organizationMemberService = organizationMemberService;
    }

    /**
     * POST  /changes : Create a new change.
     *
     * @param changeDTO the changeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new changeDTO,
     *          or with status 400 (Bad Request) if the change contains an ID
     */
    @PostMapping("/changes")
    @Timed
    public ResponseEntity<ChangeDTO> createChange(UriComponentsBuilder uriBuilder, @Valid @RequestBody ChangeDTO changeDTO) {
        log.debug("REST request to save Change : {}", changeDTO);
        return ControllerUtil
            .with(ENTITY_NAME, uriBuilder.path("/api/changes/{id}"), changeService)
            .forbidWhen(notAdminOrEditor(changeDTO))
            .doPost(changeDTO);
    }

    /**
     * PUT  /changes/:id : modify the "id" chan ge.
     *
     * @param changeDTO the changeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated changeDTO,
     * or with status 404 (Not Found) if the changeDTO is not valid,
     */
    @PutMapping("/changes/{id}")
    @Timed
    public ResponseEntity<ChangeDTO> updateChange(@PathVariable Long id, @Valid @RequestBody ChangeDTO changeDTO) {
        log.debug("REST request to update Change : {}", changeDTO);
        return ControllerUtil
            .with(ENTITY_NAME, changeService)
            .forbidWhen(notAdminOrEditor(id))
            .doPut(id, changeDTO);
    }

    /**
     * GET  /changes : get all the changes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of changes in body
     */
    @GetMapping("/changes")
    @Timed
    public ResponseEntity<List<ChangeDTO>> getAllChanges(UriComponentsBuilder uriBuilder, @ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Changes");
        return ControllerUtil
            .with(uriBuilder.path("/api/changes"), changeService)
            .list(changeService::findAllPublicOrganizations)
            .listAuthenticated(changeService::findAllInPrincipalOrganizations)
            .doGet(pageable);
    }

    /**
     * GET  /changes/:id : get the "id" change.
     *
     * @param id the id of the changeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the changeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/changes/{id}")
    @Timed
    public ResponseEntity<ChangeDTO> getChange(@PathVariable Long id) {
        log.debug("REST request to get Change : {}", id);
        return ControllerUtil
            .with(changeService)
            .get(changeService::findOnePublic)
            .getAuthenticated(changeService::findOneInPrincipalOrganizations)
            .doGet(id);
    }

    /**
     * DELETE  /changes/:id : delete the "id" change.
     *
     * @param id the id of the changeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/changes/{id}")
    @Timed
    public ResponseEntity<Void> deleteChange(@PathVariable Long id) {
        log.debug("REST request to delete Change : {}", id);
        return ControllerUtil
            .with(ENTITY_NAME, changeService)
            .forbidWhen(notAdminOrEditor(id))
            .doDelete(id);
    }
    
    private Supplier<Boolean> notAdminOrEditor(ChangeDTO target) {
        return () -> {
        	FeatureDTO feature = featureService.findOne(target.getFeatureId());
        	NamespaceDTO namespace = namespaceService.findOne(feature.getNamespaceId());
            OrganizationMemberDTO organizationMember = organizationMemberService.findOneByOrganizationInPrincipal(namespace.getOwnerId());
            return organizationMember == null || 
            		(organizationMember.getCapacity() != Capacity.ADMIN && 
            		organizationMember.getCapacity() != Capacity.EDITOR);
        };
    }
    
    private Supplier<Boolean> notAdminOrEditor(Long id) {
        return () -> {
        	ChangeDTO change = changeService.findOne(id);
        	return notAdminOrEditor(change).get();
        };
    }
}
