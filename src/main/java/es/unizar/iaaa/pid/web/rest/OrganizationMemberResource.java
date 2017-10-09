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
import es.unizar.iaaa.pid.service.OrganizationMemberDTOService;
import es.unizar.iaaa.pid.service.dto.OrganizationMemberDTO;
import es.unizar.iaaa.pid.web.rest.util.HeaderUtil;
import es.unizar.iaaa.pid.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

/**
 * REST controller for managing OrganizationMember.
 */
@RestController
@RequestMapping("/api")
public class OrganizationMemberResource {

    private final Logger log = LoggerFactory.getLogger(OrganizationMemberResource.class);

    private static final String ENTITY_NAME = "organizationMember";

    private final OrganizationMemberDTOService organizationMemberService;

    public OrganizationMemberResource(OrganizationMemberDTOService organizationMemberService) {
        this.organizationMemberService = organizationMemberService;
    }

    /**
     * POST  /organization-members : Create a new organizationMember.
     *
     * @param organizationMemberDTO the organizationMemberDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new organizationMemberDTO, or with status 400 (Bad Request) if the organizationMember has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/organization-members")
    @Timed
    public ResponseEntity<OrganizationMemberDTO> createOrganizationMember(@Valid @RequestBody OrganizationMemberDTO organizationMemberDTO) throws URISyntaxException {
        log.debug("REST request to save OrganizationMember : {}", organizationMemberDTO);
        if (organizationMemberDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new organizationMember cannot already have an ID")).body(null);
        }
        
        //check if user have capacity to add this namespace
        OrganizationMemberDTO organizationMember = organizationMemberService.findOneByOrganizationInPrincipal(organizationMemberDTO.getOrganizationId());
        
        if(organizationMember == null || organizationMember.getCapacity() != Capacity.ADMIN){
        	return ResponseEntity.status(HttpStatus.FORBIDDEN).headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "notCapacityToAddOrganizationMember", 
        			"You must be Admin in the organization to add a user of it")).body(null);
        }
        
        OrganizationMemberDTO result = organizationMemberService.save(organizationMemberDTO);
        return ResponseEntity.created(new URI("/api/organization-members/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /organization-members : Updates an existing organizationMember.
     *
     * @param organizationMemberDTO the organizationMemberDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated organizationMemberDTO,
     * or with status 400 (Bad Request) if the organizationMemberDTO is not valid,
     * or with status 500 (Internal Server Error) if the organizationMemberDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/organization-members")
    @Timed
    public ResponseEntity<OrganizationMemberDTO> updateOrganizationMember(@Valid @RequestBody OrganizationMemberDTO organizationMemberDTO) throws URISyntaxException {
        log.debug("REST request to update OrganizationMember : {}", organizationMemberDTO);
        if (organizationMemberDTO.getId() == null) {
            return createOrganizationMember(organizationMemberDTO);
        }
        
        //check if user have capacity to edit a organizationMember
        OrganizationMemberDTO organizationMember = organizationMemberService.findOneByOrganizationInPrincipal(organizationMemberDTO.getOrganizationId());
        
        if(organizationMember == null || organizationMember.getCapacity() != Capacity.ADMIN){
        	return ResponseEntity.status(HttpStatus.FORBIDDEN).headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "notCapacityToModifyCapacity", 
        			"You must be Admin the organization to modify the capacity of a user")).body(null);
        }
        OrganizationMemberDTO result = organizationMemberService.save(organizationMemberDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, organizationMemberDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /organization-members : get all the organizationMembers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of organizationMembers in body
     */
    @GetMapping("/organization-members")
    @Timed
    public ResponseEntity<List<OrganizationMemberDTO>> getAllOrganizationMembers(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of OrganizationMembers");
        Page<OrganizationMemberDTO> page;
        if (SecurityUtils.isAuthenticated()) {
        	page = organizationMemberService.findAllInPrincipalOrganizations(pageable);
        }  
        else {
        	return ResponseEntity.status(HttpStatus.FORBIDDEN).headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "notAuthenticated", 
        			"You be authenticated to show OrganizationMembers")).body(null);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/organization-members");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /organization-members/:id : get the "id" organizationMember.
     *
     * @param id the id of the organizationMemberDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the organizationMemberDTO, or with status 404 (Not Found)
     */
    @GetMapping("/organization-members/{id}")
    @Timed
    public ResponseEntity<OrganizationMemberDTO> getOrganizationMember(@PathVariable Long id) {
        log.debug("REST request to get OrganizationMember : {}", id);
        OrganizationMemberDTO organizationMemberDTO;
        
        if (SecurityUtils.isAuthenticated()) {
        	organizationMemberDTO = organizationMemberService.findOneInPrincipalOrganizations(id);
        }  
        else {
        	return ResponseEntity.status(HttpStatus.FORBIDDEN).headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "notAuthenticated", 
        			"You be authenticated to show the properties of an OrganizationMember")).body(null);
        }
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(organizationMemberDTO));
    }

    /**
     * DELETE  /organization-members/:id : delete the "id" organizationMember.
     *
     * @param id the id of the organizationMemberDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/organization-members/{id}")
    @Timed
    public ResponseEntity<Void> deleteOrganizationMember(@PathVariable Long id) {
        log.debug("REST request to delete OrganizationMember : {}", id);
        
        //get the organizacion of the OrganizacionMember
        long organizationId = organizationMemberService.findOne(id).getOrganizationId();
        //check if user have capacity to delete this organizationMember
        OrganizationMemberDTO organizationMember = organizationMemberService.findOneByOrganizationInPrincipal(organizationId);
        
        if(organizationMember == null || organizationMember.getCapacity() != Capacity.ADMIN){
        	return ResponseEntity.status(HttpStatus.FORBIDDEN).headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "notCapacityToDeleteUserOfOrganization", 
        			"You must be Admin of the organization to delete a user of it")).body(null);
        }
        organizationMemberService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
