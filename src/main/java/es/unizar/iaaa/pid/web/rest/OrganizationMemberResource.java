package es.unizar.iaaa.pid.web.rest;

import com.codahale.metrics.annotation.Timed;
import es.unizar.iaaa.pid.service.OrganizationMemberService;
import es.unizar.iaaa.pid.web.rest.util.HeaderUtil;
import es.unizar.iaaa.pid.service.dto.OrganizationMemberDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing OrganizationMember.
 */
@RestController
@RequestMapping("/api")
public class OrganizationMemberResource {

    private final Logger log = LoggerFactory.getLogger(OrganizationMemberResource.class);

    private static final String ENTITY_NAME = "organizationMember";

    private final OrganizationMemberService organizationMemberService;

    public OrganizationMemberResource(OrganizationMemberService organizationMemberService) {
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
        OrganizationMemberDTO result = organizationMemberService.save(organizationMemberDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, organizationMemberDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /organization-members : get all the organizationMembers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of organizationMembers in body
     */
    @GetMapping("/organization-members")
    @Timed
    public List<OrganizationMemberDTO> getAllOrganizationMembers() {
        log.debug("REST request to get all OrganizationMembers");
        return organizationMemberService.findAll();
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
        OrganizationMemberDTO organizationMemberDTO = organizationMemberService.findOne(id);
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
        organizationMemberService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
