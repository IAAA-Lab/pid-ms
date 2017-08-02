package es.unizar.iaaa.pid.service.mapper;

import es.unizar.iaaa.pid.domain.*;
import es.unizar.iaaa.pid.service.dto.GroupMemberDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity GroupMember and its DTO GroupMemberDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, GroupMapper.class, })
public interface GroupMemberMapper extends EntityMapper <GroupMemberDTO, GroupMember> {

    @Mapping(source = "user.id", target = "userId")

    @Mapping(source = "group.id", target = "groupId")
    GroupMemberDTO toDto(GroupMember groupMember); 

    @Mapping(source = "userId", target = "user")

    @Mapping(source = "groupId", target = "group")
    GroupMember toEntity(GroupMemberDTO groupMemberDTO); 
    default GroupMember fromId(Long id) {
        if (id == null) {
            return null;
        }
        GroupMember groupMember = new GroupMember();
        groupMember.setId(id);
        return groupMember;
    }
}
