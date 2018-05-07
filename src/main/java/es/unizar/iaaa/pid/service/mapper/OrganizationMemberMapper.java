package es.unizar.iaaa.pid.service.mapper;

import com.google.common.collect.ImmutableMap;
import es.unizar.iaaa.pid.domain.OrganizationMember;
import es.unizar.iaaa.pid.service.dto.OrganizationMemberDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Map;

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

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "organizationId", target = "organization.id")
    OrganizationMember toEntity(OrganizationMemberDTO organizationMemberDTO);
    default OrganizationMember fromId(Long id) {
        if (id == null) {
            return null;
        }
        OrganizationMember organizationMember = new OrganizationMember();
        organizationMember.setId(id);
        return organizationMember;
    }

    Map<String, String> conversions = ImmutableMap.<String, String>builder().
    		put("userId", "user.id").
            put("organizationId", "organization.id").
            build();

    @Override
    default Map<String, String> getConversions() {
        return conversions;
    }
}
