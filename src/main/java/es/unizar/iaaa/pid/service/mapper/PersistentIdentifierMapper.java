package es.unizar.iaaa.pid.service.mapper;

import com.google.common.collect.ImmutableMap;
import es.unizar.iaaa.pid.domain.PersistentIdentifier;
import es.unizar.iaaa.pid.service.dto.PersistentIdentifierDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.Map;
import java.util.UUID;

/**
 * Mapper for the entity PersistentIdentifier and its DTO PersistentIdentifierDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PersistentIdentifierMapper extends EntityMapper <PersistentIdentifierDTO, PersistentIdentifier> {

    @Mappings({
        @Mapping(source = "resource.resourceType", target = "resourceType"),
        @Mapping(source = "resource.locator", target = "locator"),
        @Mapping(source = "identifier.namespace", target = "namespace"),
        @Mapping(source = "identifier.localId", target = "localId"),
        @Mapping(source = "identifier.versionId", target = "versionId"),
        @Mapping(source = "identifier.beginLifespanVersion", target = "beginLifespanVersion"),
        @Mapping(source = "identifier.endLifespanVersion", target = "endLifespanVersion"),
        @Mapping(source = "identifier.alternateId", target = "alternateId"),
        @Mapping(source = "registration.processStatus", target = "processStatus"),
        @Mapping(source = "registration.itemStatus", target = "itemStatus"),
        @Mapping(source = "registration.lastChangeDate", target = "lastChangeDate"),
        @Mapping(source = "registration.registrationDate", target = "registrationDate"),
        @Mapping(source = "registration.lastRevisionDate", target = "lastRevisionDate"),
        @Mapping(source = "registration.nextRenewalDate", target = "nextRenewalDate"),
        @Mapping(source = "registration.annullationDate", target = "annullationDate"),
        @Mapping(source = "feature.id", target = "featureId")
    })
    PersistentIdentifierDTO toDto(PersistentIdentifier persistentIdentifier);

    @Mappings({
        @Mapping(source = "resourceType", target = "resource.resourceType"),
        @Mapping(source = "locator", target = "resource.locator"),
        @Mapping(source = "namespace", target = "identifier.namespace"),
        @Mapping(source = "localId", target = "identifier.localId"),
        @Mapping(source = "versionId", target = "identifier.versionId"),
        @Mapping(source = "beginLifespanVersion", target = "identifier.beginLifespanVersion"),
        @Mapping(source = "endLifespanVersion", target = "identifier.endLifespanVersion"),
        @Mapping(source = "alternateId", target = "identifier.alternateId"),
        @Mapping(source = "processStatus", target = "registration.processStatus"),
        @Mapping(source = "itemStatus", target = "registration.itemStatus"),
        @Mapping(source = "lastChangeDate", target = "registration.lastChangeDate"),
        @Mapping(source = "registrationDate", target = "registration.registrationDate"),
        @Mapping(source = "lastRevisionDate", target = "registration.lastRevisionDate"),
        @Mapping(source = "nextRenewalDate", target = "registration.nextRenewalDate"),
        @Mapping(source = "annullationDate", target = "registration.annullationDate"),
        @Mapping(source = "featureId", target = "feature.id"),
    })
    PersistentIdentifier toEntity(PersistentIdentifierDTO persistentIdentifierDTO);

    default PersistentIdentifier fromId(UUID id) {
        if (id == null) {
            return null;
        }
        PersistentIdentifier persistentIdentifier = new PersistentIdentifier();
        persistentIdentifier.setId(id);
        return persistentIdentifier;
    }

    Map<String, String> conversions = ImmutableMap.<String, String>builder().
        put("resourceType", "resource.resourceType").
        put("locator", "resource.locator").
        put("namespace", "identifier.namespace").
        put("localId", "identifier.localId").
        put("versionId", "identifier.versionId").
        put("beginLifespanVersion", "identifier.beginLifespanVersion").
        put("endLifespanVersion", "identifier.endLifespanVersion").
        put("alternateId", "identifier.alternateId").
        put("processStatus", "registration.processStatus").
        put("itemStatus", "registration.itemStatus").
        put("lastChangeDate", "registration.lastChangeDate").
        put("registrationDate", "registration.registrationDate").
        put("lastRevisionDate", "registration.lastRevisionDate").
        put("nextRenewalDate", "registration.nextRenewalDate").
        put("annullationDate", "registration.annullationDate").
        put("featureId", "feature.id").
        build();

    @Override
    default Map<String, String> getConversions() {
        return conversions;
    }
}
