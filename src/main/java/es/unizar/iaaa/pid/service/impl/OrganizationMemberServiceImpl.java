package es.unizar.iaaa.pid.service.impl;

import es.unizar.iaaa.pid.service.OrganizationMemberService;
import es.unizar.iaaa.pid.domain.OrganizationMember;
import es.unizar.iaaa.pid.repository.OrganizationMemberRepository;
import es.unizar.iaaa.pid.service.dto.OrganizationMemberDTO;
import es.unizar.iaaa.pid.service.mapper.OrganizationMemberMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing OrganizationMember.
 */
@Service
@Transactional
public class OrganizationMemberServiceImpl implements OrganizationMemberService{

    private final Logger log = LoggerFactory.getLogger(OrganizationMemberServiceImpl.class);

    private final OrganizationMemberRepository organizationMemberRepository;

    private final OrganizationMemberMapper organizationMemberMapper;

    public OrganizationMemberServiceImpl(OrganizationMemberRepository organizationMemberRepository, OrganizationMemberMapper organizationMemberMapper) {
        this.organizationMemberRepository = organizationMemberRepository;
        this.organizationMemberMapper = organizationMemberMapper;
    }

    /**
     * Save a organizationMember.
     *
     * @param organizationMemberDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public OrganizationMemberDTO save(OrganizationMemberDTO organizationMemberDTO) {
        log.debug("Request to save OrganizationMember : {}", organizationMemberDTO);
        OrganizationMember organizationMember = organizationMemberMapper.toEntity(organizationMemberDTO);
        organizationMember = organizationMemberRepository.save(organizationMember);
        return organizationMemberMapper.toDto(organizationMember);
    }

    /**
     *  Get all the organizationMembers.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<OrganizationMemberDTO> findAll() {
        log.debug("Request to get all OrganizationMembers");
        return organizationMemberRepository.findAll().stream()
            .map(organizationMemberMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get one organizationMember by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public OrganizationMemberDTO findOne(Long id) {
        log.debug("Request to get OrganizationMember : {}", id);
        OrganizationMember organizationMember = organizationMemberRepository.findOne(id);
        return organizationMemberMapper.toDto(organizationMember);
    }

    /**
     *  Delete the  organizationMember by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete OrganizationMember : {}", id);
        organizationMemberRepository.delete(id);
    }
}
