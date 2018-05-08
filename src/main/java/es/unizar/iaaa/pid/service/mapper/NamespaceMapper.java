package es.unizar.iaaa.pid.service.mapper;

import com.google.common.collect.ImmutableMap;
import es.unizar.iaaa.pid.domain.Namespace;
import es.unizar.iaaa.pid.service.dto.NamespaceDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.Map;

/**
 * Mapper for the entity Namespace and its DTO NamespaceDTO.
 */
@Mapper(componentModel = "spring", uses = {OrganizationMapper.class, })
public interface NamespaceMapper extends EntityMapper <NamespaceDTO, Namespace> {

    @Mappings({
        @Mapping(source = "owner.id", target = "ownerId"),
        @Mapping(source = "owner.title", target = "ownerTitle"),
        @Mapping(source = "source.methodType", target = "methodType"),
        @Mapping(source = "source.sourceType", target = "sourceType"),
        @Mapping(source = "source.endpointLocation", target = "endpointLocation"),
        @Mapping(source = "source.resolverProxyMode", target = "resolverProxyMode"),
        @Mapping(source = "source.maxNumRequest", target = "maxNumRequest"),
        @Mapping(source = "registration.processStatus", target = "processStatus"),
        @Mapping(source = "registration.itemStatus", target = "itemStatus"),
        @Mapping(source = "registration.lastChangeDate", target = "lastChangeDate"),
        @Mapping(source = "registration.registrationDate", target = "registrationDate"),
        @Mapping(source = "registration.lastRevisionDate", target = "lastRevisionDate"),
        @Mapping(source = "registration.nextRenewalDate", target = "nextRenewalDate"),
        @Mapping(source = "registration.annullationDate", target = "annullationDate")
    })
    NamespaceDTO toDto(Namespace namespace);

    @Mappings({
        @Mapping(source = "ownerId", target = "owner.id"),
        @Mapping(source = "methodType", target = "source.methodType"),
        @Mapping(source = "sourceType", target = "source.sourceType"),
        @Mapping(source = "endpointLocation", target = "source.endpointLocation"),
        @Mapping(source = "resolverProxyMode", target = "source.resolverProxyMode"),
        @Mapping(source = "maxNumRequest", target = "source.maxNumRequest"),
        @Mapping(source = "processStatus", target = "registration.processStatus"),
        @Mapping(source = "itemStatus", target = "registration.itemStatus"),
        @Mapping(source = "lastChangeDate", target = "registration.lastChangeDate"),
        @Mapping(source = "registrationDate", target = "registration.registrationDate"),
        @Mapping(source = "lastRevisionDate", target = "registration.lastRevisionDate"),
        @Mapping(source = "nextRenewalDate", target = "registration.nextRenewalDate"),
        @Mapping(source = "annullationDate", target = "registration.annullationDate")
    })
    Namespace toEntity(NamespaceDTO namespaceDTO);

    default Namespace fromId(Long id) {
        if (id == null) {
            return null;
        }
        Namespace namespace = new Namespace();
        namespace.setId(id);
        return namespace;
    }

    Map<String, String> conversions = ImmutableMap.<String, String>builder().
    	put("ownerId","owner.id").
        put("methodType", "source.methodType").
        put("sourceType", "source.sourceType").
        put("endpointLocation", "source.endpointLocation").
        put("resolverProxyMode", "source.resolverProxyMode").
        put("maxNumRequest", "source.maxNumRequest").
        put("processStatus", "registration.processStatus").
        put("itemStatus", "registration.itemStatus").
        put("lastChangeDate", "registration.lastChangeDate").
        put("registrationDate", "registration.registrationDate").
        put("lastRevisionDate", "registration.lastRevisionDate").
        put("nextRenewalDate", "registration.nextRenewalDate").
        put("annullationDate", "registration.annullationDate").
        build();

    @Override
    default Map<String, String> getConversions() {
        return conversions;
    }
}
