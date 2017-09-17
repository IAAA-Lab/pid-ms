package es.unizar.iaaa.pid.web.rest;

import com.codahale.metrics.annotation.Timed;
import es.unizar.iaaa.pid.service.ChangeDTOService;
import es.unizar.iaaa.pid.service.dto.ChangeDTO;
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

/**
 * REST controller for managing Change.
 */
@RestController
@RequestMapping("/api")
public class ChangeResource {

    private final Logger log = LoggerFactory.getLogger(ChangeResource.class);

    private static final String ENTITY_NAME = "change";

    private final ChangeDTOService changeService;

    public ChangeResource(ChangeDTOService changeService) {
        this.changeService = changeService;
    }

    /**
     * POST  /changes : Create a new change.
     *
     * @param changeDTO the changeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new changeDTO, or with status 400 (Bad Request) if the change has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/changes")
    @Timed
    public ResponseEntity<ChangeDTO> createChange(@Valid @RequestBody ChangeDTO changeDTO) throws URISyntaxException {
        log.debug("REST request to save Change : {}", changeDTO);
        if (changeDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new change cannot already have an ID")).body(null);
        }
        ChangeDTO result = changeService.save(changeDTO);
        return ResponseEntity.created(new URI("/api/changes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /changes : Updates an existing change.
     *
     * @param changeDTO the changeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated changeDTO,
     * or with status 400 (Bad Request) if the changeDTO is not valid,
     * or with status 500 (Internal Server Error) if the changeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/changes")
    @Timed
    public ResponseEntity<ChangeDTO> updateChange(@Valid @RequestBody ChangeDTO changeDTO) throws URISyntaxException {
        log.debug("REST request to update Change : {}", changeDTO);
        if (changeDTO.getId() == null) {
            return createChange(changeDTO);
        }
        ChangeDTO result = changeService.save(changeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, changeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /changes : get all the changes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of changes in body
     */
    @GetMapping("/changes")
    @Timed
    public ResponseEntity<List<ChangeDTO>> getAllChanges(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Changes");
        Page<ChangeDTO> page = changeService.findAllInPrincipalOrganizations(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/changes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
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
        ChangeDTO changeDTO = changeService.findOneInPrincipalOrganizations(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(changeDTO));
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
        changeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
