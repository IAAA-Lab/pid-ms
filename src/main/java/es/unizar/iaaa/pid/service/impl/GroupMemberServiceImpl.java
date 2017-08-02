package es.unizar.iaaa.pid.service.impl;

import es.unizar.iaaa.pid.service.GroupMemberService;
import es.unizar.iaaa.pid.domain.GroupMember;
import es.unizar.iaaa.pid.repository.GroupMemberRepository;
import es.unizar.iaaa.pid.service.dto.GroupMemberDTO;
import es.unizar.iaaa.pid.service.mapper.GroupMemberMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing GroupMember.
 */
@Service
@Transactional
public class GroupMemberServiceImpl implements GroupMemberService{

    private final Logger log = LoggerFactory.getLogger(GroupMemberServiceImpl.class);

    private final GroupMemberRepository groupMemberRepository;

    private final GroupMemberMapper groupMemberMapper;

    public GroupMemberServiceImpl(GroupMemberRepository groupMemberRepository, GroupMemberMapper groupMemberMapper) {
        this.groupMemberRepository = groupMemberRepository;
        this.groupMemberMapper = groupMemberMapper;
    }

    /**
     * Save a groupMember.
     *
     * @param groupMemberDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public GroupMemberDTO save(GroupMemberDTO groupMemberDTO) {
        log.debug("Request to save GroupMember : {}", groupMemberDTO);
        GroupMember groupMember = groupMemberMapper.toEntity(groupMemberDTO);
        groupMember = groupMemberRepository.save(groupMember);
        return groupMemberMapper.toDto(groupMember);
    }

    /**
     *  Get all the groupMembers.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<GroupMemberDTO> findAll() {
        log.debug("Request to get all GroupMembers");
        return groupMemberRepository.findAll().stream()
            .map(groupMemberMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get one groupMember by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public GroupMemberDTO findOne(Long id) {
        log.debug("Request to get GroupMember : {}", id);
        GroupMember groupMember = groupMemberRepository.findOne(id);
        return groupMemberMapper.toDto(groupMember);
    }

    /**
     *  Delete the  groupMember by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete GroupMember : {}", id);
        groupMemberRepository.delete(id);
    }
}
