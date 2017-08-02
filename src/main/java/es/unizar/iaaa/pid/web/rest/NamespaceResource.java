package es.unizar.iaaa.pid.web.rest;

import com.codahale.metrics.annotation.Timed;
import es.unizar.iaaa.pid.service.NamespaceService;
import es.unizar.iaaa.pid.web.rest.util.HeaderUtil;
import es.unizar.iaaa.pid.service.dto.NamespaceDTO;
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
 * REST controller for managing Namespace.
 */
@RestController
@RequestMapping("/api")
public class NamespaceResource {

    private final Logger log = LoggerFactory.getLogger(NamespaceResource.class);

    private static final String ENTITY_NAME = "namespace";

    private final NamespaceService namespaceService;

    public NamespaceResource(NamespaceService namespaceService) {
        this.namespaceService = namespaceService;
    }

    /**
     * POST  /namespaces : Create a new namespace.
     *
     * @param namespaceDTO the namespaceDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new namespaceDTO, or with status 400 (Bad Request) if the namespace has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/namespaces")
    @Timed
    public ResponseEntity<NamespaceDTO> createNamespace(@Valid @RequestBody NamespaceDTO namespaceDTO) throws URISyntaxException {
        log.debug("REST request to save Namespace : {}", namespaceDTO);
        if (namespaceDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new namespace cannot already have an ID")).body(null);
        }
        NamespaceDTO result = namespaceService.save(namespaceDTO);
        return ResponseEntity.created(new URI("/api/namespaces/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /namespaces : Updates an existing namespace.
     *
     * @param namespaceDTO the namespaceDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated namespaceDTO,
     * or with status 400 (Bad Request) if the namespaceDTO is not valid,
     * or with status 500 (Internal Server Error) if the namespaceDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/namespaces")
    @Timed
    public ResponseEntity<NamespaceDTO> updateNamespace(@Valid @RequestBody NamespaceDTO namespaceDTO) throws URISyntaxException {
        log.debug("REST request to update Namespace : {}", namespaceDTO);
        if (namespaceDTO.getId() == null) {
            return createNamespace(namespaceDTO);
        }
        NamespaceDTO result = namespaceService.save(namespaceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, namespaceDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /namespaces : get all the namespaces.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of namespaces in body
     */
    @GetMapping("/namespaces")
    @Timed
    public List<NamespaceDTO> getAllNamespaces() {
        log.debug("REST request to get all Namespaces");
        return namespaceService.findAll();
    }

    /**
     * GET  /namespaces/:id : get the "id" namespace.
     *
     * @param id the id of the namespaceDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the namespaceDTO, or with status 404 (Not Found)
     */
    @GetMapping("/namespaces/{id}")
    @Timed
    public ResponseEntity<NamespaceDTO> getNamespace(@PathVariable Long id) {
        log.debug("REST request to get Namespace : {}", id);
        NamespaceDTO namespaceDTO = namespaceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(namespaceDTO));
    }

    /**
     * DELETE  /namespaces/:id : delete the "id" namespace.
     *
     * @param id the id of the namespaceDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/namespaces/{id}")
    @Timed
    public ResponseEntity<Void> deleteNamespace(@PathVariable Long id) {
        log.debug("REST request to delete Namespace : {}", id);
        namespaceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
