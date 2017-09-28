package es.unizar.iaaa.pid.web.rest;

import com.codahale.metrics.annotation.Timed;

import es.unizar.iaaa.pid.security.SecurityUtils;
import es.unizar.iaaa.pid.service.PersistentIdentifierDTOService;
import es.unizar.iaaa.pid.service.dto.PersistentIdentifierDTO;
import es.unizar.iaaa.pid.web.rest.util.HeaderUtil;
import es.unizar.iaaa.pid.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing PersistentIdentifier.
 */
@RestController
@RequestMapping("/api")
public class PersistentIdentifierResource {

    private final Logger log = LoggerFactory.getLogger(PersistentIdentifierResource.class);

    private static final String ENTITY_NAME = "persistentIdentifier";

    private final PersistentIdentifierDTOService persistentIdentifierService;

    public PersistentIdentifierResource(PersistentIdentifierDTOService persistentIdentifierService) {
        this.persistentIdentifierService = persistentIdentifierService;
    }

    /**
     * POST  /persistent-identifiers : Create a new persistentIdentifier.
     *
     * @param persistentIdentifierDTO the persistentIdentifierDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new persistentIdentifierDTO, or with status 400 (Bad Request) if the persistentIdentifier has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/persistent-identifiers")
    @Timed
    public ResponseEntity<PersistentIdentifierDTO> createPersistentIdentifier(@Valid @RequestBody PersistentIdentifierDTO persistentIdentifierDTO) throws URISyntaxException {
        log.debug("REST request to save PersistentIdentifier : {}", persistentIdentifierDTO);
        if (persistentIdentifierDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new persistentIdentifier cannot already have an ID")).body(null);
        }
        PersistentIdentifierDTO result = persistentIdentifierService.save(persistentIdentifierDTO);
        return ResponseEntity.created(new URI("/api/persistent-identifiers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /persistent-identifiers : Updates an existing persistentIdentifier.
     *
     * @param persistentIdentifierDTO the persistentIdentifierDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated persistentIdentifierDTO,
     * or with status 400 (Bad Request) if the persistentIdentifierDTO is not valid,
     * or with status 500 (Internal Server Error) if the persistentIdentifierDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/persistent-identifiers")
    @Timed
    public ResponseEntity<PersistentIdentifierDTO> updatePersistentIdentifier(@Valid @RequestBody PersistentIdentifierDTO persistentIdentifierDTO) throws URISyntaxException {
        log.debug("REST request to update PersistentIdentifier : {}", persistentIdentifierDTO);
        if (persistentIdentifierDTO.getId() == null) {
            return createPersistentIdentifier(persistentIdentifierDTO);
        }
        PersistentIdentifierDTO result = persistentIdentifierService.save(persistentIdentifierDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, persistentIdentifierDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /persistent-identifiers : get all the persistentIdentifiers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of persistentIdentifiers in body
     */
    @GetMapping("/persistent-identifiers")
    @Timed
    public ResponseEntity<List<PersistentIdentifierDTO>> getAllPersistentIdentifiers(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of PersistentIdentifiers");
        
        Page<PersistentIdentifierDTO> page;
        if (SecurityUtils.isAuthenticated()) {
        	page = persistentIdentifierService.findAllPublicOrInPrincipalOrganizations(pageable);
        }
        else{
        	page = persistentIdentifierService.findAllPublic(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/persistent-identifiers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /persistent-identifiers/:id : get the "id" persistentIdentifier.
     *
     * @param id the id of the persistentIdentifierDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the persistentIdentifierDTO, or with status 404 (Not Found)
     */
    @GetMapping("/persistent-identifiers/{id}")
    @Timed
    public ResponseEntity<PersistentIdentifierDTO> getPersistentIdentifier(@PathVariable UUID id) {
        log.debug("REST request to get PersistentIdentifier : {}", id);
        PersistentIdentifierDTO persistentIdentifierDTO;
        if(SecurityUtils.isAuthenticated()){
        	persistentIdentifierDTO = persistentIdentifierService.findOnePublicOrInPrincipalOrganizations(id);
        }
        else{
        	persistentIdentifierDTO = persistentIdentifierService.findOnePublic(id);
        }
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(persistentIdentifierDTO));
    }

    /**
     * DELETE  /persistent-identifiers/:id : delete the "id" persistentIdentifier.
     *
     * @param id the id of the persistentIdentifierDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/persistent-identifiers/{id}")
    @Timed
    public ResponseEntity<Void> deletePersistentIdentifier(@PathVariable UUID id) {
        log.debug("REST request to delete PersistentIdentifier : {}", id);
        persistentIdentifierService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
