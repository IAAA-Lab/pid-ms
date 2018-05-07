package es.unizar.iaaa.pid.web.rest;

import com.codahale.metrics.annotation.Timed;
import es.unizar.iaaa.pid.domain.enumeration.Capacity;
import es.unizar.iaaa.pid.domain.enumeration.ItemStatus;
import es.unizar.iaaa.pid.domain.enumeration.NamespaceStatus;
import es.unizar.iaaa.pid.domain.enumeration.ProcessStatus;
import es.unizar.iaaa.pid.service.NamespaceDTOService;
import es.unizar.iaaa.pid.service.OrganizationMemberDTOService;
import es.unizar.iaaa.pid.service.dto.NamespaceDTO;
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
import java.time.Instant;
import java.util.Calendar;
import java.util.List;
import java.util.function.Supplier;

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


    public NamespaceResource(NamespaceDTOService namespaceService,
    		     		OrganizationMemberDTOService organizationMemberService) {
    	this.namespaceService = namespaceService;
	    this.organizationMemberService = organizationMemberService;
	}

    /**
     * POST  /namespaces : Create a new namespace.
     *
     * @param namespaceDTO the namespaceDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new DTO,
     * with status 400 (Bad Request) if the DTO has already an ID,
     * or with status 404 if yo are not an administrator or editor
     */
    @PostMapping("/namespaces")
    @Timed
    public ResponseEntity<NamespaceDTO> createNamespace(UriComponentsBuilder uriBuilder,  @Valid @RequestBody NamespaceDTO namespaceDTO) {
        log.debug("REST request to save Namespace : {}", namespaceDTO);
        return ControllerUtil
            .with(ENTITY_NAME, uriBuilder.path("/api/namespaces/{id}"), namespaceService)
            .customise( dto -> {
                dto.setNamespaceStatus(NamespaceStatus.STOP);
                dto.setProcessStatus(ProcessStatus.NONE);
                dto.setItemStatus(ItemStatus.PENDING_VALIDATION);
                Calendar calendar = Calendar.getInstance();
                Instant instant = calendar.toInstant();
                dto.setLastChangeDate(instant);
                dto.setRegistrationDate(instant);
            })
            .badRequestWhen(duplicatedNamespace(namespaceDTO),
                "namespaceExist",
                "Exist other Namespace with the same Namespace")
            .forbidWhen(notAnAdminOrEditor(namespaceDTO))
            .doPost(namespaceDTO);
    }

    private Supplier<Boolean> duplicatedNamespace(@Valid @RequestBody NamespaceDTO namespaceDTO) {
        return ()-> namespaceService.findOneByNamespace(namespaceDTO.getNamespace()) != null;
    }

    /**
     * PUT  /namespaces : Updates an existing namespace.
     *
     * @param namespaceDTO the namespaceDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated namespaceDTO,
     * or with status 404 (Not Found) if the DTO is not found,
     * or with status 403 (Forbidden) if the you are not an administrator of the organization
     */
    @PutMapping("/namespaces/{id}")
    @Timed
    public ResponseEntity<NamespaceDTO> updateNamespace(@PathVariable Long id, @Valid @RequestBody NamespaceDTO namespaceDTO) {
        log.debug("REST request to update Namespace : {}", namespaceDTO);
        return ControllerUtil
            .with(ENTITY_NAME, namespaceService)
            .badRequestWhen(duplicatedNamespace(namespaceDTO),
                "namespaceExist",
                "Exist other Namespace with the same Namespace")
            .forbidWhen(notAnAdminOrEditor(id))
            .doPut(id, namespaceDTO);
    }

    /**
     * GET  /namespaces : get all the namespaces.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of namespaces in body
     */
    @GetMapping("/namespaces")
    @Timed
    public ResponseEntity<List<NamespaceDTO>> getAllNamespaces(UriComponentsBuilder uriBuilder, @ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Namespaces");
        return ControllerUtil
            .with(uriBuilder.path("/api/namespaces"), namespaceService)
            .list(namespaceService::findPublic)
            .listAuthenticated(namespaceService::findAllInPrincipalOrganizationsOrPublic)
            .doGet(pageable);
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
        return ControllerUtil
            .with(ENTITY_NAME, namespaceService)
            .get(namespaceService::findOnePublic)
            .getAuthenticated(namespaceService::findOneInPrincipalOrganizationsOrPublic)
            .doGet(id);
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
        return ControllerUtil
            .with(ENTITY_NAME, namespaceService)
            .forbidWhen(notAnAdminOrEditor(id))
            .doDelete(id);
    }

    private Supplier<Boolean> notAnAdminOrEditor(NamespaceDTO namespaceDTO) {
        return () -> {
            OrganizationMemberDTO organizationMember = organizationMemberService.findOneByOrganizationInPrincipal(namespaceDTO.getOwnerId());
            return organizationMember == null || organizationMember.getCapacity() == Capacity.MEMBER;
        };
    }


    private Supplier<Boolean> notAnAdminOrEditor(Long id) {
        return () -> {
            NamespaceDTO namespaceDTO = namespaceService.findOne(id);
            OrganizationMemberDTO organizationMember = organizationMemberService.findOneByOrganizationInPrincipal(namespaceDTO.getOwnerId());
            return organizationMember == null || organizationMember.getCapacity() == Capacity.MEMBER;
        };
    }
}
