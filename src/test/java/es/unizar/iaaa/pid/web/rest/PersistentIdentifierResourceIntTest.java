package es.unizar.iaaa.pid.web.rest;

import es.unizar.iaaa.pid.PidmsApp;

import es.unizar.iaaa.pid.domain.PersistentIdentifier;
import es.unizar.iaaa.pid.repository.PersistentIdentifierRepository;
import es.unizar.iaaa.pid.service.PersistentIdentifierService;
import es.unizar.iaaa.pid.service.dto.PersistentIdentifierDTO;
import es.unizar.iaaa.pid.service.mapper.PersistentIdentifierMapper;
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

import es.unizar.iaaa.pid.domain.enumeration.ResourceType;
import es.unizar.iaaa.pid.domain.enumeration.ProcessStatus;
import es.unizar.iaaa.pid.domain.enumeration.ItemStatus;
/**
 * Test class for the PersistentIdentifierResource REST controller.
 *
 * @see PersistentIdentifierResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PidmsApp.class)
public class PersistentIdentifierResourceIntTest {

    private static final String DEFAULT_EXTERNAL = "AAAAAAAAAA";
    private static final String UPDATED_EXTERNAL = "BBBBBBBBBB";

    private static final String DEFAULT_FEATURE = "AAAAAAAAAA";
    private static final String UPDATED_FEATURE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_RESOLVER_PROXY_MODE = false;
    private static final Boolean UPDATED_RESOLVER_PROXY_MODE = true;

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

    private static final ProcessStatus DEFAULT_PROCESS_STATUS = ProcessStatus.PENDING_PREPARING_HARVEST;
    private static final ProcessStatus UPDATED_PROCESS_STATUS = ProcessStatus.PREPARING_HARVEST;

    private static final ItemStatus DEFAULT_ITEM_STATUS = ItemStatus.ISSUED;
    private static final ItemStatus UPDATED_ITEM_STATUS = ItemStatus.LAPSED;

    private static final Instant DEFAULT_LAST_CHANGE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_CHANGE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_REGISTRATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REGISTRATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_REVISION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_REVISION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_NEXT_RENEWAL_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_NEXT_RENEWAL_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_ANNULLATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ANNULLATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private PersistentIdentifierRepository persistentIdentifierRepository;

    @Autowired
    private PersistentIdentifierMapper persistentIdentifierMapper;

    @Autowired
    private PersistentIdentifierService persistentIdentifierService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPersistentIdentifierMockMvc;

    private PersistentIdentifier persistentIdentifier;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PersistentIdentifierResource persistentIdentifierResource = new PersistentIdentifierResource(persistentIdentifierService);
        this.restPersistentIdentifierMockMvc = MockMvcBuilders.standaloneSetup(persistentIdentifierResource)
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
    public static PersistentIdentifier createEntity(EntityManager em) {
        PersistentIdentifier persistentIdentifier = new PersistentIdentifier()
            .external(DEFAULT_EXTERNAL)
            .feature(DEFAULT_FEATURE)
            .resolverProxyMode(DEFAULT_RESOLVER_PROXY_MODE)
            .namespace(DEFAULT_NAMESPACE)
            .localId(DEFAULT_LOCAL_ID)
            .versionId(DEFAULT_VERSION_ID)
            .beginLifespanVersion(DEFAULT_BEGIN_LIFESPAN_VERSION)
            .endLifespanVersion(DEFAULT_END_LIFESPAN_VERSION)
            .alternateId(DEFAULT_ALTERNATE_ID)
            .type(DEFAULT_TYPE)
            .locator(DEFAULT_LOCATOR)
            .processStatus(DEFAULT_PROCESS_STATUS)
            .itemStatus(DEFAULT_ITEM_STATUS)
            .lastChangeDate(DEFAULT_LAST_CHANGE_DATE)
            .registrationDate(DEFAULT_REGISTRATION_DATE)
            .lastRevisionDate(DEFAULT_LAST_REVISION_DATE)
            .nextRenewalDate(DEFAULT_NEXT_RENEWAL_DATE)
            .annullationDate(DEFAULT_ANNULLATION_DATE);
        return persistentIdentifier;
    }

    @Before
    public void initTest() {
        persistentIdentifier = createEntity(em);
    }

    @Test
    @Transactional
    public void createPersistentIdentifier() throws Exception {
        int databaseSizeBeforeCreate = persistentIdentifierRepository.findAll().size();

        // Create the PersistentIdentifier
        PersistentIdentifierDTO persistentIdentifierDTO = persistentIdentifierMapper.toDto(persistentIdentifier);
        restPersistentIdentifierMockMvc.perform(post("/api/persistent-identifiers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(persistentIdentifierDTO)))
            .andExpect(status().isCreated());

        // Validate the PersistentIdentifier in the database
        List<PersistentIdentifier> persistentIdentifierList = persistentIdentifierRepository.findAll();
        assertThat(persistentIdentifierList).hasSize(databaseSizeBeforeCreate + 1);
        PersistentIdentifier testPersistentIdentifier = persistentIdentifierList.get(persistentIdentifierList.size() - 1);
        assertThat(testPersistentIdentifier.getExternal()).isEqualTo(DEFAULT_EXTERNAL);
        assertThat(testPersistentIdentifier.getFeature()).isEqualTo(DEFAULT_FEATURE);
        assertThat(testPersistentIdentifier.isResolverProxyMode()).isEqualTo(DEFAULT_RESOLVER_PROXY_MODE);
        assertThat(testPersistentIdentifier.getNamespace()).isEqualTo(DEFAULT_NAMESPACE);
        assertThat(testPersistentIdentifier.getLocalId()).isEqualTo(DEFAULT_LOCAL_ID);
        assertThat(testPersistentIdentifier.getVersionId()).isEqualTo(DEFAULT_VERSION_ID);
        assertThat(testPersistentIdentifier.getBeginLifespanVersion()).isEqualTo(DEFAULT_BEGIN_LIFESPAN_VERSION);
        assertThat(testPersistentIdentifier.getEndLifespanVersion()).isEqualTo(DEFAULT_END_LIFESPAN_VERSION);
        assertThat(testPersistentIdentifier.getAlternateId()).isEqualTo(DEFAULT_ALTERNATE_ID);
        assertThat(testPersistentIdentifier.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testPersistentIdentifier.getLocator()).isEqualTo(DEFAULT_LOCATOR);
        assertThat(testPersistentIdentifier.getProcessStatus()).isEqualTo(DEFAULT_PROCESS_STATUS);
        assertThat(testPersistentIdentifier.getItemStatus()).isEqualTo(DEFAULT_ITEM_STATUS);
        assertThat(testPersistentIdentifier.getLastChangeDate()).isEqualTo(DEFAULT_LAST_CHANGE_DATE);
        assertThat(testPersistentIdentifier.getRegistrationDate()).isEqualTo(DEFAULT_REGISTRATION_DATE);
        assertThat(testPersistentIdentifier.getLastRevisionDate()).isEqualTo(DEFAULT_LAST_REVISION_DATE);
        assertThat(testPersistentIdentifier.getNextRenewalDate()).isEqualTo(DEFAULT_NEXT_RENEWAL_DATE);
        assertThat(testPersistentIdentifier.getAnnullationDate()).isEqualTo(DEFAULT_ANNULLATION_DATE);
    }

    @Test
    @Transactional
    public void createPersistentIdentifierWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = persistentIdentifierRepository.findAll().size();

        // Create the PersistentIdentifier with an existing ID
        persistentIdentifier.setId(1L);
        PersistentIdentifierDTO persistentIdentifierDTO = persistentIdentifierMapper.toDto(persistentIdentifier);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPersistentIdentifierMockMvc.perform(post("/api/persistent-identifiers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(persistentIdentifierDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<PersistentIdentifier> persistentIdentifierList = persistentIdentifierRepository.findAll();
        assertThat(persistentIdentifierList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkExternalIsRequired() throws Exception {
        int databaseSizeBeforeTest = persistentIdentifierRepository.findAll().size();
        // set the field null
        persistentIdentifier.setExternal(null);

        // Create the PersistentIdentifier, which fails.
        PersistentIdentifierDTO persistentIdentifierDTO = persistentIdentifierMapper.toDto(persistentIdentifier);

        restPersistentIdentifierMockMvc.perform(post("/api/persistent-identifiers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(persistentIdentifierDTO)))
            .andExpect(status().isBadRequest());

        List<PersistentIdentifier> persistentIdentifierList = persistentIdentifierRepository.findAll();
        assertThat(persistentIdentifierList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFeatureIsRequired() throws Exception {
        int databaseSizeBeforeTest = persistentIdentifierRepository.findAll().size();
        // set the field null
        persistentIdentifier.setFeature(null);

        // Create the PersistentIdentifier, which fails.
        PersistentIdentifierDTO persistentIdentifierDTO = persistentIdentifierMapper.toDto(persistentIdentifier);

        restPersistentIdentifierMockMvc.perform(post("/api/persistent-identifiers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(persistentIdentifierDTO)))
            .andExpect(status().isBadRequest());

        List<PersistentIdentifier> persistentIdentifierList = persistentIdentifierRepository.findAll();
        assertThat(persistentIdentifierList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNamespaceIsRequired() throws Exception {
        int databaseSizeBeforeTest = persistentIdentifierRepository.findAll().size();
        // set the field null
        persistentIdentifier.setNamespace(null);

        // Create the PersistentIdentifier, which fails.
        PersistentIdentifierDTO persistentIdentifierDTO = persistentIdentifierMapper.toDto(persistentIdentifier);

        restPersistentIdentifierMockMvc.perform(post("/api/persistent-identifiers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(persistentIdentifierDTO)))
            .andExpect(status().isBadRequest());

        List<PersistentIdentifier> persistentIdentifierList = persistentIdentifierRepository.findAll();
        assertThat(persistentIdentifierList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLocalIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = persistentIdentifierRepository.findAll().size();
        // set the field null
        persistentIdentifier.setLocalId(null);

        // Create the PersistentIdentifier, which fails.
        PersistentIdentifierDTO persistentIdentifierDTO = persistentIdentifierMapper.toDto(persistentIdentifier);

        restPersistentIdentifierMockMvc.perform(post("/api/persistent-identifiers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(persistentIdentifierDTO)))
            .andExpect(status().isBadRequest());

        List<PersistentIdentifier> persistentIdentifierList = persistentIdentifierRepository.findAll();
        assertThat(persistentIdentifierList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPersistentIdentifiers() throws Exception {
        // Initialize the database
        persistentIdentifierRepository.saveAndFlush(persistentIdentifier);

        // Get all the persistentIdentifierList
        restPersistentIdentifierMockMvc.perform(get("/api/persistent-identifiers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(persistentIdentifier.getId().intValue())))
            .andExpect(jsonPath("$.[*].external").value(hasItem(DEFAULT_EXTERNAL.toString())))
            .andExpect(jsonPath("$.[*].feature").value(hasItem(DEFAULT_FEATURE.toString())))
            .andExpect(jsonPath("$.[*].resolverProxyMode").value(hasItem(DEFAULT_RESOLVER_PROXY_MODE.booleanValue())))
            .andExpect(jsonPath("$.[*].namespace").value(hasItem(DEFAULT_NAMESPACE.toString())))
            .andExpect(jsonPath("$.[*].localId").value(hasItem(DEFAULT_LOCAL_ID.toString())))
            .andExpect(jsonPath("$.[*].versionId").value(hasItem(DEFAULT_VERSION_ID.toString())))
            .andExpect(jsonPath("$.[*].beginLifespanVersion").value(hasItem(DEFAULT_BEGIN_LIFESPAN_VERSION.toString())))
            .andExpect(jsonPath("$.[*].endLifespanVersion").value(hasItem(DEFAULT_END_LIFESPAN_VERSION.toString())))
            .andExpect(jsonPath("$.[*].alternateId").value(hasItem(DEFAULT_ALTERNATE_ID.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].locator").value(hasItem(DEFAULT_LOCATOR.toString())))
            .andExpect(jsonPath("$.[*].processStatus").value(hasItem(DEFAULT_PROCESS_STATUS.toString())))
            .andExpect(jsonPath("$.[*].itemStatus").value(hasItem(DEFAULT_ITEM_STATUS.toString())))
            .andExpect(jsonPath("$.[*].lastChangeDate").value(hasItem(DEFAULT_LAST_CHANGE_DATE.toString())))
            .andExpect(jsonPath("$.[*].registrationDate").value(hasItem(DEFAULT_REGISTRATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastRevisionDate").value(hasItem(DEFAULT_LAST_REVISION_DATE.toString())))
            .andExpect(jsonPath("$.[*].nextRenewalDate").value(hasItem(DEFAULT_NEXT_RENEWAL_DATE.toString())))
            .andExpect(jsonPath("$.[*].annullationDate").value(hasItem(DEFAULT_ANNULLATION_DATE.toString())));
    }

    @Test
    @Transactional
    public void getPersistentIdentifier() throws Exception {
        // Initialize the database
        persistentIdentifierRepository.saveAndFlush(persistentIdentifier);

        // Get the persistentIdentifier
        restPersistentIdentifierMockMvc.perform(get("/api/persistent-identifiers/{id}", persistentIdentifier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(persistentIdentifier.getId().intValue()))
            .andExpect(jsonPath("$.external").value(DEFAULT_EXTERNAL.toString()))
            .andExpect(jsonPath("$.feature").value(DEFAULT_FEATURE.toString()))
            .andExpect(jsonPath("$.resolverProxyMode").value(DEFAULT_RESOLVER_PROXY_MODE.booleanValue()))
            .andExpect(jsonPath("$.namespace").value(DEFAULT_NAMESPACE.toString()))
            .andExpect(jsonPath("$.localId").value(DEFAULT_LOCAL_ID.toString()))
            .andExpect(jsonPath("$.versionId").value(DEFAULT_VERSION_ID.toString()))
            .andExpect(jsonPath("$.beginLifespanVersion").value(DEFAULT_BEGIN_LIFESPAN_VERSION.toString()))
            .andExpect(jsonPath("$.endLifespanVersion").value(DEFAULT_END_LIFESPAN_VERSION.toString()))
            .andExpect(jsonPath("$.alternateId").value(DEFAULT_ALTERNATE_ID.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.locator").value(DEFAULT_LOCATOR.toString()))
            .andExpect(jsonPath("$.processStatus").value(DEFAULT_PROCESS_STATUS.toString()))
            .andExpect(jsonPath("$.itemStatus").value(DEFAULT_ITEM_STATUS.toString()))
            .andExpect(jsonPath("$.lastChangeDate").value(DEFAULT_LAST_CHANGE_DATE.toString()))
            .andExpect(jsonPath("$.registrationDate").value(DEFAULT_REGISTRATION_DATE.toString()))
            .andExpect(jsonPath("$.lastRevisionDate").value(DEFAULT_LAST_REVISION_DATE.toString()))
            .andExpect(jsonPath("$.nextRenewalDate").value(DEFAULT_NEXT_RENEWAL_DATE.toString()))
            .andExpect(jsonPath("$.annullationDate").value(DEFAULT_ANNULLATION_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPersistentIdentifier() throws Exception {
        // Get the persistentIdentifier
        restPersistentIdentifierMockMvc.perform(get("/api/persistent-identifiers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePersistentIdentifier() throws Exception {
        // Initialize the database
        persistentIdentifierRepository.saveAndFlush(persistentIdentifier);
        int databaseSizeBeforeUpdate = persistentIdentifierRepository.findAll().size();

        // Update the persistentIdentifier
        PersistentIdentifier updatedPersistentIdentifier = persistentIdentifierRepository.findOne(persistentIdentifier.getId());
        updatedPersistentIdentifier
            .external(UPDATED_EXTERNAL)
            .feature(UPDATED_FEATURE)
            .resolverProxyMode(UPDATED_RESOLVER_PROXY_MODE)
            .namespace(UPDATED_NAMESPACE)
            .localId(UPDATED_LOCAL_ID)
            .versionId(UPDATED_VERSION_ID)
            .beginLifespanVersion(UPDATED_BEGIN_LIFESPAN_VERSION)
            .endLifespanVersion(UPDATED_END_LIFESPAN_VERSION)
            .alternateId(UPDATED_ALTERNATE_ID)
            .type(UPDATED_TYPE)
            .locator(UPDATED_LOCATOR)
            .processStatus(UPDATED_PROCESS_STATUS)
            .itemStatus(UPDATED_ITEM_STATUS)
            .lastChangeDate(UPDATED_LAST_CHANGE_DATE)
            .registrationDate(UPDATED_REGISTRATION_DATE)
            .lastRevisionDate(UPDATED_LAST_REVISION_DATE)
            .nextRenewalDate(UPDATED_NEXT_RENEWAL_DATE)
            .annullationDate(UPDATED_ANNULLATION_DATE);
        PersistentIdentifierDTO persistentIdentifierDTO = persistentIdentifierMapper.toDto(updatedPersistentIdentifier);

        restPersistentIdentifierMockMvc.perform(put("/api/persistent-identifiers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(persistentIdentifierDTO)))
            .andExpect(status().isOk());

        // Validate the PersistentIdentifier in the database
        List<PersistentIdentifier> persistentIdentifierList = persistentIdentifierRepository.findAll();
        assertThat(persistentIdentifierList).hasSize(databaseSizeBeforeUpdate);
        PersistentIdentifier testPersistentIdentifier = persistentIdentifierList.get(persistentIdentifierList.size() - 1);
        assertThat(testPersistentIdentifier.getExternal()).isEqualTo(UPDATED_EXTERNAL);
        assertThat(testPersistentIdentifier.getFeature()).isEqualTo(UPDATED_FEATURE);
        assertThat(testPersistentIdentifier.isResolverProxyMode()).isEqualTo(UPDATED_RESOLVER_PROXY_MODE);
        assertThat(testPersistentIdentifier.getNamespace()).isEqualTo(UPDATED_NAMESPACE);
        assertThat(testPersistentIdentifier.getLocalId()).isEqualTo(UPDATED_LOCAL_ID);
        assertThat(testPersistentIdentifier.getVersionId()).isEqualTo(UPDATED_VERSION_ID);
        assertThat(testPersistentIdentifier.getBeginLifespanVersion()).isEqualTo(UPDATED_BEGIN_LIFESPAN_VERSION);
        assertThat(testPersistentIdentifier.getEndLifespanVersion()).isEqualTo(UPDATED_END_LIFESPAN_VERSION);
        assertThat(testPersistentIdentifier.getAlternateId()).isEqualTo(UPDATED_ALTERNATE_ID);
        assertThat(testPersistentIdentifier.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testPersistentIdentifier.getLocator()).isEqualTo(UPDATED_LOCATOR);
        assertThat(testPersistentIdentifier.getProcessStatus()).isEqualTo(UPDATED_PROCESS_STATUS);
        assertThat(testPersistentIdentifier.getItemStatus()).isEqualTo(UPDATED_ITEM_STATUS);
        assertThat(testPersistentIdentifier.getLastChangeDate()).isEqualTo(UPDATED_LAST_CHANGE_DATE);
        assertThat(testPersistentIdentifier.getRegistrationDate()).isEqualTo(UPDATED_REGISTRATION_DATE);
        assertThat(testPersistentIdentifier.getLastRevisionDate()).isEqualTo(UPDATED_LAST_REVISION_DATE);
        assertThat(testPersistentIdentifier.getNextRenewalDate()).isEqualTo(UPDATED_NEXT_RENEWAL_DATE);
        assertThat(testPersistentIdentifier.getAnnullationDate()).isEqualTo(UPDATED_ANNULLATION_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingPersistentIdentifier() throws Exception {
        int databaseSizeBeforeUpdate = persistentIdentifierRepository.findAll().size();

        // Create the PersistentIdentifier
        PersistentIdentifierDTO persistentIdentifierDTO = persistentIdentifierMapper.toDto(persistentIdentifier);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPersistentIdentifierMockMvc.perform(put("/api/persistent-identifiers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(persistentIdentifierDTO)))
            .andExpect(status().isCreated());

        // Validate the PersistentIdentifier in the database
        List<PersistentIdentifier> persistentIdentifierList = persistentIdentifierRepository.findAll();
        assertThat(persistentIdentifierList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePersistentIdentifier() throws Exception {
        // Initialize the database
        persistentIdentifierRepository.saveAndFlush(persistentIdentifier);
        int databaseSizeBeforeDelete = persistentIdentifierRepository.findAll().size();

        // Get the persistentIdentifier
        restPersistentIdentifierMockMvc.perform(delete("/api/persistent-identifiers/{id}", persistentIdentifier.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PersistentIdentifier> persistentIdentifierList = persistentIdentifierRepository.findAll();
        assertThat(persistentIdentifierList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PersistentIdentifier.class);
        PersistentIdentifier persistentIdentifier1 = new PersistentIdentifier();
        persistentIdentifier1.setId(1L);
        PersistentIdentifier persistentIdentifier2 = new PersistentIdentifier();
        persistentIdentifier2.setId(persistentIdentifier1.getId());
        assertThat(persistentIdentifier1).isEqualTo(persistentIdentifier2);
        persistentIdentifier2.setId(2L);
        assertThat(persistentIdentifier1).isNotEqualTo(persistentIdentifier2);
        persistentIdentifier1.setId(null);
        assertThat(persistentIdentifier1).isNotEqualTo(persistentIdentifier2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PersistentIdentifierDTO.class);
        PersistentIdentifierDTO persistentIdentifierDTO1 = new PersistentIdentifierDTO();
        persistentIdentifierDTO1.setId(1L);
        PersistentIdentifierDTO persistentIdentifierDTO2 = new PersistentIdentifierDTO();
        assertThat(persistentIdentifierDTO1).isNotEqualTo(persistentIdentifierDTO2);
        persistentIdentifierDTO2.setId(persistentIdentifierDTO1.getId());
        assertThat(persistentIdentifierDTO1).isEqualTo(persistentIdentifierDTO2);
        persistentIdentifierDTO2.setId(2L);
        assertThat(persistentIdentifierDTO1).isNotEqualTo(persistentIdentifierDTO2);
        persistentIdentifierDTO1.setId(null);
        assertThat(persistentIdentifierDTO1).isNotEqualTo(persistentIdentifierDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(persistentIdentifierMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(persistentIdentifierMapper.fromId(null)).isNull();
    }
}
