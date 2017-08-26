package es.unizar.iaaa.pid.service.mapper;

import es.unizar.iaaa.pid.domain.*;
import es.unizar.iaaa.pid.service.dto.NamespaceDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Namespace and its DTO NamespaceDTO.
 */
@Mapper(componentModel = "spring", uses = {OrganizationMapper.class, })
public interface NamespaceMapper extends EntityMapper <NamespaceDTO, Namespace> {

    @Mapping(source = "owner.id", target = "ownerId")
    NamespaceDTO toDto(Namespace namespace); 

    @Mapping(source = "ownerId", target = "owner")
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
