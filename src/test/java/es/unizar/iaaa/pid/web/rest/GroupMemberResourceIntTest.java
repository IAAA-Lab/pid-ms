package es.unizar.iaaa.pid.web.rest;

import es.unizar.iaaa.pid.PidmsApp;

import es.unizar.iaaa.pid.domain.GroupMember;
import es.unizar.iaaa.pid.repository.GroupMemberRepository;
import es.unizar.iaaa.pid.service.GroupMemberService;
import es.unizar.iaaa.pid.service.dto.GroupMemberDTO;
import es.unizar.iaaa.pid.service.mapper.GroupMemberMapper;
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
 * Test class for the GroupMemberResource REST controller.
 *
 * @see GroupMemberResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PidmsApp.class)
public class GroupMemberResourceIntTest {

    private static final Capacity DEFAULT_CAPACITY = Capacity.ADMIN;
    private static final Capacity UPDATED_CAPACITY = Capacity.EDITOR;

    @Autowired
    private GroupMemberRepository groupMemberRepository;

    @Autowired
    private GroupMemberMapper groupMemberMapper;

    @Autowired
    private GroupMemberService groupMemberService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restGroupMemberMockMvc;

    private GroupMember groupMember;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        GroupMemberResource groupMemberResource = new GroupMemberResource(groupMemberService);
        this.restGroupMemberMockMvc = MockMvcBuilders.standaloneSetup(groupMemberResource)
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
    public static GroupMember createEntity(EntityManager em) {
        GroupMember groupMember = new GroupMember()
            .capacity(DEFAULT_CAPACITY);
        return groupMember;
    }

    @Before
    public void initTest() {
        groupMember = createEntity(em);
    }

    @Test
    @Transactional
    public void createGroupMember() throws Exception {
        int databaseSizeBeforeCreate = groupMemberRepository.findAll().size();

        // Create the GroupMember
        GroupMemberDTO groupMemberDTO = groupMemberMapper.toDto(groupMember);
        restGroupMemberMockMvc.perform(post("/api/group-members")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(groupMemberDTO)))
            .andExpect(status().isCreated());

