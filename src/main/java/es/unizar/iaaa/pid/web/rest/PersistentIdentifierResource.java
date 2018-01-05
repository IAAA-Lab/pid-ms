package es.unizar.iaaa.pid.web.rest;

import com.codahale.metrics.annotation.Timed;
import es.unizar.iaaa.pid.service.PersistentIdentifierDTOService;
import es.unizar.iaaa.pid.service.dto.PersistentIdentifierDTO;
import es.unizar.iaaa.pid.web.rest.util.ControllerUtil;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;
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
     * @return the ResponseEntity with status 201 (Created) and with body the new DTO, or with status 400 (Bad Request) if the DTO has already an ID
     */
    @PostMapping("/persistent-identifiers")
    @Timed
    public ResponseEntity<PersistentIdentifierDTO> createPersistentIdentifier(UriComponentsBuilder uriBuilder, @Valid @RequestBody PersistentIdentifierDTO persistentIdentifierDTO) throws URISyntaxException {
        log.debug("REST request to save PersistentIdentifier : {}", persistentIdentifierDTO);
        return ControllerUtil
            .with(ENTITY_NAME, uriBuilder.path("/api/persistent-identifiers/{id}"), persistentIdentifierService)
            .doPost(persistentIdentifierDTO);
    }

    /**
     * PUT  /persistent-identifiers : Updates an existing persistentIdentifier.
     *
     * @param persistentIdentifierDTO the persistentIdentifierDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated DTO,
     * or with status 404 (Not Found) if the DTO is not found
     */
    @PutMapping("/persistent-identifiers/{id}")
    @Timed
    public ResponseEntity<PersistentIdentifierDTO> updatePersistentIdentifier(@PathVariable UUID id, @Valid @RequestBody PersistentIdentifierDTO persistentIdentifierDTO) throws URISyntaxException {
        log.debug("REST request to update PersistentIdentifier : {}", persistentIdentifierDTO);
        return ControllerUtil
            .with(ENTITY_NAME, persistentIdentifierService)
            .doPut(id, persistentIdentifierDTO);
    }

    /**
     * GET  /persistent-identifiers : get all the persistentIdentifiers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of persistentIdentifiers in body
     */
    @GetMapping("/persistent-identifiers")
    @Timed
    public ResponseEntity<List<PersistentIdentifierDTO>> getAllPersistentIdentifiers(UriComponentsBuilder uriBuilder, @ApiParam Pageable pageable) {
        log.debug("REST request to get a page of PersistentIdentifiers");
        return ControllerUtil
            .with(uriBuilder.path("/api/persistent-identifiers"), persistentIdentifierService)
            .list(persistentIdentifierService::findAllPublic)
            .listAuthenticated(persistentIdentifierService::findAllPublicOrInPrincipalOrganizations)
            .doGet(pageable);
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
        return ControllerUtil
            .with(persistentIdentifierService)
            .get(persistentIdentifierService::findOnePublic)
            .getAuthenticated(persistentIdentifierService::findOnePublicOrInPrincipalOrganizations)
            .doGet(id);
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
        return ControllerUtil
            .with(ENTITY_NAME, persistentIdentifierService)
            .doDelete(id);
    }
}
