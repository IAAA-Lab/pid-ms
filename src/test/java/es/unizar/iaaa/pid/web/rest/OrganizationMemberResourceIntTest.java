package es.unizar.iaaa.pid.web.rest;

import es.unizar.iaaa.pid.PidmsApp;
import es.unizar.iaaa.pid.domain.Organization;
import es.unizar.iaaa.pid.domain.OrganizationMember;
import es.unizar.iaaa.pid.domain.enumeration.Capacity;
import es.unizar.iaaa.pid.repository.OrganizationMemberRepository;
import es.unizar.iaaa.pid.service.OrganizationDTOService;
import es.unizar.iaaa.pid.service.OrganizationMemberDTOService;
import es.unizar.iaaa.pid.service.dto.OrganizationMemberDTO;
import es.unizar.iaaa.pid.service.mapper.OrganizationMapper;
import es.unizar.iaaa.pid.service.mapper.OrganizationMemberMapper;
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

import static es.unizar.iaaa.pid.web.rest.util.ResourceFixtures.*;
import static es.unizar.iaaa.pid.web.rest.util.HeaderUtil.ERROR_ID_ALREADY_EXIST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
/**
 * Test class for the OrganizationMemberResource REST controller.
 *
 * @see OrganizationMemberResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PidmsApp.class)
@Transactional
@WithMockUser(username=UserResourceIntTest.DEFAULT_LOGIN,password=UserResourceIntTest.DEFAULT_PASSWORD)
public class OrganizationMemberResourceIntTest extends LoggedUser {


    @Autowired
    private OrganizationMemberRepository organizationMemberRepository;

    @Autowired
    private OrganizationMemberMapper organizationMemberMapper;

    @Autowired
    private OrganizationMemberDTOService organizationMemberService;

    @Autowired
    private OrganizationDTOService organizationDTOService;

    @Autowired
    private OrganizationMapper organizationMapper;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restOrganizationMemberMockMvc;

    private OrganizationMember organizationMember;

    private OrganizationMember organizationMemberNew;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OrganizationMemberResource organizationMemberResource = new OrganizationMemberResource(organizationMemberService);
        this.restOrganizationMemberMockMvc = MockMvcBuilders.standaloneSetup(organizationMemberResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */

    @Before
    public void initTest() {
        Organization organization = organizationMapper.toEntity(organizationDTOService.save(organizationMapper.toDto(organization())));

        em.flush();

        organizationMember = organizationMemberRepository.findAll().stream().findFirst().get();

        //create organizationMembers
        organizationMemberNew = new OrganizationMember().user(loggedUser).organization(organization).capacity(Capacity.ADMIN);
    }

    @Test
    public void createOrganizationMember() throws Exception {
        // Create the Task
        OrganizationMemberDTO organizationMemberDTO = organizationMemberMapper.toDto(organizationMemberNew);

        MvcResult result = restOrganizationMemberMockMvc.perform(post("/api/organization-members")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(organizationMemberDTO)))
            .andExpect(status().isCreated()).andReturn();

        // Extract current id from Location
        Long id = MvcResultUtils.extractIdFromLocation(result);

        // Validate the Task in the database
        OrganizationMemberDTO testOrganizationMember = organizationMemberService.findOne(id);
        assertThat(testOrganizationMember).isNotNull();
        assertThat(testOrganizationMember.getCapacity()).isEqualTo(DEFAULT_CAPACITY);
    }

    @Test
    public void createOrganizationMemberTaskWithIdMustFail() throws Exception {
        // Create the Task with an existing ID
        organizationMemberNew.setId(1L);
        OrganizationMemberDTO organizationMemberDTO = organizationMemberMapper.toDto(organizationMemberNew);

        // An entity with an  ID cannot be created, so this API call must fail
        restOrganizationMemberMockMvc.perform(post("/api/organization-members")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(organizationMemberDTO)))
            .andExpect(status().isBadRequest())
            .andExpect(header().string(ERROR_HEADER,ERROR_ID_ALREADY_EXIST));
    }

    @Test
    public void checkCapacityIsRequired() throws Exception {
        // Create the Task with an existing ID
        organizationMemberNew.setCapacity(null);
        OrganizationMemberDTO organizationMemberDTO = organizationMemberMapper.toDto(organizationMemberNew);

        // An entity with an  ID cannot be created, so this API call must fail
        restOrganizationMemberMockMvc.perform(post("/api/organization-members")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(organizationMemberDTO)))
            .andExpect(status().isBadRequest())
            .andExpect(TestUtil.checkError(FIELD_CAPACITY, ERROR_NULL_FIELD));
    }

    @Test
    public void getAllOrganizationMembers() throws Exception {
        // Initialize the database
        OrganizationMemberDTO organizationMemberDTO = organizationMemberMapper.toDto(organizationMemberNew);
        organizationMemberDTO = organizationMemberService.save(organizationMemberDTO);

        // Get all the organizationMemberList
        restOrganizationMemberMockMvc.perform(get("/api/organization-members?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(organizationMemberDTO.getId().intValue())))
            .andExpect(jsonPath("$.[*].capacity").value(hasItem(organizationMemberDTO.getCapacity().toString())));
    }

    @Test
    public void getOrganizationMember() throws Exception {
        // Initialize the database
        OrganizationMemberDTO organizationMemberDTO = organizationMemberMapper.toDto(organizationMemberNew);
        organizationMemberDTO = organizationMemberService.save(organizationMemberDTO);

        // Get the organizationMember
        restOrganizationMemberMockMvc.perform(get("/api/organization-members/{id}", organizationMemberDTO.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(organizationMemberDTO.getId().intValue()))
            .andExpect(jsonPath("$.capacity").value(organizationMemberDTO.getCapacity().toString()));
    }

    @Test
    public void getNonExistingOrganizationMember() throws Exception {
        // Get the task
        restOrganizationMemberMockMvc.perform(get("/api/organization-members/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateOrganizationMember() throws Exception {
        // Initialize the database
        OrganizationMemberDTO organizationMemberDTO = organizationMemberMapper.toDto(organizationMember);
        organizationMemberDTO = organizationMemberService.save(organizationMemberDTO);

        // Update the organizationMember
        OrganizationMemberDTO updated = organizationMemberService.findOne(organizationMemberDTO.getId());
        updated.setCapacity(UPDATED_CAPACITY);

        restOrganizationMemberMockMvc.perform(put("/api/organization-members/{id}", updated.getId())
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updated)))
            .andExpect(status().isOk());

        // Validate the OrganizationMember in the database
        OrganizationMemberDTO testOrganizationMember = organizationMemberService.findOne(organizationMemberDTO.getId());
        assertThat(testOrganizationMember.getCapacity()).isEqualTo(UPDATED_CAPACITY);
    }

    @Test
    public void updateNonExistingOrganizationMemberFails() throws Exception {
        OrganizationMemberDTO organizationMemberDTO = organizationMemberMapper.toDto(organizationMemberNew);
        Long id = Long.MAX_VALUE;

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restOrganizationMemberMockMvc.perform(put("/api/organization-members/{id}", id)
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(organizationMemberDTO)))
            .andExpect(status().isNotFound());
    }

    @Test
    public void deleteOrganizationMember() throws Exception {

        restOrganizationMemberMockMvc.perform(get("/api/organization-members/{id}", organizationMember.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        restOrganizationMemberMockMvc.perform(delete("/api/organization-members/{id}", organizationMember.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        restOrganizationMemberMockMvc.perform(get("/api/organization-members/{id}", organizationMember.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNotFound());
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrganizationMember.class);
        OrganizationMember organizationMember1 = new OrganizationMember();
        organizationMember1.setId(1L);
        OrganizationMember organizationMember2 = new OrganizationMember();
        organizationMember2.setId(organizationMember1.getId());
        assertThat(organizationMember1).isEqualTo(organizationMember2);
        organizationMember2.setId(2L);
        assertThat(organizationMember1).isNotEqualTo(organizationMember2);
        organizationMember1.setId(null);
        assertThat(organizationMember1).isNotEqualTo(organizationMember2);
    }

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrganizationMemberDTO.class);
        OrganizationMemberDTO organizationMemberDTO1 = new OrganizationMemberDTO();
        organizationMemberDTO1.setId(1L);
        OrganizationMemberDTO organizationMemberDTO2 = new OrganizationMemberDTO();
        assertThat(organizationMemberDTO1).isNotEqualTo(organizationMemberDTO2);
        organizationMemberDTO2.setId(organizationMemberDTO1.getId());
        assertThat(organizationMemberDTO1).isEqualTo(organizationMemberDTO2);
        organizationMemberDTO2.setId(2L);
        assertThat(organizationMemberDTO1).isNotEqualTo(organizationMemberDTO2);
        organizationMemberDTO1.setId(null);
        assertThat(organizationMemberDTO1).isNotEqualTo(organizationMemberDTO2);
    }

    @Test
    public void testEntityFromId() {
        assertThat(organizationMemberMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(organizationMemberMapper.fromId(null)).isNull();
    }
}
