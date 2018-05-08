package es.unizar.iaaa.pid.web.rest;

import es.unizar.iaaa.pid.PidmsApp;
import es.unizar.iaaa.pid.domain.Organization;
import es.unizar.iaaa.pid.repository.OrganizationRepository;
import es.unizar.iaaa.pid.service.OrganizationDTOService;
import es.unizar.iaaa.pid.service.OrganizationMemberDTOService;
import es.unizar.iaaa.pid.service.dto.OrganizationDTO;
import es.unizar.iaaa.pid.service.mapper.OrganizationMapper;
import es.unizar.iaaa.pid.web.rest.errors.ExceptionTranslator;
import es.unizar.iaaa.pid.web.rest.util.MvcResultUtils;
import es.unizar.iaaa.pid.web.rest.util.TestUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static es.unizar.iaaa.pid.web.rest.util.ResourceFixtures.*;
import static es.unizar.iaaa.pid.web.rest.util.HeaderUtil.ERROR_HEADER;
import static es.unizar.iaaa.pid.web.rest.util.HeaderUtil.ERROR_ID_ALREADY_EXIST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the OrganizationResource REST controller.
 *
 * @see OrganizationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PidmsApp.class)
@Transactional
public class OrganizationResourceIntTest extends LoggedUser {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private OrganizationMapper organizationMapper;

    @Autowired
    private OrganizationDTOService organizationService;

    @Autowired
    private OrganizationMemberDTOService organizationMemberDTOService;


    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;


    private MockMvc restOrganizationMockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OrganizationResource organizationResource = new OrganizationResource(organizationService,
        		organizationMemberDTOService);
        this.restOrganizationMockMvc = MockMvcBuilders.standaloneSetup(organizationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }



