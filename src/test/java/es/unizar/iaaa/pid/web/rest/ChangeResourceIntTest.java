package es.unizar.iaaa.pid.web.rest;

import es.unizar.iaaa.pid.PidmsApp;

import es.unizar.iaaa.pid.domain.Change;
import es.unizar.iaaa.pid.repository.ChangeRepository;
import es.unizar.iaaa.pid.service.ChangeService;
import es.unizar.iaaa.pid.service.dto.ChangeDTO;
import es.unizar.iaaa.pid.service.mapper.ChangeMapper;
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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import es.unizar.iaaa.pid.domain.enumeration.ChangeAction;
import es.unizar.iaaa.pid.domain.enumeration.ResourceType;
/**
 * Test class for the ChangeResource REST controller.
 *
 * @see ChangeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PidmsApp.class)
public class ChangeResourceIntTest {

    private static final Instant DEFAULT_TIMESTAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIMESTAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final ChangeAction DEFAULT_ACTION = ChangeAction.ISSUED;
    private static final ChangeAction UPDATED_ACTION = ChangeAction.CANCELLED;

    private static final String DEFAULT_FEATURE = "AAAAAAAAAA";
    private static final String UPDATED_FEATURE = "BBBBBBBBBB";

    private static final String DEFAULT_NAMESPACE = "AAAAAAAAAA";
    private static final String UPDATED_NAMESPACE = "BBBBBBBBBB";

    private static final String DEFAULT_LOCAL_ID = "AAAAAAAAAA";
    private static final String UPDATED_LOCAL_ID = "BBBBBBBBBB";

    private static final String DEFAULT_VERSION_ID = "AAAAAAAAAA";
    private static final String UPDATED_VERSION_ID = "BBBBBBBBBB";

    private static final Instant DEFAULT_BEGIN_LIFESPAN_VERSION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_BEGIN_LIFESPAN_VERSION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_LIFESPAN_VERSION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_LIFESPAN_VERSION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_ALTERNATE_ID = "AAAAAAAAAA";
    private static final String UPDATED_ALTERNATE_ID = "BBBBBBBBBB";

    private static final ResourceType DEFAULT_TYPE = ResourceType.DATASET;
    private static final ResourceType UPDATED_TYPE = ResourceType.SPATIAL_OBJECT;

    private static final String DEFAULT_LOCATOR = "AAAAAAAAAA";
    private static final String UPDATED_LOCATOR = "BBBBBBBBBB";

    @Autowired
    private ChangeRepository changeRepository;

    @Autowired
    private ChangeMapper changeMapper;

    @Autowired
    private ChangeService changeService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restChangeMockMvc;

    private Change change;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ChangeResource changeResource = new ChangeResource(changeService);
        this.restChangeMockMvc = MockMvcBuilders.standaloneSetup(changeResource)
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
    public static Change createEntity(EntityManager em) {
        Change change = new Change()
            .timestamp(DEFAULT_TIMESTAMP)
            .action(DEFAULT_ACTION)
            .feature(DEFAULT_FEATURE)
            .namespace(DEFAULT_NAMESPACE)
            .localId(DEFAULT_LOCAL_ID)
            .versionId(DEFAULT_VERSION_ID)
            .beginLifespanVersion(DEFAULT_BEGIN_LIFESPAN_VERSION)
            .endLifespanVersion(DEFAULT_END_LIFESPAN_VERSION)
            .alternateId(DEFAULT_ALTERNATE_ID)
            .type(DEFAULT_TYPE)
            .locator(DEFAULT_LOCATOR);
        return change;
    }

    @Before
    public void initTest() {
        change = createEntity(em);
    }

    @Test
    @Transactional
    public void createChange() throws Exception {
        int databaseSizeBeforeCreate = changeRepository.findAll().size();

        // Create the Change
        ChangeDTO changeDTO = changeMapper.toDto(change);
        restChangeMockMvc.perform(post("/api/changes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(changeDTO)))
            .andExpect(status().isCreated());

        // Validate the Change in the database
        List<Change> changeList = changeRepository.findAll();
        assertThat(changeList).hasSize(databaseSizeBeforeCreate + 1);
        Change testChange = changeList.get(changeList.size() - 1);
        assertThat(testChange.getTimestamp()).isEqualTo(DEFAULT_TIMESTAMP);
        assertThat(testChange.getAction()).isEqualTo(DEFAULT_ACTION);
        assertThat(testChange.getFeature()).isEqualTo(DEFAULT_FEATURE);
        assertThat(testChange.getNamespace()).isEqualTo(DEFAULT_NAMESPACE);
        assertThat(testChange.getLocalId()).isEqualTo(DEFAULT_LOCAL_ID);
        assertThat(testChange.getVersionId()).isEqualTo(DEFAULT_VERSION_ID);
        assertThat(testChange.getBeginLifespanVersion()).isEqualTo(DEFAULT_BEGIN_LIFESPAN_VERSION);
        assertThat(testChange.getEndLifespanVersion()).isEqualTo(DEFAULT_END_LIFESPAN_VERSION);
        assertThat(testChange.getAlternateId()).isEqualTo(DEFAULT_ALTERNATE_ID);
        assertThat(testChange.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testChange.getLocator()).isEqualTo(DEFAULT_LOCATOR);
    }

    @Test
    @Transactional
    public void createChangeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = changeRepository.findAll().size();

        // Create the Change with an existing ID
        change.setId(1L);
        ChangeDTO changeDTO = changeMapper.toDto(change);

        // An entity with an existing ID cannot be created, so this API call must fail
        restChangeMockMvc.perform(post("/api/changes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(changeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Change> changeList = changeRepository.findAll();
        assertThat(changeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNamespaceIsRequired() throws Exception {
        int databaseSizeBeforeTest = changeRepository.findAll().size();
        // set the field null
        change.setNamespace(null);

        // Create the Change, which fails.
        ChangeDTO changeDTO = changeMapper.toDto(change);

        restChangeMockMvc.perform(post("/api/changes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(changeDTO)))
            .andExpect(status().isBadRequest());

        List<Change> changeList = changeRepository.findAll();
        assertThat(changeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLocalIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = changeRepository.findAll().size();
        // set the field null
        change.setLocalId(null);

        // Create the Change, which fails.
        ChangeDTO changeDTO = changeMapper.toDto(change);

        restChangeMockMvc.perform(post("/api/changes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(changeDTO)))
            .andExpect(status().isBadRequest());

        List<Change> changeList = changeRepository.findAll();
        assertThat(changeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllChanges() throws Exception {
        // Initialize the database
        changeRepository.saveAndFlush(change);

        // Get all the changeList
        restChangeMockMvc.perform(get("/api/changes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(change.getId().intValue())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION.toString())))
            .andExpect(jsonPath("$.[*].feature").value(hasItem(DEFAULT_FEATURE.toString())))
            .andExpect(jsonPath("$.[*].namespace").value(hasItem(DEFAULT_NAMESPACE.toString())))
            .andExpect(jsonPath("$.[*].localId").value(hasItem(DEFAULT_LOCAL_ID.toString())))
            .andExpect(jsonPath("$.[*].versionId").value(hasItem(DEFAULT_VERSION_ID.toString())))
            .andExpect(jsonPath("$.[*].beginLifespanVersion").value(hasItem(DEFAULT_BEGIN_LIFESPAN_VERSION.toString())))
            .andExpect(jsonPath("$.[*].endLifespanVersion").value(hasItem(DEFAULT_END_LIFESPAN_VERSION.toString())))
            .andExpect(jsonPath("$.[*].alternateId").value(hasItem(DEFAULT_ALTERNATE_ID.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].locator").value(hasItem(DEFAULT_LOCATOR.toString())));
    }

    @Test
    @Transactional
    public void getChange() throws Exception {
        // Initialize the database
        changeRepository.saveAndFlush(change);

        // Get the change
        restChangeMockMvc.perform(get("/api/changes/{id}", change.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(change.getId().intValue()))
            .andExpect(jsonPath("$.timestamp").value(DEFAULT_TIMESTAMP.toString()))
            .andExpect(jsonPath("$.action").value(DEFAULT_ACTION.toString()))
            .andExpect(jsonPath("$.feature").value(DEFAULT_FEATURE.toString()))
            .andExpect(jsonPath("$.namespace").value(DEFAULT_NAMESPACE.toString()))
            .andExpect(jsonPath("$.localId").value(DEFAULT_LOCAL_ID.toString()))
            .andExpect(jsonPath("$.versionId").value(DEFAULT_VERSION_ID.toString()))
            .andExpect(jsonPath("$.beginLifespanVersion").value(DEFAULT_BEGIN_LIFESPAN_VERSION.toString()))
            .andExpect(jsonPath("$.endLifespanVersion").value(DEFAULT_END_LIFESPAN_VERSION.toString()))
            .andExpect(jsonPath("$.alternateId").value(DEFAULT_ALTERNATE_ID.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.locator").value(DEFAULT_LOCATOR.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingChange() throws Exception {
        // Get the change
        restChangeMockMvc.perform(get("/api/changes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChange() throws Exception {
        // Initialize the database
        changeRepository.saveAndFlush(change);
        int databaseSizeBeforeUpdate = changeRepository.findAll().size();

        // Update the change
        Change updatedChange = changeRepository.findOne(change.getId());
        updatedChange
            .timestamp(UPDATED_TIMESTAMP)
            .action(UPDATED_ACTION)
            .feature(UPDATED_FEATURE)
            .namespace(UPDATED_NAMESPACE)
            .localId(UPDATED_LOCAL_ID)
            .versionId(UPDATED_VERSION_ID)
            .beginLifespanVersion(UPDATED_BEGIN_LIFESPAN_VERSION)
            .endLifespanVersion(UPDATED_END_LIFESPAN_VERSION)
            .alternateId(UPDATED_ALTERNATE_ID)
            .type(UPDATED_TYPE)
            .locator(UPDATED_LOCATOR);
        ChangeDTO changeDTO = changeMapper.toDto(updatedChange);

        restChangeMockMvc.perform(put("/api/changes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(changeDTO)))
            .andExpect(status().isOk());

        // Validate the Change in the database
        List<Change> changeList = changeRepository.findAll();
        assertThat(changeList).hasSize(databaseSizeBeforeUpdate);
        Change testChange = changeList.get(changeList.size() - 1);
        assertThat(testChange.getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);
        assertThat(testChange.getAction()).isEqualTo(UPDATED_ACTION);
        assertThat(testChange.getFeature()).isEqualTo(UPDATED_FEATURE);
        assertThat(testChange.getNamespace()).isEqualTo(UPDATED_NAMESPACE);
        assertThat(testChange.getLocalId()).isEqualTo(UPDATED_LOCAL_ID);
        assertThat(testChange.getVersionId()).isEqualTo(UPDATED_VERSION_ID);
        assertThat(testChange.getBeginLifespanVersion()).isEqualTo(UPDATED_BEGIN_LIFESPAN_VERSION);
        assertThat(testChange.getEndLifespanVersion()).isEqualTo(UPDATED_END_LIFESPAN_VERSION);
        assertThat(testChange.getAlternateId()).isEqualTo(UPDATED_ALTERNATE_ID);
        assertThat(testChange.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testChange.getLocator()).isEqualTo(UPDATED_LOCATOR);
    }

    @Test
    @Transactional
    public void updateNonExistingChange() throws Exception {
        int databaseSizeBeforeUpdate = changeRepository.findAll().size();

        // Create the Change
        ChangeDTO changeDTO = changeMapper.toDto(change);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restChangeMockMvc.perform(put("/api/changes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(changeDTO)))
            .andExpect(status().isCreated());

        // Validate the Change in the database
        List<Change> changeList = changeRepository.findAll();
        assertThat(changeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteChange() throws Exception {
        // Initialize the database
        changeRepository.saveAndFlush(change);
        int databaseSizeBeforeDelete = changeRepository.findAll().size();

        // Get the change
        restChangeMockMvc.perform(delete("/api/changes/{id}", change.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Change> changeList = changeRepository.findAll();
        assertThat(changeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Change.class);
        Change change1 = new Change();
        change1.setId(1L);
        Change change2 = new Change();
        change2.setId(change1.getId());
        assertThat(change1).isEqualTo(change2);
        change2.setId(2L);
        assertThat(change1).isNotEqualTo(change2);
        change1.setId(null);
        assertThat(change1).isNotEqualTo(change2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChangeDTO.class);
        ChangeDTO changeDTO1 = new ChangeDTO();
        changeDTO1.setId(1L);
        ChangeDTO changeDTO2 = new ChangeDTO();
        assertThat(changeDTO1).isNotEqualTo(changeDTO2);
        changeDTO2.setId(changeDTO1.getId());
        assertThat(changeDTO1).isEqualTo(changeDTO2);
        changeDTO2.setId(2L);
        assertThat(changeDTO1).isNotEqualTo(changeDTO2);
        changeDTO1.setId(null);
        assertThat(changeDTO1).isNotEqualTo(changeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(changeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(changeMapper.fromId(null)).isNull();
    }
}
