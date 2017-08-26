package es.unizar.iaaa.pid.web.rest;

import es.unizar.iaaa.pid.PidmsApp;

import es.unizar.iaaa.pid.domain.Identifier;
import es.unizar.iaaa.pid.repository.IdentifierRepository;
import es.unizar.iaaa.pid.service.IdentifierService;
import es.unizar.iaaa.pid.service.dto.IdentifierDTO;
import es.unizar.iaaa.pid.service.mapper.IdentifierMapper;
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

/**
 * Test class for the IdentifierResource REST controller.
 *
 * @see IdentifierResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PidmsApp.class)
public class IdentifierResourceIntTest {

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

    @Autowired
    private IdentifierRepository identifierRepository;

    @Autowired
    private IdentifierMapper identifierMapper;

    @Autowired
    private IdentifierService identifierService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restIdentifierMockMvc;

    private Identifier identifier;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        IdentifierResource identifierResource = new IdentifierResource(identifierService);
        this.restIdentifierMockMvc = MockMvcBuilders.standaloneSetup(identifierResource)
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
    public static Identifier createEntity(EntityManager em) {
        Identifier identifier = new Identifier()
            .namespace(DEFAULT_NAMESPACE)
            .localId(DEFAULT_LOCAL_ID)
            .versionId(DEFAULT_VERSION_ID)
            .beginLifespanVersion(DEFAULT_BEGIN_LIFESPAN_VERSION)
            .endLifespanVersion(DEFAULT_END_LIFESPAN_VERSION)
            .alternateId(DEFAULT_ALTERNATE_ID);
        return identifier;
    }

    @Before
    public void initTest() {
        identifier = createEntity(em);
    }

    @Test
    @Transactional
    public void createIdentifier() throws Exception {
        int databaseSizeBeforeCreate = identifierRepository.findAll().size();

        // Create the Identifier
        IdentifierDTO identifierDTO = identifierMapper.toDto(identifier);
        restIdentifierMockMvc.perform(post("/api/identifiers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(identifierDTO)))
            .andExpect(status().isCreated());

        // Validate the Identifier in the database
        List<Identifier> identifierList = identifierRepository.findAll();
        assertThat(identifierList).hasSize(databaseSizeBeforeCreate + 1);
        Identifier testIdentifier = identifierList.get(identifierList.size() - 1);
        assertThat(testIdentifier.getNamespace()).isEqualTo(DEFAULT_NAMESPACE);
        assertThat(testIdentifier.getLocalId()).isEqualTo(DEFAULT_LOCAL_ID);
        assertThat(testIdentifier.getVersionId()).isEqualTo(DEFAULT_VERSION_ID);
        assertThat(testIdentifier.getBeginLifespanVersion()).isEqualTo(DEFAULT_BEGIN_LIFESPAN_VERSION);
        assertThat(testIdentifier.getEndLifespanVersion()).isEqualTo(DEFAULT_END_LIFESPAN_VERSION);
        assertThat(testIdentifier.getAlternateId()).isEqualTo(DEFAULT_ALTERNATE_ID);
    }

    @Test
    @Transactional
    public void createIdentifierWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = identifierRepository.findAll().size();

        // Create the Identifier with an existing ID
        identifier.setId(1L);
        IdentifierDTO identifierDTO = identifierMapper.toDto(identifier);

        // An entity with an existing ID cannot be created, so this API call must fail
        restIdentifierMockMvc.perform(post("/api/identifiers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(identifierDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Identifier> identifierList = identifierRepository.findAll();
        assertThat(identifierList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNamespaceIsRequired() throws Exception {
        int databaseSizeBeforeTest = identifierRepository.findAll().size();
        // set the field null
        identifier.setNamespace(null);

        // Create the Identifier, which fails.
        IdentifierDTO identifierDTO = identifierMapper.toDto(identifier);

        restIdentifierMockMvc.perform(post("/api/identifiers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(identifierDTO)))
            .andExpect(status().isBadRequest());

        List<Identifier> identifierList = identifierRepository.findAll();
        assertThat(identifierList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLocalIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = identifierRepository.findAll().size();
        // set the field null
        identifier.setLocalId(null);

        // Create the Identifier, which fails.
        IdentifierDTO identifierDTO = identifierMapper.toDto(identifier);

        restIdentifierMockMvc.perform(post("/api/identifiers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(identifierDTO)))
            .andExpect(status().isBadRequest());

        List<Identifier> identifierList = identifierRepository.findAll();
        assertThat(identifierList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllIdentifiers() throws Exception {
        // Initialize the database
        identifierRepository.saveAndFlush(identifier);

        // Get all the identifierList
        restIdentifierMockMvc.perform(get("/api/identifiers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(identifier.getId().intValue())))
            .andExpect(jsonPath("$.[*].namespace").value(hasItem(DEFAULT_NAMESPACE.toString())))
            .andExpect(jsonPath("$.[*].localId").value(hasItem(DEFAULT_LOCAL_ID.toString())))
            .andExpect(jsonPath("$.[*].versionId").value(hasItem(DEFAULT_VERSION_ID.toString())))
            .andExpect(jsonPath("$.[*].beginLifespanVersion").value(hasItem(DEFAULT_BEGIN_LIFESPAN_VERSION.toString())))
            .andExpect(jsonPath("$.[*].endLifespanVersion").value(hasItem(DEFAULT_END_LIFESPAN_VERSION.toString())))
            .andExpect(jsonPath("$.[*].alternateId").value(hasItem(DEFAULT_ALTERNATE_ID.toString())));
    }

    @Test
    @Transactional
    public void getIdentifier() throws Exception {
        // Initialize the database
        identifierRepository.saveAndFlush(identifier);

        // Get the identifier
        restIdentifierMockMvc.perform(get("/api/identifiers/{id}", identifier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(identifier.getId().intValue()))
            .andExpect(jsonPath("$.namespace").value(DEFAULT_NAMESPACE.toString()))
            .andExpect(jsonPath("$.localId").value(DEFAULT_LOCAL_ID.toString()))
            .andExpect(jsonPath("$.versionId").value(DEFAULT_VERSION_ID.toString()))
            .andExpect(jsonPath("$.beginLifespanVersion").value(DEFAULT_BEGIN_LIFESPAN_VERSION.toString()))
            .andExpect(jsonPath("$.endLifespanVersion").value(DEFAULT_END_LIFESPAN_VERSION.toString()))
            .andExpect(jsonPath("$.alternateId").value(DEFAULT_ALTERNATE_ID.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingIdentifier() throws Exception {
        // Get the identifier
        restIdentifierMockMvc.perform(get("/api/identifiers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateIdentifier() throws Exception {
        // Initialize the database
        identifierRepository.saveAndFlush(identifier);
        int databaseSizeBeforeUpdate = identifierRepository.findAll().size();

        // Update the identifier
        Identifier updatedIdentifier = identifierRepository.findOne(identifier.getId());
        updatedIdentifier
            .namespace(UPDATED_NAMESPACE)
            .localId(UPDATED_LOCAL_ID)
            .versionId(UPDATED_VERSION_ID)
            .beginLifespanVersion(UPDATED_BEGIN_LIFESPAN_VERSION)
            .endLifespanVersion(UPDATED_END_LIFESPAN_VERSION)
            .alternateId(UPDATED_ALTERNATE_ID);
        IdentifierDTO identifierDTO = identifierMapper.toDto(updatedIdentifier);

        restIdentifierMockMvc.perform(put("/api/identifiers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(identifierDTO)))
            .andExpect(status().isOk());

        // Validate the Identifier in the database
        List<Identifier> identifierList = identifierRepository.findAll();
        assertThat(identifierList).hasSize(databaseSizeBeforeUpdate);
        Identifier testIdentifier = identifierList.get(identifierList.size() - 1);
        assertThat(testIdentifier.getNamespace()).isEqualTo(UPDATED_NAMESPACE);
        assertThat(testIdentifier.getLocalId()).isEqualTo(UPDATED_LOCAL_ID);
        assertThat(testIdentifier.getVersionId()).isEqualTo(UPDATED_VERSION_ID);
        assertThat(testIdentifier.getBeginLifespanVersion()).isEqualTo(UPDATED_BEGIN_LIFESPAN_VERSION);
        assertThat(testIdentifier.getEndLifespanVersion()).isEqualTo(UPDATED_END_LIFESPAN_VERSION);
        assertThat(testIdentifier.getAlternateId()).isEqualTo(UPDATED_ALTERNATE_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingIdentifier() throws Exception {
        int databaseSizeBeforeUpdate = identifierRepository.findAll().size();

        // Create the Identifier
        IdentifierDTO identifierDTO = identifierMapper.toDto(identifier);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restIdentifierMockMvc.perform(put("/api/identifiers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(identifierDTO)))
            .andExpect(status().isCreated());

        // Validate the Identifier in the database
        List<Identifier> identifierList = identifierRepository.findAll();
        assertThat(identifierList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteIdentifier() throws Exception {
        // Initialize the database
        identifierRepository.saveAndFlush(identifier);
        int databaseSizeBeforeDelete = identifierRepository.findAll().size();

        // Get the identifier
        restIdentifierMockMvc.perform(delete("/api/identifiers/{id}", identifier.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Identifier> identifierList = identifierRepository.findAll();
        assertThat(identifierList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Identifier.class);
        Identifier identifier1 = new Identifier();
        identifier1.setId(1L);
        Identifier identifier2 = new Identifier();
        identifier2.setId(identifier1.getId());
        assertThat(identifier1).isEqualTo(identifier2);
        identifier2.setId(2L);
        assertThat(identifier1).isNotEqualTo(identifier2);
        identifier1.setId(null);
        assertThat(identifier1).isNotEqualTo(identifier2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(IdentifierDTO.class);
        IdentifierDTO identifierDTO1 = new IdentifierDTO();
        identifierDTO1.setId(1L);
        IdentifierDTO identifierDTO2 = new IdentifierDTO();
        assertThat(identifierDTO1).isNotEqualTo(identifierDTO2);
        identifierDTO2.setId(identifierDTO1.getId());
        assertThat(identifierDTO1).isEqualTo(identifierDTO2);
        identifierDTO2.setId(2L);
        assertThat(identifierDTO1).isNotEqualTo(identifierDTO2);
        identifierDTO1.setId(null);
        assertThat(identifierDTO1).isNotEqualTo(identifierDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(identifierMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(identifierMapper.fromId(null)).isNull();
    }
}
