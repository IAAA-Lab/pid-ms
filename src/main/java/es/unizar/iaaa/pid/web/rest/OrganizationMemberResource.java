package es.unizar.iaaa.pid.web.rest;

import com.codahale.metrics.annotation.Timed;
import es.unizar.iaaa.pid.domain.enumeration.Capacity;
import es.unizar.iaaa.pid.service.OrganizationMemberDTOService;
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
     */
    @PostMapping("/organization-members")
    @Timed
    public ResponseEntity<OrganizationMemberDTO> createOrganizationMember(UriComponentsBuilder uriBuilder, @Valid @RequestBody OrganizationMemberDTO organizationMemberDTO) {
        log.debug("REST request to save OrganizationMember : {}", organizationMemberDTO);
        return ControllerUtil
            .with(ENTITY_NAME, uriBuilder.path("/api/organization-members/{id}"), organizationMemberService)
            .forbidWhen(notAnAdmin(organizationMemberDTO))
            .doPost(organizationMemberDTO);
    }

    /**
     * PUT  /organizations-members/:id : overwrites the "id" organization member.
     *
     * @param organizationMemberDTO the organizationMemberDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated organizationMemberDTO,
     * or with status 404 (Not Found) if the organizationMemberDTO is not valid,
     * or with status 403 (Forbidden) if the organizationMemberDTO couldn't be updated
     */
    @PutMapping("/organization-members/{id}")
    @Timed
    public ResponseEntity<OrganizationMemberDTO> updateOrganizationMember(@PathVariable Long id, @Valid @RequestBody OrganizationMemberDTO organizationMemberDTO) {
        log.debug("REST request to update OrganizationMember : {}", organizationMemberDTO);
        return ControllerUtil
            .with(ENTITY_NAME, organizationMemberService)
            .forbidWhen(notAnAdmin(id))
            .doPut(id, organizationMemberDTO);
    }

    /**
     * GET  /organization-members : get all the organizationMembers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of organizationMembers in body
     */
    @GetMapping("/organization-members")
    @Timed
    public ResponseEntity<List<OrganizationMemberDTO>> getAllOrganizationMembers(UriComponentsBuilder uriBuilder, @ApiParam Pageable pageable) {
        log.debug("REST request to get a page of OrganizationMembers");
        return ControllerUtil
            .with(ENTITY_NAME, uriBuilder.path("/api/organization-members"), organizationMemberService)
            .mustBeAuthenticated()
            .listAuthenticated(organizationMemberService::findAllInPrincipalOrganizations)
            .doGet(pageable);
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
        return ControllerUtil
            .with(ENTITY_NAME, organizationMemberService)
            .mustBeAuthenticated()
            .getAuthenticated(organizationMemberService::findOneInPrincipalOrganizations)
            .doGet(id);
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
        return ControllerUtil
            .with(organizationMemberService)
            .forbidWhen(notAnAdmin(id))
            .doDelete(id);
    }

    private Supplier<Boolean> notAnAdmin(OrganizationMemberDTO target) {
        return () -> {
            OrganizationMemberDTO organizationMember = organizationMemberService.findOneByOrganizationInPrincipal(target.getOrganizationId());
            return organizationMember == null || organizationMember.getCapacity() != Capacity.ADMIN;
        };
    }

    private Supplier<Boolean> notAnAdmin(Long id) {
        return () -> {
            long organizationId = organizationMemberService.findOne(id).getOrganizationId();
            OrganizationMemberDTO organizationMember = organizationMemberService.findOneByOrganizationInPrincipal(organizationId);
            return organizationMember == null || organizationMember.getCapacity() != Capacity.ADMIN;
        };
    }
}
