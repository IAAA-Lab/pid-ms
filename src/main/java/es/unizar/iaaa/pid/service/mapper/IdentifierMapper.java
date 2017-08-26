package es.unizar.iaaa.pid.service.mapper;

import es.unizar.iaaa.pid.domain.*;
import es.unizar.iaaa.pid.service.dto.IdentifierDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Identifier and its DTO IdentifierDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface IdentifierMapper extends EntityMapper <IdentifierDTO, Identifier> {
    
    
    default Identifier fromId(Long id) {
        if (id == null) {
            return null;
        }
        Identifier identifier = new Identifier();
        identifier.setId(id);
        return identifier;
    }
}
