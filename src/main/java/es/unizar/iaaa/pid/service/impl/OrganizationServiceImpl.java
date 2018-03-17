package es.unizar.iaaa.pid.service.impl;

import es.unizar.iaaa.pid.domain.Organization;
import es.unizar.iaaa.pid.domain.enumeration.Capacity;
import es.unizar.iaaa.pid.repository.OrganizationMemberRepository;
import es.unizar.iaaa.pid.repository.OrganizationRepository;
import es.unizar.iaaa.pid.service.NamespaceDTOService;
import es.unizar.iaaa.pid.service.OrganizationDTOService;
import es.unizar.iaaa.pid.service.OrganizationMemberDTOService;
import es.unizar.iaaa.pid.service.UserService;
import es.unizar.iaaa.pid.service.dto.OrganizationDTO;
import es.unizar.iaaa.pid.service.dto.OrganizationMemberDTO;
import es.unizar.iaaa.pid.service.mapper.OrganizationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Organization.
 */
@Service
@Transactional
public class OrganizationServiceImpl implements OrganizationDTOService {

    private final Logger log = LoggerFactory.getLogger(OrganizationServiceImpl.class);

    private final OrganizationRepository organizationRepository;

    private final OrganizationMemberRepository organizationMemberRepository;

    private final NamespaceDTOService namespaceDTOService;

    private final OrganizationMemberDTOService organizationMemberDTOService;

    private final UserService userService;

    private final OrganizationMapper organizationMapper;

    public OrganizationServiceImpl(OrganizationRepository organizationRepository, OrganizationMapper organizationMapper,
    		NamespaceDTOService namespaceDTOService, OrganizationMemberDTOService organizationMemberDTOService,
    		UserService userService, OrganizationMemberRepository organizationMemberRepository) {
        this.organizationRepository = organizationRepository;
        this.organizationMapper = organizationMapper;
        this.namespaceDTOService = namespaceDTOService;
        this.organizationMemberDTOService = organizationMemberDTOService;
        this.userService = userService;
        this.organizationMemberRepository = organizationMemberRepository;
    }

    /**
     * Save a organization.
     *
     * @param organizationDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public OrganizationDTO save(OrganizationDTO organizationDTO) {
        log.debug("Request to save Organization : {}", organizationDTO);
        Organization organization = organizationMapper.toEntity(organizationDTO);
        Organization savedOrganization = organizationRepository.save(organization);

        //Add organization member if none
        if (organizationMemberRepository.findByOrganization(savedOrganization).isEmpty()) {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            userService.getUserWithAuthoritiesByLogin(user.getUsername()).ifPresent(u -> {
                    OrganizationMemberDTO organizationMemberDTO = new OrganizationMemberDTO();
                    organizationMemberDTO.setCapacity(Capacity.ADMIN);
                    organizationMemberDTO.setOrganizationId(savedOrganization.getId());
                    organizationMemberDTO.setUserId(u.getId());
                    organizationMemberDTOService.save(organizationMemberDTO);
                }
            );
        }

        return organizationMapper.toDto(savedOrganization);
    }

    /**
     *  Get all the organizations.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<OrganizationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Organizations");
        return organizationRepository.findAll(organizationMapper.toPage(pageable))
            .map(organizationMapper::toDto);
    }

    /**
     *  Get one organization by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public OrganizationDTO findOne(Long id) {
        log.debug("Request to get Organization : {}", id);
        Organization organization = organizationRepository.findOne(id);
        return organizationMapper.toDto(organization);
    }

    /**
     *  Delete the  organization by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Organization : {}", id);

        //delete all associated namespaces
        namespaceDTOService.deleteAllByOrganizationId(id);

        //delete all associated organizationMember
        organizationMemberDTOService.deleteAllByOrganizationId(id);
        organizationRepository.delete(id);
    }
}
