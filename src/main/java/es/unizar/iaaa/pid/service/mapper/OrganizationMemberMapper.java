package es.unizar.iaaa.pid.service.mapper;

import es.unizar.iaaa.pid.domain.*;
import es.unizar.iaaa.pid.service.dto.OrganizationMemberDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity OrganizationMember and its DTO OrganizationMemberDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, OrganizationMapper.class, })
public interface OrganizationMemberMapper extends EntityMapper <OrganizationMemberDTO, OrganizationMember> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    @Mapping(source = "organization.id", target = "organizationId")
    @Mapping(source = "organization.name", target = "organizationName")
    OrganizationMemberDTO toDto(OrganizationMember organizationMember);

    @Mapping(source = "userId", target = "user")
    @Mapping(source = "organizationId", target = "organization")
    OrganizationMember toEntity(OrganizationMemberDTO organizationMemberDTO);
    default OrganizationMember fromId(Long id) {
        if (id == null) {
            return null;
        }
        OrganizationMember organizationMember = new OrganizationMember();
        organizationMember.setId(id);
        return organizationMember;
    }
}
