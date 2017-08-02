package es.unizar.iaaa.pid.service.impl;

import es.unizar.iaaa.pid.service.GroupService;
import es.unizar.iaaa.pid.domain.Group;
import es.unizar.iaaa.pid.repository.GroupRepository;
import es.unizar.iaaa.pid.service.dto.GroupDTO;
import es.unizar.iaaa.pid.service.mapper.GroupMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Group.
 */
@Service
@Transactional
public class GroupServiceImpl implements GroupService{

    private final Logger log = LoggerFactory.getLogger(GroupServiceImpl.class);

    private final GroupRepository groupRepository;

    private final GroupMapper groupMapper;

    public GroupServiceImpl(GroupRepository groupRepository, GroupMapper groupMapper) {
        this.groupRepository = groupRepository;
        this.groupMapper = groupMapper;
    }

    /**
     * Save a group.
     *
     * @param groupDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public GroupDTO save(GroupDTO groupDTO) {
        log.debug("Request to save Group : {}", groupDTO);
        Group group = groupMapper.toEntity(groupDTO);
        group = groupRepository.save(group);
        return groupMapper.toDto(group);
    }

    /**
     *  Get all the groups.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<GroupDTO> findAll() {
        log.debug("Request to get all Groups");
        return groupRepository.findAll().stream()
            .map(groupMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get one group by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public GroupDTO findOne(Long id) {
        log.debug("Request to get Group : {}", id);
        Group group = groupRepository.findOne(id);
        return groupMapper.toDto(group);
    }

    /**
     *  Delete the  group by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Group : {}", id);
        groupRepository.delete(id);
    }
}
