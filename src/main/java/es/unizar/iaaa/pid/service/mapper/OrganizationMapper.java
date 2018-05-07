package es.unizar.iaaa.pid.service.mapper;

import es.unizar.iaaa.pid.domain.Organization;
import es.unizar.iaaa.pid.service.dto.OrganizationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity Organization and its DTO OrganizationDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface OrganizationMapper extends EntityMapper <OrganizationDTO, Organization> {

    @Mapping(target = "members", ignore = true)
    @Mapping(target = "namespaces", ignore = true)
    Organization toEntity(OrganizationDTO organizationDTO);
    default Organization fromId(Long id) {
        if (id == null) {
            return null;
        }
        Organization organization = new Organization();
        organization.setId(id);
        return organization;
    }
}
