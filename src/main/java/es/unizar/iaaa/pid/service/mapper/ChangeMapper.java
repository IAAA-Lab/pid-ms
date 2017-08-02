package es.unizar.iaaa.pid.service.mapper;

import es.unizar.iaaa.pid.domain.*;
import es.unizar.iaaa.pid.service.dto.ChangeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Change and its DTO ChangeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ChangeMapper extends EntityMapper <ChangeDTO, Change> {
    
    
    default Change fromId(Long id) {
        if (id == null) {
            return null;
        }
        Change change = new Change();
        change.setId(id);
        return change;
    }
}
