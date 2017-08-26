package es.unizar.iaaa.pid.web.rest;

import com.codahale.metrics.annotation.Timed;
import es.unizar.iaaa.pid.service.IdentifierService;
import es.unizar.iaaa.pid.web.rest.util.HeaderUtil;
import es.unizar.iaaa.pid.service.dto.IdentifierDTO;
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
 * REST controller for managing Identifier.
 */
@RestController
@RequestMapping("/api")
public class IdentifierResource {

    private final Logger log = LoggerFactory.getLogger(IdentifierResource.class);

    private static final String ENTITY_NAME = "identifier";

    private final IdentifierService identifierService;

    public IdentifierResource(IdentifierService identifierService) {
        this.identifierService = identifierService;
    }

    /**
     * POST  /identifiers : Create a new identifier.
     *
     * @param identifierDTO the identifierDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new identifierDTO, or with status 400 (Bad Request) if the identifier has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/identifiers")
    @Timed
    public ResponseEntity<IdentifierDTO> createIdentifier(@Valid @RequestBody IdentifierDTO identifierDTO) throws URISyntaxException {
        log.debug("REST request to save Identifier : {}", identifierDTO);
        if (identifierDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new identifier cannot already have an ID")).body(null);
        }
        IdentifierDTO result = identifierService.save(identifierDTO);
        return ResponseEntity.created(new URI("/api/identifiers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /identifiers : Updates an existing identifier.
     *
     * @param identifierDTO the identifierDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated identifierDTO,
     * or with status 400 (Bad Request) if the identifierDTO is not valid,
     * or with status 500 (Internal Server Error) if the identifierDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/identifiers")
    @Timed
    public ResponseEntity<IdentifierDTO> updateIdentifier(@Valid @RequestBody IdentifierDTO identifierDTO) throws URISyntaxException {
        log.debug("REST request to update Identifier : {}", identifierDTO);
        if (identifierDTO.getId() == null) {
            return createIdentifier(identifierDTO);
        }
        IdentifierDTO result = identifierService.save(identifierDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, identifierDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /identifiers : get all the identifiers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of identifiers in body
     */
    @GetMapping("/identifiers")
    @Timed
    public List<IdentifierDTO> getAllIdentifiers() {
        log.debug("REST request to get all Identifiers");
        return identifierService.findAll();
    }

    /**
     * GET  /identifiers/:id : get the "id" identifier.
     *
     * @param id the id of the identifierDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the identifierDTO, or with status 404 (Not Found)
     */
    @GetMapping("/identifiers/{id}")
    @Timed
    public ResponseEntity<IdentifierDTO> getIdentifier(@PathVariable Long id) {
        log.debug("REST request to get Identifier : {}", id);
        IdentifierDTO identifierDTO = identifierService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(identifierDTO));
    }

    /**
     * DELETE  /identifiers/:id : delete the "id" identifier.
     *
     * @param id the id of the identifierDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/identifiers/{id}")
    @Timed
    public ResponseEntity<Void> deleteIdentifier(@PathVariable Long id) {
        log.debug("REST request to delete Identifier : {}", id);
        identifierService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
