package es.unizar.iaaa.pid.web.rest;

import es.unizar.iaaa.pid.PidmsApp;

import es.unizar.iaaa.pid.domain.OrganizationMember;
import es.unizar.iaaa.pid.repository.OrganizationMemberRepository;
import es.unizar.iaaa.pid.service.OrganizationMemberDTOService;
import es.unizar.iaaa.pid.service.dto.OrganizationMemberDTO;
import es.unizar.iaaa.pid.service.mapper.OrganizationMemberMapper;
import es.unizar.iaaa.pid.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import es.unizar.iaaa.pid.domain.enumeration.Capacity;
/**
 * Test class for the OrganizationMemberResource REST controller.
 *
 * @see OrganizationMemberResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PidmsApp.class)
public class OrganizationMemberResourceIntTest {

    private static final Capacity DEFAULT_CAPACITY = Capacity.ADMIN;
    private static final Capacity UPDATED_CAPACITY = Capacity.EDITOR;

    @Autowired
    private OrganizationMemberRepository organizationMemberRepository;

    @Autowired
    private OrganizationMemberMapper organizationMemberMapper;

    @Autowired
    private OrganizationMemberDTOService organizationMemberService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restOrganizationMemberMockMvc;

    private OrganizationMember organizationMember;

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
    public static OrganizationMember createEntity(EntityManager em) {
        OrganizationMember organizationMember = new OrganizationMember()
            .capacity(DEFAULT_CAPACITY);
        return organizationMember;
    }

    @Before
    public void initTest() {
        organizationMember = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrganizationMember() throws Exception {
        int databaseSizeBeforeCreate = organizationMemberRepository.findAll().size();

        // Create the OrganizationMember
        OrganizationMemberDTO organizationMemberDTO = organizationMemberMapper.toDto(organizationMember);
        restOrganizationMemberMockMvc.perform(post("/api/organization-members")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(organizationMemberDTO)))
            .andExpect(status().isCreated());

        // Validate the OrganizationMember in the database
        List<OrganizationMember> organizationMemberList = organizationMemberRepository.findAll();
        assertThat(organizationMemberList).hasSize(databaseSizeBeforeCreate + 1);
        OrganizationMember testOrganizationMember = organizationMemberList.get(organizationMemberList.size() - 1);
        assertThat(testOrganizationMember.getCapacity()).isEqualTo(DEFAULT_CAPACITY);
    }

    @Test
    @Transactional
    public void createOrganizationMemberWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = organizationMemberRepository.findAll().size();

        // Create the OrganizationMember with an existing ID
        organizationMember.setId(1L);
        OrganizationMemberDTO organizationMemberDTO = organizationMemberMapper.toDto(organizationMember);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrganizationMemberMockMvc.perform(post("/api/organization-members")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(organizationMemberDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<OrganizationMember> organizationMemberList = organizationMemberRepository.findAll();
        assertThat(organizationMemberList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCapacityIsRequired() throws Exception {
        int databaseSizeBeforeTest = organizationMemberRepository.findAll().size();
        // set the field null
        organizationMember.setCapacity(null);

        // Create the OrganizationMember, which fails.
        OrganizationMemberDTO organizationMemberDTO = organizationMemberMapper.toDto(organizationMember);

        restOrganizationMemberMockMvc.perform(post("/api/organization-members")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(organizationMemberDTO)))
            .andExpect(status().isBadRequest());

        List<OrganizationMember> organizationMemberList = organizationMemberRepository.findAll();
        assertThat(organizationMemberList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOrganizationMembers() throws Exception {
        // Initialize the database
        organizationMemberRepository.saveAndFlush(organizationMember);

        // Get all the organizationMemberList
        restOrganizationMemberMockMvc.perform(get("/api/organization-members?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(organizationMember.getId().intValue())))
            .andExpect(jsonPath("$.[*].capacity").value(hasItem(DEFAULT_CAPACITY.toString())));
    }

    @Test
    @Transactional
    public void getOrganizationMember() throws Exception {
        // Initialize the database
        organizationMemberRepository.saveAndFlush(organizationMember);

        // Get the organizationMember
        restOrganizationMemberMockMvc.perform(get("/api/organization-members/{id}", organizationMember.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(organizationMember.getId().intValue()))
            .andExpect(jsonPath("$.capacity").value(DEFAULT_CAPACITY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingOrganizationMember() throws Exception {
        // Get the organizationMember
        restOrganizationMemberMockMvc.perform(get("/api/organization-members/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrganizationMember() throws Exception {
        // Initialize the database
        organizationMemberRepository.saveAndFlush(organizationMember);
        int databaseSizeBeforeUpdate = organizationMemberRepository.findAll().size();

        // Update the organizationMember
        OrganizationMember updatedOrganizationMember = organizationMemberRepository.findOne(organizationMember.getId());
        updatedOrganizationMember
            .capacity(UPDATED_CAPACITY);
        OrganizationMemberDTO organizationMemberDTO = organizationMemberMapper.toDto(updatedOrganizationMember);

        restOrganizationMemberMockMvc.perform(put("/api/organization-members")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(organizationMemberDTO)))
            .andExpect(status().isOk());

        // Validate the OrganizationMember in the database
        List<OrganizationMember> organizationMemberList = organizationMemberRepository.findAll();
        assertThat(organizationMemberList).hasSize(databaseSizeBeforeUpdate);
        OrganizationMember testOrganizationMember = organizationMemberList.get(organizationMemberList.size() - 1);
        assertThat(testOrganizationMember.getCapacity()).isEqualTo(UPDATED_CAPACITY);
    }

    @Test
    @Transactional
    public void updateNonExistingOrganizationMember() throws Exception {
        int databaseSizeBeforeUpdate = organizationMemberRepository.findAll().size();

        // Create the OrganizationMember
        OrganizationMemberDTO organizationMemberDTO = organizationMemberMapper.toDto(organizationMember);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restOrganizationMemberMockMvc.perform(put("/api/organization-members")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(organizationMemberDTO)))
            .andExpect(status().isCreated());

        // Validate the OrganizationMember in the database
        List<OrganizationMember> organizationMemberList = organizationMemberRepository.findAll();
        assertThat(organizationMemberList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteOrganizationMember() throws Exception {
        // Initialize the database
        organizationMemberRepository.saveAndFlush(organizationMember);
        int databaseSizeBeforeDelete = organizationMemberRepository.findAll().size();

        // Get the organizationMember
        restOrganizationMemberMockMvc.perform(delete("/api/organization-members/{id}", organizationMember.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<OrganizationMember> organizationMemberList = organizationMemberRepository.findAll();
        assertThat(organizationMemberList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
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
    @Transactional
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
    @Transactional
    public void testEntityFromId() {
        assertThat(organizationMemberMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(organizationMemberMapper.fromId(null)).isNull();
    }
}
