package es.unizar.iaaa.pid.service.mapper;

import es.unizar.iaaa.pid.domain.*;
import es.unizar.iaaa.pid.service.dto.GroupDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Group and its DTO GroupDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface GroupMapper extends EntityMapper <GroupDTO, Group> {
    
    @Mapping(target = "members", ignore = true)
    @Mapping(target = "namespaces", ignore = true)
    Group toEntity(GroupDTO groupDTO); 
    default Group fromId(Long id) {
        if (id == null) {
            return null;
        }
        Group group = new Group();
        group.setId(id);
        return group;
    }
}
