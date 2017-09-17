package es.unizar.iaaa.pid.service.impl;

import es.unizar.iaaa.pid.service.OrganizationMemberDTOService;
import es.unizar.iaaa.pid.domain.OrganizationMember;
import es.unizar.iaaa.pid.repository.OrganizationMemberRepository;
import es.unizar.iaaa.pid.service.dto.OrganizationMemberDTO;
import es.unizar.iaaa.pid.service.mapper.OrganizationMemberMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing OrganizationMember.
 */
@Service
@Transactional
public class OrganizationMemberServiceImpl implements OrganizationMemberDTOService {

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
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<OrganizationMemberDTO> findAll(Pageable pageable) {
        log.debug("Request to get all OrganizationMembers");
        return organizationMemberRepository.findAll(organizationMemberMapper.toPage(pageable))
            .map(organizationMemberMapper::toDto);
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

    /**
     *  Get all the organizationMembers that belongs to organizations where the Principal is a member.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<OrganizationMemberDTO> findAllInPrincipalOrganizations(Pageable pageable) {
        log.debug("Request to get all OrganizationMembers belonging to organizations where the Principal is a member");
        return organizationMemberRepository.findAllInPrincipalOrganizations(organizationMemberMapper.toPage(pageable))
            .map(organizationMemberMapper::toDto);
    }

    /**
     *  Get the "id" organizationMember that belongs to an organization where the Principal is a member.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public OrganizationMemberDTO findOneInPrincipalOrganizations(Long id) {
        log.debug("Request to get OrganizationMember : {}", id);
        OrganizationMember organizationMember = organizationMemberRepository.findOneInPrincipalOrganizations(id);
        return organizationMemberMapper.toDto(organizationMember);
    }
}
