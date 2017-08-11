package es.unizar.iaaa.pid.service.mapper;

import es.unizar.iaaa.pid.domain.Change;
import es.unizar.iaaa.pid.service.dto.ChangeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * Mapper for the entity Change and its DTO ChangeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ChangeMapper extends EntityMapper <ChangeDTO, Change> {

    @Mappings({
        @Mapping(source = "resource.resourceType", target = "resourceType"),
        @Mapping(source = "resource.locator", target = "locator")
    })
    ChangeDTO toDto(Change change);

    @Mappings({
        @Mapping(source = "resourceType", target = "resource.resourceType"),
        @Mapping(source = "locator", target = "resource.locator")
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
