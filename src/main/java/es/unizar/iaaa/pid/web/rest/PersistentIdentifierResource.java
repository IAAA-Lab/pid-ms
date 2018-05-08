package es.unizar.iaaa.pid.web.rest;

import com.codahale.metrics.annotation.Timed;
import es.unizar.iaaa.pid.config.ApplicationProperties;
import es.unizar.iaaa.pid.domain.enumeration.Capacity;
import es.unizar.iaaa.pid.service.FeatureDTOService;
import es.unizar.iaaa.pid.service.NamespaceDTOService;
import es.unizar.iaaa.pid.service.OrganizationMemberDTOService;
import es.unizar.iaaa.pid.service.PersistentIdentifierDTOService;
import es.unizar.iaaa.pid.service.dto.FeatureDTO;
import es.unizar.iaaa.pid.service.dto.NamespaceDTO;
import es.unizar.iaaa.pid.service.dto.OrganizationMemberDTO;
import es.unizar.iaaa.pid.service.dto.PersistentIdentifierDTO;
import es.unizar.iaaa.pid.web.rest.util.ControllerUtil;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;


/**
 * REST controller for managing PersistentIdentifier.
 */
@RestController
public class PersistentIdentifierResource {

    private final Logger log = LoggerFactory.getLogger(PersistentIdentifierResource.class);

    private static final String ENTITY_NAME = "persistentIdentifier";

    private final PersistentIdentifierDTOService persistentIdentifierService;
    private final FeatureDTOService featureService;
    private final NamespaceDTOService namespaceService;
    private final OrganizationMemberDTOService organizationMemberService;
    private final ApplicationProperties applicationProperties;

    public PersistentIdentifierResource(PersistentIdentifierDTOService persistentIdentifierService,
    		FeatureDTOService featureService, NamespaceDTOService namespaceService,
    		OrganizationMemberDTOService organizationMemberService,
            ApplicationProperties applicationProperties) {
        this.persistentIdentifierService = persistentIdentifierService;
        this.featureService = featureService;
        this.namespaceService = namespaceService;
        this.organizationMemberService = organizationMemberService;
        this.applicationProperties = applicationProperties;
    }

    /**
     * POST  /persistent-identifiers : Create a new persistentIdentifier.
     *
     * @param persistentIdentifierDTO the persistentIdentifierDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new DTO, or with status 400 (Bad Request) if the DTO has already an ID
     */
    @PostMapping("/api/persistent-identifiers")
    @Timed
    public ResponseEntity<PersistentIdentifierDTO> createPersistentIdentifier(UriComponentsBuilder uriBuilder, @Valid @RequestBody PersistentIdentifierDTO persistentIdentifierDTO) {
        log.debug("REST request to save PersistentIdentifier : {}", persistentIdentifierDTO);
        return ControllerUtil
            .with(ENTITY_NAME, uriBuilder.path("/api/persistent-identifiers/{id}"), persistentIdentifierService)
            .forbidWhen(notAdminOrEditor(persistentIdentifierDTO))
            .doPost(persistentIdentifierDTO);
    }

    /**
     * PUT  /persistent-identifiers : Updates an existing persistentIdentifier.
     *
     * @param persistentIdentifierDTO the persistentIdentifierDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated DTO,
     * or with status 404 (Not Found) if the DTO is not found
     */
    @PutMapping("/api/persistent-identifiers/{id}")
    @Timed
    public ResponseEntity<PersistentIdentifierDTO> updatePersistentIdentifier(@PathVariable UUID id, @Valid @RequestBody PersistentIdentifierDTO persistentIdentifierDTO) {
        log.debug("REST request to update PersistentIdentifier : {}", persistentIdentifierDTO);
        return ControllerUtil
            .with(ENTITY_NAME, persistentIdentifierService)
            .forbidWhen(notAdminOrEditor(persistentIdentifierDTO))
            .doPut(id, persistentIdentifierDTO);
    }

