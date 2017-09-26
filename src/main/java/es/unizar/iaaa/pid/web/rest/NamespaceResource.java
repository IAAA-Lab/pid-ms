package es.unizar.iaaa.pid.web.rest;

import com.codahale.metrics.annotation.Timed;

import es.unizar.iaaa.pid.domain.enumeration.Capacity;
import es.unizar.iaaa.pid.domain.enumeration.ItemStatus;
import es.unizar.iaaa.pid.domain.enumeration.NamespaceStatus;
import es.unizar.iaaa.pid.domain.enumeration.ProcessStatus;
import es.unizar.iaaa.pid.security.SecurityUtils;
import es.unizar.iaaa.pid.service.NamespaceDTOService;
import es.unizar.iaaa.pid.service.OrganizationMemberDTOService;
import es.unizar.iaaa.pid.service.dto.NamespaceDTO;
import es.unizar.iaaa.pid.service.dto.OrganizationMemberDTO;
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
import java.time.Instant;
import java.util.Calendar;
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

    private final NamespaceDTOService namespaceService;
    private final OrganizationMemberDTOService organizationMemberService;

    public NamespaceResource(NamespaceDTOService namespaceService, OrganizationMemberDTOService organizationMemberService) {
        this.namespaceService = namespaceService;
        this.organizationMemberService = organizationMemberService;
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

        //check if user have capacity to add this namespace
        OrganizationMemberDTO organizationMember = organizationMemberService.findOneByOrganizationInPrincipal(namespaceDTO.getOwnerId());
        
        if(organizationMember == null || organizationMember.getCapacity() == Capacity.MEMBER){
        	return ResponseEntity.status(HttpStatus.FORBIDDEN).headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "notCapacityToAddNamespace", 
        			"You must be Admin or Editor of the organization to add a Namespace")).body(null);
        }
        //check if exist other namespace with the same id, if exist, return error
        NamespaceDTO auxNamespace = namespaceService.findOneByNamespace(namespaceDTO.getNamespace());
        if(auxNamespace != null){
        	return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "namespaceExist", "Exist other Namespace with the same Namespace")).body(null);
        }

        //add the rest of the attributes for namespace
        namespaceDTO.setNamespaceStatus(NamespaceStatus.STOP);
        namespaceDTO.setProcessStatus(ProcessStatus.NONE);
        namespaceDTO.setItemStatus(ItemStatus.PENDING_VALIDATION);

        Calendar calendar = Calendar.getInstance();
        Instant instant = calendar.toInstant();

        namespaceDTO.setLastChangeDate(instant);
        namespaceDTO.setRegistrationDate(instant);

//        NamespaceDTO result = namespaceService.save(namespaceDTO);
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
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of namespaces in body
     */
    @GetMapping("/namespaces")
    @Timed
    public ResponseEntity<List<NamespaceDTO>> getAllNamespaces(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Namespaces");

        Page<NamespaceDTO> page;
        if (SecurityUtils.isAuthenticated()) {
            page = namespaceService.findAllInPrincipalOrganizationsOrPublic(pageable);
        } else {
            page = namespaceService.findPublic(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/namespaces");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
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
        NamespaceDTO namespaceDTO;
        if (SecurityUtils.isAuthenticated()) {
            namespaceDTO = namespaceService.findOneInPrincipalOrganizationsOrPublic(id);
        } else {
            namespaceDTO = namespaceService.findOnePublic(id);
        }
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