    @Test
    @WithMockUser(username=UserResourceIntTest.DEFAULT_LOGIN,password=UserResourceIntTest.DEFAULT_PASSWORD)
    public void basicCreationWorks() throws Exception {
        // Create the Organization
        OrganizationDTO organizationDTO = organizationMapper.toDto(organization());
        MvcResult result = restOrganizationMockMvc.perform(post("/api/organizations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(organizationDTO)))
            .andExpect(status().isCreated()).andReturn();

        // Extract current id from Location
        Long id = MvcResultUtils.extractIdFromLocation(result);

        // Validate the Organization in the database
        Organization organization = organizationRepository.findOne(id);
        assertThat(organization).isNotNull();
        assertThat(organization.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(organization.getTitle()).isEqualTo(DEFAULT_TITLE);
    }

    @Test
    @WithMockUser(username=UserResourceIntTest.DEFAULT_LOGIN,password=UserResourceIntTest.DEFAULT_PASSWORD)
    public void creationFailWhenContainsId() throws Exception {
        List<Organization> previousState = organizationRepository.findAll();

        // Create the Organization with an ID
        OrganizationDTO organizationDTO = organizationMapper.toDto(organization(1L));

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrganizationMockMvc.perform(post("/api/organizations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(organizationDTO)))
            .andExpect(status().isBadRequest()).andExpect(header().string(ERROR_HEADER,ERROR_ID_ALREADY_EXIST));

        // Check that the size of the database has not changes
        assertThat(organizationRepository.findAll()).isEqualTo(previousState);
    }

    @Test
    @WithMockUser(username=UserResourceIntTest.DEFAULT_LOGIN,password=UserResourceIntTest.DEFAULT_PASSWORD)
    public void creationFailWhenNameIsMissing() throws Exception {
        List<Organization> previousState = organizationRepository.findAll();

        // Create the Organization, which fails.
        OrganizationDTO organizationDTO = organizationMapper.toDto(anonymousOrganization());

        restOrganizationMockMvc.perform(post("/api/organizations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(organizationDTO)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.fieldErrors[0].field").value("name"))
            .andExpect(jsonPath("$.fieldErrors[0].message").value("NotNull"));


        // Validate the Organization in the database
        assertThat(organizationRepository.findAll()).isEqualTo(previousState);
    }

    @Test
    public void getAllOrganizations() throws Exception {
        Organization persistedOrganization = organizationRepository.saveAndFlush(organization());

        // Get all the organizationList
        restOrganizationMockMvc.perform(get("/api/organizations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(persistedOrganization.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)));
    }

    @Test
    public void getAnOrganization() throws Exception {
        Organization persistedOrganization = organizationRepository.saveAndFlush(organization());

        // Get the organization
        restOrganizationMockMvc.perform(get("/api/organizations/{id}", persistedOrganization.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(persistedOrganization.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE));
    }

    @Test
    public void getNonExistingOrganization() throws Exception {
        organizationRepository.saveAndFlush(organization());
        long id = organizationRepository.findAll().stream().map(Organization::getId).max(Long::compare).orElse(0L) + 1;

        // Get the organization
        restOrganizationMockMvc.perform(get("/api/organizations/{id}", id))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username=UserResourceIntTest.DEFAULT_LOGIN,password=UserResourceIntTest.DEFAULT_PASSWORD)
    public void basicUpdateWorks() throws Exception {
        Organization persistedOrganization = organizationRepository.saveAndFlush(organization());
        linkOrganizationToLoggedUser(persistedOrganization);
        int databaseSizeBeforeUpdate = organizationRepository.findAll().size();

        // Update the organization
        persistedOrganization
            .name(UPDATED_NAME)
            .title(UPDATED_TITLE);
        OrganizationDTO organizationDTO = organizationMapper.toDto(persistedOrganization);

        restOrganizationMockMvc.perform(put("/api/organizations/{id}", persistedOrganization.getId())
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(organizationDTO)))
            .andExpect(status().isOk());

        // Validate the Organization in the database
        Organization organization = organizationRepository.findOne(persistedOrganization.getId());
        assertThat(organization).isNotNull();
        assertThat(organization.getName()).isEqualTo(UPDATED_NAME);
        assertThat(organization.getTitle()).isEqualTo(UPDATED_TITLE);

        // Validate the Organization in the database
        assertThat(organizationRepository.findAll()).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @WithMockUser(username=UserResourceIntTest.DEFAULT_LOGIN,password=UserResourceIntTest.DEFAULT_PASSWORD)
    public void updateNonExistingOrganization() throws Exception {
        List<Organization> previousState = organizationRepository.findAll();

        // Create the Organization
        long id = previousState.stream().map(Organization::getId).max(Long::compare).orElse(0L) + 1;
        Organization organization = organization(id);
        OrganizationDTO organizationDTO = organizationMapper.toDto(organization);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restOrganizationMockMvc.perform(put("/api/organizations/{id}", id)
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(organizationDTO)))
            .andExpect(status().isNotFound());

        // Validate the Organization in the database
        assertThat(organizationRepository.findAll()).isEqualTo(previousState);
    }

    @Test
    @WithMockUser(username=UserResourceIntTest.DEFAULT_LOGIN,password=UserResourceIntTest.DEFAULT_PASSWORD)
    public void anAdminCanDeleteOrganization() throws Exception {
        Organization persistedOrganization = organizationRepository.saveAndFlush(organization());
        linkOrganizationToLoggedUser(persistedOrganization);

        // Delete the organization
        restOrganizationMockMvc.perform(delete("/api/organizations/{id}", persistedOrganization.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database does not contains the key
        assertThat(organizationRepository.findAll()).doesNotContain(persistedOrganization);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Organization.class);
        Organization organization1 = new Organization();
        organization1.setId(1L);
        Organization organization2 = new Organization();
        organization2.setId(organization1.getId());
        assertThat(organization1).isEqualTo(organization2);
        organization2.setId(2L);
        assertThat(organization1).isNotEqualTo(organization2);
        organization1.setId(null);
        assertThat(organization1).isNotEqualTo(organization2);
    }

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrganizationDTO.class);
        OrganizationDTO organizationDTO1 = new OrganizationDTO();
        organizationDTO1.setId(1L);
        OrganizationDTO organizationDTO2 = new OrganizationDTO();
        assertThat(organizationDTO1).isNotEqualTo(organizationDTO2);
        organizationDTO2.setId(organizationDTO1.getId());
        assertThat(organizationDTO1).isEqualTo(organizationDTO2);
        organizationDTO2.setId(2L);
        assertThat(organizationDTO1).isNotEqualTo(organizationDTO2);
        organizationDTO1.setId(null);
        assertThat(organizationDTO1).isNotEqualTo(organizationDTO2);
    }

    @Test
    public void testEntityFromId() {
        assertThat(organizationMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(organizationMapper.fromId(null)).isNull();
    }
}