    /**
     * GET  /persistent-identifiers : get all the persistentIdentifiers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of persistentIdentifiers in body
     */
    @GetMapping("/api/persistent-identifiers")
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
    @GetMapping("/api/persistent-identifiers/{id}")
    @Timed
    public ResponseEntity<PersistentIdentifierDTO> getPersistentIdentifier(@PathVariable UUID id) {
        log.debug("REST request to get PersistentIdentifier : {}", id);
        return ControllerUtil
            .with(persistentIdentifierService)
            .get(findOnePublic())
            .getAuthenticated(findOnePublicOrInPrincipal())
            .doGet(id);
    }

    private Function<UUID, PersistentIdentifierDTO> findOnePublicOrInPrincipal() {
        return _id -> {
                PersistentIdentifierDTO dto = persistentIdentifierService.findOnePublicOrInPrincipalOrganizations(_id);
                if (dto != null) {
                    setResolverLink(dto);
                }
                return dto;
            };
    }

    private Function<UUID, PersistentIdentifierDTO> findOnePublic() {
        return _id -> {
            PersistentIdentifierDTO dto = persistentIdentifierService.findOnePublic(_id);
            if (dto != null) {
                setResolverLink(dto);
            }
            return dto;
        };
    }

    private void setResolverLink(PersistentIdentifierDTO dto) {
        URI link;

        switch (dto.getResourceType()) {
            case DATASET:
                link = linkTo(methodOn(PersistentIdentifierResolver.class)
                    .resolverMetadatoConjunto(dto.getLocalId(), null)).toUri();
                break;
            case SPATIAL_OBJECT:
            default:
                if (dto.getVersionId() != null) {
                    link = linkTo(methodOn(PersistentIdentifierResolver.class)
                        .resolverObjetoEspacialVersionado(dto.getNamespace(),
                            dto.getLocalId(), dto.getVersionId(), null)).toUri();
                } else {
                    link = linkTo(methodOn(PersistentIdentifierResolver.class)
                        .resolverObjetoEspacialNoVersionado(dto.getNamespace(),
                            dto.getLocalId(), null)).toUri();
                }
        }

        if (applicationProperties.getResolver()!=null &&
            !StringUtils.isEmpty(applicationProperties.getResolver().getMetadataBase())) {
            String base = linkTo(PersistentIdentifierResource.class).toUri().toASCIIString();
            link = URI.create(
                applicationProperties.getResolver().getMetadataBase() +
                    link.toASCIIString().substring(base.length())
            );
        }
        dto.setResolverLink(link);
    }

    /**
     * DELETE  /persistent-identifiers/:id : delete the "id" persistentIdentifier.
     *
     * @param id the id of the persistentIdentifierDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/api/persistent-identifiers/{id}")
    @Timed
    public ResponseEntity<Void> deletePersistentIdentifier(@PathVariable UUID id) {
        log.debug("REST request to delete PersistentIdentifier : {}", id);
        return ControllerUtil
            .with(ENTITY_NAME, persistentIdentifierService)
            .forbidWhen(notAdminOrEditor(id))
            .doDelete(id);
    }

    private Supplier<Boolean> notAdminOrEditor(PersistentIdentifierDTO target) {
        return () -> {
        	FeatureDTO feature = featureService.findOne(target.getFeatureId());
        	NamespaceDTO namespace = namespaceService.findOne(feature.getNamespaceId());
            OrganizationMemberDTO organizationMember = organizationMemberService.findOneByOrganizationInPrincipal(namespace.getOwnerId());
            return organizationMember == null ||
            		(organizationMember.getCapacity() != Capacity.ADMIN &&
            		organizationMember.getCapacity() != Capacity.EDITOR);
        };
    }

    private Supplier<Boolean> notAdminOrEditor(UUID id) {
        return () -> {
        	PersistentIdentifierDTO persistentIdentifier = persistentIdentifierService.findOne(id);
        	return notAdminOrEditor(persistentIdentifier).get();
        };
    }
}
