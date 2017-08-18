package es.unizar.iaaa.pid.service.mapper;

import es.unizar.iaaa.pid.domain.*;
import es.unizar.iaaa.pid.service.dto.ChangeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Change and its DTO ChangeDTO.
 */
@Mapper(componentModel = "spring", uses = {TaskMapper.class, })
public interface ChangeMapper extends EntityMapper <ChangeDTO, Change> {

    @Mappings({
        @Mapping(source = "resource.resourceType", target = "resourceType"),
        @Mapping(source = "resource.locator", target = "locator"),
        @Mapping(source = "identifier.namespace", target = "namespace"),
        @Mapping(source = "identifier.localId", target = "localId"),
        @Mapping(source = "identifier.versionId", target = "versionId"),
        @Mapping(source = "identifier.beginLifespanVersion", target = "beginLifespanVersion"),
        @Mapping(source = "identifier.endLifespanVersion", target = "endLifespanVersion"),
        @Mapping(source = "identifier.alternateId", target = "alternateId"),
        @Mapping(source = "task.id", target = "taskId")
    })
    ChangeDTO toDto(Change change);

    @Mappings({
        @Mapping(source = "taskId", target = "task"),
        @Mapping(source = "resourceType", target = "resource.resourceType"),
        @Mapping(source = "locator", target = "resource.locator"),
        @Mapping(source = "namespace", target = "identifier.namespace"),
        @Mapping(source = "localId", target = "identifier.localId"),
        @Mapping(source = "versionId", target = "identifier.versionId"),
        @Mapping(source = "beginLifespanVersion", target = "identifier.beginLifespanVersion"),
        @Mapping(source = "endLifespanVersion", target = "identifier.endLifespanVersion"),
        @Mapping(source = "alternateId", target = "identifier.alternateId")
    })
    Change toEntity(ChangeDTO changeDTO);

    default Change fromId(Long id) {
        if (id == null) {
            return null;
        }
        Change change = new Change();
        change.setId(id);
        return change;
    }
}
