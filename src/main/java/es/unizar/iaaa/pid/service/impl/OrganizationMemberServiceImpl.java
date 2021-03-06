package es.unizar.iaaa.pid.service.impl;

import es.unizar.iaaa.pid.domain.OrganizationMember;
import es.unizar.iaaa.pid.domain.User;
import es.unizar.iaaa.pid.repository.OrganizationMemberRepository;
import es.unizar.iaaa.pid.service.OrganizationMemberDTOService;
import es.unizar.iaaa.pid.service.dto.OrganizationMemberDTO;
import es.unizar.iaaa.pid.service.mapper.OrganizationMemberMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;


/**
 * Service Implementation for managing OrganizationMember.
 */
@Service
@Transactional
public class OrganizationMemberServiceImpl implements OrganizationMemberDTOService {

    private final Logger log = LoggerFactory.getLogger(OrganizationMemberServiceImpl.class);

    private final OrganizationMemberRepository organizationMemberRepository;

    private final OrganizationMemberMapper organizationMemberMapper;

    private final EntityManager entityManager;

    public OrganizationMemberServiceImpl(OrganizationMemberRepository organizationMemberRepository,
                                         OrganizationMemberMapper organizationMemberMapper, EntityManager entityManager) {
        this.organizationMemberRepository = organizationMemberRepository;
        this.organizationMemberMapper = organizationMemberMapper;
        this.entityManager = entityManager;
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
         organizationMember.setUser(entityManager.getReference(User.class, organizationMember.getUser().getId()));
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
     *  Get the organizationMember that belongs to a specific organization where the Principal is a member.
     *
     *  @param organizationId the organization id
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public OrganizationMemberDTO findOneByOrganizationInPrincipal(Long organizationId) {
        log.debug("Request to get the OrganizationMember that belonging to a specific organization where the Principal is a member");
        OrganizationMember organizationMember = organizationMemberRepository.findOneByOrganizationInPrincipal(organizationId);
        return organizationMemberMapper.toDto(organizationMember);
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

    /**
     * Delete all organizationMembers associate with the organization
     *
     * @param organizationId id of the organization to be deleted
     */
    public void deleteAllByOrganizationId(Long organizationId){
    	log.debug("Request to delete all oragnizationMembers of the organization : {}", organizationId);
    	organizationMemberRepository.deleteAllByOrganizationId(organizationId);
    }
    
    /**
     * Find the organizationMember that has a specific user and specific organization
     * 
     * @param userId id of the organizationMember
     * @param organizationId id of the organizationMember
     */
    public OrganizationMemberDTO findOneByUserIdAndOrganizationId(Long userId, Long organizationId){
    	log.debug("Request to get OrganizationMember filter by userId {} and organizationId {}",userId, organizationId);
    	OrganizationMember organizationMember = organizationMemberRepository.findOneByUserIdAndOrganizationId(userId, organizationId);
    	return organizationMemberMapper.toDto(organizationMember);
    }
}
