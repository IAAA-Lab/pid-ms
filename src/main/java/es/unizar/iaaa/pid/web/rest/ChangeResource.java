package es.unizar.iaaa.pid.web.rest;

import com.codahale.metrics.annotation.Timed;
import es.unizar.iaaa.pid.service.ChangeDTOService;
import es.unizar.iaaa.pid.service.dto.ChangeDTO;
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
     * @return the ResponseEntity with status 201 (Created) and with body the new changeDTO,
     *          or with status 400 (Bad Request) if the change contains an ID
     */
    @PostMapping("/changes")
    @Timed
    public ResponseEntity<ChangeDTO> createChange(UriComponentsBuilder uriBuilder, @Valid @RequestBody ChangeDTO changeDTO) {
        log.debug("REST request to save Change : {}", changeDTO);
        return ControllerUtil
            .with(ENTITY_NAME, uriBuilder.path("/api/changes/{id}"), changeService)
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
            .doDelete(id);
    }
}
