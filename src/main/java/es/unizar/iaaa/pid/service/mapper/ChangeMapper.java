package es.unizar.iaaa.pid.service.mapper;

import es.unizar.iaaa.pid.domain.*;
import es.unizar.iaaa.pid.service.dto.ChangeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Change and its DTO ChangeDTO.
 */
@Mapper(componentModel = "spring", uses = {TaskMapper.class, })
public interface ChangeMapper extends EntityMapper <ChangeDTO, Change> {

    @Mapping(source = "task.id", target = "taskId")
    ChangeDTO toDto(Change change); 

    @Mapping(source = "taskId", target = "task")
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
