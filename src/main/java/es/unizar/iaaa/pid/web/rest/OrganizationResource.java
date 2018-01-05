package es.unizar.iaaa.pid.web.rest;

import com.codahale.metrics.annotation.Timed;
import es.unizar.iaaa.pid.domain.enumeration.Capacity;
import es.unizar.iaaa.pid.service.OrganizationDTOService;
import es.unizar.iaaa.pid.service.OrganizationMemberDTOService;
import es.unizar.iaaa.pid.service.dto.OrganizationDTO;
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
 * REST controller for managing Organization.
 */
@RestController
@RequestMapping("/api")
public class OrganizationResource {

    private final Logger log = LoggerFactory.getLogger(OrganizationResource.class);

    private static final String ENTITY_NAME = "organization";

    private final OrganizationDTOService organizationService;
    private final OrganizationMemberDTOService organizationMemberService;

    public OrganizationResource(OrganizationDTOService organizationService,
    		OrganizationMemberDTOService organizationMemberService) {
        this.organizationService = organizationService;
        this.organizationMemberService = organizationMemberService;
    }

    /**
     * POST  /organizations : Create a new organization.
     *
     * @param organizationDTO the DTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new DTO, or with status 400 (Bad Request) if the DTO has already an ID
     */
    @PostMapping("/organizations")
    @Timed
    public ResponseEntity<OrganizationDTO> createOrganization(UriComponentsBuilder uriBuilder, @Valid @RequestBody OrganizationDTO organizationDTO) {
        log.debug("REST request to save Organization : {}", organizationDTO);
        return ControllerUtil
            .with(ENTITY_NAME, uriBuilder.path("/api/organizations/{id}"), organizationService)
            .doPost(organizationDTO);
    }

    /**
     * PUT  /organizations/:id : overwrites the "id" organization.
     *
     * @param organizationDTO the organizationDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated DTO,
     * or with status 404 (Not Found) if the DTO is not found,
     * or with status 403 (Forbidden) if the you are not an administrator of the organization
     */
    @PutMapping("/organizations/{id}")
    @Timed
    public ResponseEntity<OrganizationDTO> updateOrganization(@PathVariable Long id, @Valid @RequestBody OrganizationDTO organizationDTO) {
        log.debug("REST request to update Organization {}: {}", id, organizationDTO);
        return ControllerUtil
            .with(ENTITY_NAME, organizationService)
            .forbidWhen(notAnAdmin(id))
            .doPut(id, organizationDTO);
    }

    /**
     * GET  /organizations : get all the organizations.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of organizations in body
     */
    @GetMapping("/organizations")
    @Timed
    public ResponseEntity<List<OrganizationDTO>> getAllOrganizations(UriComponentsBuilder uriBuilder, @ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Organizations");
        return ControllerUtil
            .with(uriBuilder.path("/api/organizations"), organizationService)
            .doGet(pageable);
    }

    /**
     * GET  /organizations/:id : get the "id" organization.
     *
     * @param id the id of the organizationDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the organizationDTO, or with status 404 (Not Found)
     */
    @GetMapping("/organizations/{id}")
    @Timed
    public ResponseEntity<OrganizationDTO> getOrganization(@PathVariable Long id) {
        log.debug("REST request to get Organization : {}", id);
        return ControllerUtil
            .with(ENTITY_NAME, organizationService)
            .doGet(id);
    }

    /**
     * DELETE  /organizations/:id : delete the "id" organization.
     *
     * @param id the id of the organizationDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/organizations/{id}")
    @Timed
    public ResponseEntity<Void> deleteOrganization(@PathVariable Long id) {
        log.debug("REST request to delete Organization : {}", id);
        return ControllerUtil
            .with(ENTITY_NAME, organizationService)
            .forbidWhen(notAnAdmin(id))
            .doDelete(id);
    }

    private Supplier<Boolean> notAnAdmin(Long id) {
        return () -> {
            OrganizationMemberDTO organizationMember = organizationMemberService.findOneByOrganizationInPrincipal(id);
            return organizationMember == null || organizationMember.getCapacity() != Capacity.ADMIN;
        };
    }
}
