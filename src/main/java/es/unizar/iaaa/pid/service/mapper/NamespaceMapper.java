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
        @Mapping(source = "source.srsName", target = "srsName"),
        @Mapping(source = "source.schemaUri", target = "schemaUri"),
        @Mapping(source = "source.schemaUriGML", target = "schemaUriGML"),
        @Mapping(source = "source.schemaUriBase", target = "schemaUriBase"),
        @Mapping(source = "source.schemaPrefix", target = "schemaPrefix"),
        @Mapping(source = "source.featureType", target = "featureType"),
        @Mapping(source = "source.geometryProperty", target = "geometryProperty"),
        @Mapping(source = "source.beginLifespanVersionProperty", target = "beginLifespanVersionProperty"),
        @Mapping(source = "source.featuresThreshold", target = "featuresThreshold"),
        @Mapping(source = "source.resolverProxyMode", target = "resolverProxyMode"),
        @Mapping(source = "source.hitsRequest", target = "hitsRequest"),
        @Mapping(source = "source.factorK", target = "factorK"),
        @Mapping(source = "source.xpath", target = "xpath"),
        @Mapping(source = "source.nameItem", target = "nameItem"),
        @Mapping(source = "source.maxNumRequest", target = "maxNumRequest"),
        @Mapping(source = "source.boundingBox.maxX", target = "maxX"),
        @Mapping(source = "source.boundingBox.maxY", target = "maxY"),
        @Mapping(source = "source.boundingBox.minX", target = "minX"),
        @Mapping(source = "source.boundingBox.minY", target = "minY"),
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
        @Mapping(source = "srsName", target = "source.srsName"),
        @Mapping(source = "schemaUri", target = "source.schemaUri"),
        @Mapping(source = "schemaUriGML", target = "source.schemaUriGML"),
        @Mapping(source = "schemaUriBase", target = "source.schemaUriBase"),
        @Mapping(source = "schemaPrefix", target = "source.schemaPrefix"),
        @Mapping(source = "featureType", target = "source.featureType"),
        @Mapping(source = "geometryProperty", target = "source.geometryProperty"),
        @Mapping(source = "beginLifespanVersionProperty", target = "source.beginLifespanVersionProperty"),
        @Mapping(source = "featuresThreshold", target = "source.featuresThreshold"),
        @Mapping(source = "resolverProxyMode", target = "source.resolverProxyMode"),
        @Mapping(source = "hitsRequest", target = "source.hitsRequest"),
        @Mapping(source = "factorK", target = "source.factorK"),
        @Mapping(source = "xpath", target = "source.xpath"),
        @Mapping(source = "nameItem", target = "source.nameItem"),
        @Mapping(source = "maxNumRequest", target = "source.maxNumRequest"),
        @Mapping(source = "maxX", target = "source.boundingBox.maxX"),
        @Mapping(source = "maxY", target = "source.boundingBox.maxY"),
        @Mapping(source = "minX", target = "source.boundingBox.minX"),
        @Mapping(source = "minY", target = "source.boundingBox.minY"),
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
		put("ownerId", "owner.id").
        put("methodType", "source.methodType").
        put("sourceType", "source.sourceType").
        put("endpointLocation", "source.endpointLocation").
        put("srsName", "source.srsName").
        put("schemaUri", "source.schemaUri").
        put("schemaUriGML", "source.schemaUriGML").
        put("schemaUriBase", "source.schemaUriBase").
        put("schemaPrefix", "source.schemaPrefix").
        put("featureType", "source.featureType").
        put("geometryProperty", "source.geometryProperty").
        put("beginLifespanVersionProperty", "source.beginLifespanVersionProperty").
        put("featuresThreshold", "source.featuresThreshold").
        put("resolverProxyMode", "source.resolverProxyMode").
        put("hitsRequest", "source.hitsRequest").
        put("factorK", "source.factorK").
        put("xpath", "source.xpath").
        put("nameItem", "source.nameItem").
        put("maxNumRequest", "source.maxNumRequest").
        put("maxX", "source.boundingBox.maxX").
        put("maxY", "source.boundingBox.maxY").
        put("minX", "source.boundingBox.minX").
        put("minY", "source.boundingBox.minY").
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
