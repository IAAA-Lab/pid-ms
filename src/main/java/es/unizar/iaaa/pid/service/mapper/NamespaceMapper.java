package es.unizar.iaaa.pid.service.mapper;

import es.unizar.iaaa.pid.domain.*;
import es.unizar.iaaa.pid.service.dto.NamespaceDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Namespace and its DTO NamespaceDTO.
 */
@Mapper(componentModel = "spring", uses = {OrganizationMapper.class, })
public interface NamespaceMapper extends EntityMapper <NamespaceDTO, Namespace> {

    @Mappings({
        @Mapping(source = "owner.id", target = "ownerId"),
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
        @Mapping(source = "ownerId", target = "owner"),
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
}
