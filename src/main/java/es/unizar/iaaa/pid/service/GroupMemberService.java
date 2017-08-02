package es.unizar.iaaa.pid.service;

import es.unizar.iaaa.pid.service.dto.GroupMemberDTO;
import java.util.List;

/**
 * Service Interface for managing GroupMember.
 */
public interface GroupMemberService {

    /**
     * Save a groupMember.
     *
     * @param groupMemberDTO the entity to save
     * @return the persisted entity
     */
    GroupMemberDTO save(GroupMemberDTO groupMemberDTO);

    /**
     *  Get all the groupMembers.
     *
     *  @return the list of entities
     */
    List<GroupMemberDTO> findAll();

    /**
     *  Get the "id" groupMember.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    GroupMemberDTO findOne(Long id);

    /**
     *  Delete the "id" groupMember.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