        // Validate the GroupMember in the database
        List<GroupMember> groupMemberList = groupMemberRepository.findAll();
        assertThat(groupMemberList).hasSize(databaseSizeBeforeCreate + 1);
        GroupMember testGroupMember = groupMemberList.get(groupMemberList.size() - 1);
        assertThat(testGroupMember.getCapacity()).isEqualTo(DEFAULT_CAPACITY);
    }

    @Test
    @Transactional
    public void createGroupMemberWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = groupMemberRepository.findAll().size();

        // Create the GroupMember with an existing ID
        groupMember.setId(1L);
        GroupMemberDTO groupMemberDTO = groupMemberMapper.toDto(groupMember);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGroupMemberMockMvc.perform(post("/api/group-members")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(groupMemberDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<GroupMember> groupMemberList = groupMemberRepository.findAll();
        assertThat(groupMemberList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCapacityIsRequired() throws Exception {
        int databaseSizeBeforeTest = groupMemberRepository.findAll().size();
        // set the field null
        groupMember.setCapacity(null);

        // Create the GroupMember, which fails.
        GroupMemberDTO groupMemberDTO = groupMemberMapper.toDto(groupMember);

        restGroupMemberMockMvc.perform(post("/api/group-members")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(groupMemberDTO)))
            .andExpect(status().isBadRequest());

        List<GroupMember> groupMemberList = groupMemberRepository.findAll();
        assertThat(groupMemberList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGroupMembers() throws Exception {
        // Initialize the database
        groupMemberRepository.saveAndFlush(groupMember);

        // Get all the groupMemberList
        restGroupMemberMockMvc.perform(get("/api/group-members?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(groupMember.getId().intValue())))
            .andExpect(jsonPath("$.[*].capacity").value(hasItem(DEFAULT_CAPACITY.toString())));
    }

    @Test
    @Transactional
    public void getGroupMember() throws Exception {
        // Initialize the database
        groupMemberRepository.saveAndFlush(groupMember);

        // Get the groupMember
        restGroupMemberMockMvc.perform(get("/api/group-members/{id}", groupMember.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(groupMember.getId().intValue()))
            .andExpect(jsonPath("$.capacity").value(DEFAULT_CAPACITY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingGroupMember() throws Exception {
        // Get the groupMember
        restGroupMemberMockMvc.perform(get("/api/group-members/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGroupMember() throws Exception {
        // Initialize the database
        groupMemberRepository.saveAndFlush(groupMember);
        int databaseSizeBeforeUpdate = groupMemberRepository.findAll().size();

        // Update the groupMember
        GroupMember updatedGroupMember = groupMemberRepository.findOne(groupMember.getId());
        updatedGroupMember
            .capacity(UPDATED_CAPACITY);
        GroupMemberDTO groupMemberDTO = groupMemberMapper.toDto(updatedGroupMember);

        restGroupMemberMockMvc.perform(put("/api/group-members")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(groupMemberDTO)))
            .andExpect(status().isOk());

        // Validate the GroupMember in the database
        List<GroupMember> groupMemberList = groupMemberRepository.findAll();
        assertThat(groupMemberList).hasSize(databaseSizeBeforeUpdate);
        GroupMember testGroupMember = groupMemberList.get(groupMemberList.size() - 1);
        assertThat(testGroupMember.getCapacity()).isEqualTo(UPDATED_CAPACITY);
    }

    @Test
    @Transactional
    public void updateNonExistingGroupMember() throws Exception {
        int databaseSizeBeforeUpdate = groupMemberRepository.findAll().size();

        // Create the GroupMember
        GroupMemberDTO groupMemberDTO = groupMemberMapper.toDto(groupMember);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restGroupMemberMockMvc.perform(put("/api/group-members")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(groupMemberDTO)))
            .andExpect(status().isCreated());

        // Validate the GroupMember in the database
        List<GroupMember> groupMemberList = groupMemberRepository.findAll();
        assertThat(groupMemberList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteGroupMember() throws Exception {
        // Initialize the database
        groupMemberRepository.saveAndFlush(groupMember);
        int databaseSizeBeforeDelete = groupMemberRepository.findAll().size();

        // Get the groupMember
        restGroupMemberMockMvc.perform(delete("/api/group-members/{id}", groupMember.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<GroupMember> groupMemberList = groupMemberRepository.findAll();
        assertThat(groupMemberList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GroupMember.class);
        GroupMember groupMember1 = new GroupMember();
        groupMember1.setId(1L);
        GroupMember groupMember2 = new GroupMember();
        groupMember2.setId(groupMember1.getId());
        assertThat(groupMember1).isEqualTo(groupMember2);
        groupMember2.setId(2L);
        assertThat(groupMember1).isNotEqualTo(groupMember2);
        groupMember1.setId(null);
        assertThat(groupMember1).isNotEqualTo(groupMember2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GroupMemberDTO.class);
        GroupMemberDTO groupMemberDTO1 = new GroupMemberDTO();
        groupMemberDTO1.setId(1L);
        GroupMemberDTO groupMemberDTO2 = new GroupMemberDTO();
        assertThat(groupMemberDTO1).isNotEqualTo(groupMemberDTO2);
        groupMemberDTO2.setId(groupMemberDTO1.getId());
        assertThat(groupMemberDTO1).isEqualTo(groupMemberDTO2);
        groupMemberDTO2.setId(2L);
        assertThat(groupMemberDTO1).isNotEqualTo(groupMemberDTO2);
        groupMemberDTO1.setId(null);
        assertThat(groupMemberDTO1).isNotEqualTo(groupMemberDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(groupMemberMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(groupMemberMapper.fromId(null)).isNull();
    }
}
