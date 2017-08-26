package es.unizar.iaaa.pid.service.mapper;

import es.unizar.iaaa.pid.domain.*;
import es.unizar.iaaa.pid.service.dto.PersistentIdentifierDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity PersistentIdentifier and its DTO PersistentIdentifierDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PersistentIdentifierMapper extends EntityMapper <PersistentIdentifierDTO, PersistentIdentifier> {
    
    
    default PersistentIdentifier fromId(Long id) {
        if (id == null) {
            return null;
        }
        PersistentIdentifier persistentIdentifier = new PersistentIdentifier();
        persistentIdentifier.setId(id);
        return persistentIdentifier;
    }
}
