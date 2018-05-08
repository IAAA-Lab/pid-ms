package es.unizar.iaaa.pid.web.rest;

import es.unizar.iaaa.pid.PidmsApp;
import es.unizar.iaaa.pid.config.ApplicationProperties;
import es.unizar.iaaa.pid.domain.Feature;
import es.unizar.iaaa.pid.domain.Namespace;
import es.unizar.iaaa.pid.domain.PersistentIdentifier;
import es.unizar.iaaa.pid.service.FeatureDTOService;
import es.unizar.iaaa.pid.service.NamespaceDTOService;
import es.unizar.iaaa.pid.service.OrganizationDTOService;
import es.unizar.iaaa.pid.service.OrganizationMemberDTOService;
import es.unizar.iaaa.pid.service.PersistentIdentifierDTOService;
import es.unizar.iaaa.pid.service.dto.PersistentIdentifierDTO;
import es.unizar.iaaa.pid.service.mapper.FeatureMapper;
import es.unizar.iaaa.pid.service.mapper.NamespaceMapper;
import es.unizar.iaaa.pid.service.mapper.OrganizationMapper;
import es.unizar.iaaa.pid.service.mapper.PersistentIdentifierMapper;
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

import java.util.UUID;

import static es.unizar.iaaa.pid.web.rest.util.HeaderUtil.ERROR_ID_ALREADY_EXIST;
import static es.unizar.iaaa.pid.web.rest.util.ResourceFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PersistentIdentifierResource REST controller.
 *
 * @see PersistentIdentifierResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PidmsApp.class)
@Transactional
@WithMockUser(username=UserResourceIntTest.DEFAULT_LOGIN,password=UserResourceIntTest.DEFAULT_PASSWORD)
public class PersistentIdentifierResourceIntTest extends LoggedUser {

    @Autowired
    private FeatureDTOService featureDTOService;

    @Autowired
    private FeatureMapper featureMapper;

    @Autowired
    private OrganizationDTOService organizationDTOService;

    @Autowired
    private OrganizationMemberDTOService organizationMemberDTOService;

    @Autowired
    private OrganizationMapper organizationMapper;

    @Autowired
    private PersistentIdentifierMapper persistentIdentifierMapper;

    @Autowired
    private PersistentIdentifierDTOService persistentIdentifierService;

    @Autowired
    private NamespaceDTOService namespaceDTOService;

    @Autowired
    private NamespaceMapper namespaceMapper;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private ApplicationProperties applicationProperties;

    private MockMvc restPersistentIdentifierMockMvc;

    private PersistentIdentifier persistentIdentifier;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PersistentIdentifierResource persistentIdentifierResource = new PersistentIdentifierResource(persistentIdentifierService,
        		featureDTOService, namespaceDTOService, organizationMemberDTOService, applicationProperties);
        this.restPersistentIdentifierMockMvc = MockMvcBuilders.standaloneSetup(persistentIdentifierResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }


    @Before
    public void initTest() {
        Namespace namespace = namespace();
        Long organizationId = organizationDTOService.save(organizationMapper.toDto(namespace.getOwner())).getId();

        namespace.getOwner().setId(organizationId);

        Long namespaceId = namespaceDTOService.save(namespaceMapper.toDto(namespace)).getId();
        namespace.setId(namespaceId);

        Feature feature = feature(namespace);
        Long featureId = featureDTOService.save(featureMapper.toDto(feature)).getId();
        feature.setId(featureId);

        persistentIdentifier = persistentIdentifier(feature);
    }

    @Test
    public void createPersistentIdentifier() throws Exception {
        // Create the PersistentIdentifier
        PersistentIdentifierDTO persistentIdentifierDTO = persistentIdentifierMapper.toDto(persistentIdentifier);

        MvcResult result = restPersistentIdentifierMockMvc.perform(post("/api/persistent-identifiers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(persistentIdentifierDTO)))
            .andExpect(status().isCreated())
            .andReturn();

        // Extract current id from Location
        UUID id = MvcResultUtils.extractUuidFromLocation(result);

        // Validate the PersistentIdentifier in the database
        PersistentIdentifier testPersistentIdentifier = persistentIdentifierMapper.toEntity(persistentIdentifierService.findOne(id));
        assertThat(testPersistentIdentifier.getExternalUrn()).isEqualTo(DEFAULT_EXTERNAL_URN);
        assertThat(testPersistentIdentifier.getFeature().getId()).isEqualTo(persistentIdentifier.getFeature().getId());
        assertThat(testPersistentIdentifier.isResolverProxyMode()).isEqualTo(DEFAULT_RESOLVER_PROXY_MODE);
        assertThat(testPersistentIdentifier.getIdentifier().getNamespace()).isEqualTo(DEFAULT_NAMESPACE);
        assertThat(testPersistentIdentifier.getIdentifier().getLocalId()).isEqualTo(DEFAULT_LOCAL_ID);
        assertThat(testPersistentIdentifier.getIdentifier().getVersionId()).isEqualTo(DEFAULT_VERSION_ID);
        assertThat(testPersistentIdentifier.getIdentifier().getBeginLifespanVersion()).isEqualTo(DEFAULT_BEGIN_LIFESPAN_VERSION);
        assertThat(testPersistentIdentifier.getIdentifier().getEndLifespanVersion()).isEqualTo(DEFAULT_END_LIFESPAN_VERSION);
        assertThat(testPersistentIdentifier.getIdentifier().getAlternateId()).isEqualTo(DEFAULT_ALTERNATE_ID);
        assertThat(testPersistentIdentifier.getResource().getResourceType()).isEqualTo(DEFAULT_RESOURCE_TYPE);
        assertThat(testPersistentIdentifier.getResource().getLocator()).isEqualTo(DEFAULT_LOCATOR);
        assertThat(testPersistentIdentifier.getRegistration().getProcessStatus()).isEqualTo(DEFAULT_PROCESS_STATUS);
        assertThat(testPersistentIdentifier.getRegistration().getItemStatus()).isEqualTo(DEFAULT_ITEM_STATUS);
        assertThat(testPersistentIdentifier.getRegistration().getLastChangeDate()).isEqualTo(DEFAULT_LAST_CHANGE_DATE);
        assertThat(testPersistentIdentifier.getRegistration().getRegistrationDate()).isEqualTo(DEFAULT_REGISTRATION_DATE);
        assertThat(testPersistentIdentifier.getRegistration().getLastRevisionDate()).isEqualTo(DEFAULT_LAST_REVISION_DATE);
        assertThat(testPersistentIdentifier.getRegistration().getNextRenewalDate()).isEqualTo(DEFAULT_NEXT_RENEWAL_DATE);
        assertThat(testPersistentIdentifier.getRegistration().getAnnullationDate()).isEqualTo(DEFAULT_ANNULLATION_DATE);
    }

    @Test
    public void createPersistentIdentifierWithIdMustFail() throws Exception {
        // Create the PersistentIdentifier with an existing ID
        persistentIdentifier.autoId();
        PersistentIdentifierDTO persistentIdentifierDTO = persistentIdentifierMapper.toDto(persistentIdentifier);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPersistentIdentifierMockMvc.perform(post("/api/persistent-identifiers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(persistentIdentifierDTO)))
            .andExpect(status().isBadRequest())
            .andExpect(header().string(ERROR_HEADER,ERROR_ID_ALREADY_EXIST));
    }

    @Test
    public void checkFeatureIsRequired() throws Exception {
        // set the field null
        persistentIdentifier.setFeature(null);
        PersistentIdentifierDTO persistentIdentifierDTO = persistentIdentifierMapper.toDto(persistentIdentifier);

        restPersistentIdentifierMockMvc.perform(post("/api/persistent-identifiers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(persistentIdentifierDTO)))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void checkNamespaceIsRequired() throws Exception {
        // set the field null
        persistentIdentifier.getIdentifier().setNamespace(null);
        PersistentIdentifierDTO persistentIdentifierDTO = persistentIdentifierMapper.toDto(persistentIdentifier);

        restPersistentIdentifierMockMvc.perform(post("/api/persistent-identifiers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(persistentIdentifierDTO)))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void checkLocalIdIsRequired() throws Exception {
        // set the field null
        persistentIdentifier.getIdentifier().setLocalId(null);
        PersistentIdentifierDTO persistentIdentifierDTO = persistentIdentifierMapper.toDto(persistentIdentifier);

        restPersistentIdentifierMockMvc.perform(post("/api/persistent-identifiers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(persistentIdentifierDTO)))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void getAllPersistentIdentifiers() throws Exception {
        // Initialize the database
        PersistentIdentifierDTO persistentIdentifierDTO = persistentIdentifierMapper.toDto(persistentIdentifier);
        persistentIdentifierDTO = persistentIdentifierService.save(persistentIdentifierDTO);

        // Get all the persistentIdentifierList
        restPersistentIdentifierMockMvc.perform(get("/api/persistent-identifiers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(persistentIdentifierDTO.getId().toString())))
            .andExpect(jsonPath("$.[*].externalUrn").value(hasItem(DEFAULT_EXTERNAL_URN)))
            .andExpect(jsonPath("$.[*].featureId").value(hasItem(persistentIdentifierDTO.getFeatureId().intValue())))
            .andExpect(jsonPath("$.[*].resolverProxyMode").value(hasItem(DEFAULT_RESOLVER_PROXY_MODE)))
            .andExpect(jsonPath("$.[*].namespace").value(hasItem(DEFAULT_NAMESPACE)))
            .andExpect(jsonPath("$.[*].localId").value(hasItem(DEFAULT_LOCAL_ID)))
            .andExpect(jsonPath("$.[*].versionId").value(hasItem(DEFAULT_VERSION_ID)))
            .andExpect(jsonPath("$.[*].beginLifespanVersion").value(hasItem(DEFAULT_BEGIN_LIFESPAN_VERSION.toString())))
            .andExpect(jsonPath("$.[*].endLifespanVersion").value(hasItem(DEFAULT_END_LIFESPAN_VERSION.toString())))
            .andExpect(jsonPath("$.[*].alternateId").value(hasItem(DEFAULT_ALTERNATE_ID)))
            .andExpect(jsonPath("$.[*].resourceType").value(hasItem(DEFAULT_RESOURCE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].locator").value(hasItem(DEFAULT_LOCATOR)))
            .andExpect(jsonPath("$.[*].processStatus").value(hasItem(DEFAULT_PROCESS_STATUS.toString())))
            .andExpect(jsonPath("$.[*].itemStatus").value(hasItem(DEFAULT_ITEM_STATUS.toString())))
            .andExpect(jsonPath("$.[*].lastChangeDate").value(hasItem(DEFAULT_LAST_CHANGE_DATE.toString())))
            .andExpect(jsonPath("$.[*].registrationDate").value(hasItem(DEFAULT_REGISTRATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastRevisionDate").value(hasItem(DEFAULT_LAST_REVISION_DATE.toString())))
            .andExpect(jsonPath("$.[*].nextRenewalDate").value(hasItem(DEFAULT_NEXT_RENEWAL_DATE.toString())))
            .andExpect(jsonPath("$.[*].annullationDate").value(hasItem(DEFAULT_ANNULLATION_DATE.toString())));
    }

    @Test
    public void getPersistentIdentifier() throws Exception {
        // Initialize the database
        PersistentIdentifierDTO persistentIdentifierDTO = persistentIdentifierMapper.toDto(persistentIdentifier);
        persistentIdentifierDTO = persistentIdentifierService.save(persistentIdentifierDTO);

        // Get the persistentIdentifier
        restPersistentIdentifierMockMvc.perform(get("/api/persistent-identifiers/{id}", persistentIdentifierDTO.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andDo(print())
            .andExpect(jsonPath("$.id").value(persistentIdentifierDTO.getId().toString()))
            .andExpect(jsonPath("$.externalUrn").value(DEFAULT_EXTERNAL_URN))
            .andExpect(jsonPath("$.featureId").value(persistentIdentifierDTO.getFeatureId().intValue()))
            .andExpect(jsonPath("$.resolverProxyMode").value(DEFAULT_RESOLVER_PROXY_MODE))
            .andExpect(jsonPath("$.namespace").value(DEFAULT_NAMESPACE))
            .andExpect(jsonPath("$.localId").value(DEFAULT_LOCAL_ID))
            .andExpect(jsonPath("$.versionId").value(DEFAULT_VERSION_ID))
            .andExpect(jsonPath("$.beginLifespanVersion").value(DEFAULT_BEGIN_LIFESPAN_VERSION.toString()))
            .andExpect(jsonPath("$.endLifespanVersion").value(DEFAULT_END_LIFESPAN_VERSION.toString()))
            .andExpect(jsonPath("$.alternateId").value(DEFAULT_ALTERNATE_ID))
            .andExpect(jsonPath("$.resourceType").value(DEFAULT_RESOURCE_TYPE.toString()))
            .andExpect(jsonPath("$.locator").value(DEFAULT_LOCATOR))
            .andExpect(jsonPath("$.processStatus").value(DEFAULT_PROCESS_STATUS.toString()))
            .andExpect(jsonPath("$.itemStatus").value(DEFAULT_ITEM_STATUS.toString()))
            .andExpect(jsonPath("$.lastChangeDate").value(DEFAULT_LAST_CHANGE_DATE.toString()))
            .andExpect(jsonPath("$.registrationDate").value(DEFAULT_REGISTRATION_DATE.toString()))
            .andExpect(jsonPath("$.lastRevisionDate").value(DEFAULT_LAST_REVISION_DATE.toString()))
            .andExpect(jsonPath("$.nextRenewalDate").value(DEFAULT_NEXT_RENEWAL_DATE.toString()))
            .andExpect(jsonPath("$.annullationDate").value(DEFAULT_ANNULLATION_DATE.toString()));
    }

    @Test
    public void getNonExistingPersistentIdentifier() throws Exception {
        // Get the persistentIdentifier
        restPersistentIdentifierMockMvc.perform(get("/api/persistent-identifiers/{id}", UUID_1))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updatePersistentIdentifier() throws Exception {
        // Initialize the database
        PersistentIdentifierDTO persistentIdentifierDTO = persistentIdentifierMapper.toDto(persistentIdentifier);
        persistentIdentifierDTO = persistentIdentifierService.save(persistentIdentifierDTO);

        // Update the persistentIdentifier
        PersistentIdentifier updatedPersistentIdentifier = persistentIdentifierMapper.toEntity(
            persistentIdentifierService.findOne(persistentIdentifierDTO.getId()));
        updatedPersistentIdentifier
            .resolverProxyMode(UPDATED_RESOLVER_PROXY_MODE)
            .getResource()
            .resourceType(UPDATED_RESOURCE_TYPE)
            .locator(UPDATED_LOCATOR);

        updatedPersistentIdentifier.getIdentifier()
            .beginLifespanVersion(UPDATED_BEGIN_LIFESPAN_VERSION)
            .endLifespanVersion(UPDATED_END_LIFESPAN_VERSION)
            .alternateId(UPDATED_ALTERNATE_ID);

        updatedPersistentIdentifier.getRegistration()
            .processStatus(UPDATED_PROCESS_STATUS)
            .itemStatus(UPDATED_ITEM_STATUS)
            .lastChangeDate(UPDATED_LAST_CHANGE_DATE)
            .registrationDate(UPDATED_REGISTRATION_DATE)
            .lastRevisionDate(UPDATED_LAST_REVISION_DATE)
            .nextRenewalDate(UPDATED_NEXT_RENEWAL_DATE)
            .annullationDate(UPDATED_ANNULLATION_DATE);

        updatedPersistentIdentifier.autoId();

        PersistentIdentifierDTO updatedPersistentIdentifierDTO = persistentIdentifierMapper.toDto(updatedPersistentIdentifier);

        restPersistentIdentifierMockMvc.perform(put("/api/persistent-identifiers/{id}", persistentIdentifierDTO.getId())
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPersistentIdentifierDTO)))
            .andExpect(status().isOk());

        // Validate the PersistentIdentifier in the database
        PersistentIdentifier testPersistentIdentifier =persistentIdentifierMapper.toEntity(
            persistentIdentifierService.findOne(persistentIdentifierDTO.getId()));
        assertThat(testPersistentIdentifier.isResolverProxyMode()).isEqualTo(UPDATED_RESOLVER_PROXY_MODE);
        assertThat(testPersistentIdentifier.getIdentifier().getBeginLifespanVersion()).isEqualTo(UPDATED_BEGIN_LIFESPAN_VERSION);
        assertThat(testPersistentIdentifier.getIdentifier().getEndLifespanVersion()).isEqualTo(UPDATED_END_LIFESPAN_VERSION);
        assertThat(testPersistentIdentifier.getIdentifier().getAlternateId()).isEqualTo(UPDATED_ALTERNATE_ID);
        assertThat(testPersistentIdentifier.getResource().getResourceType()).isEqualTo(UPDATED_RESOURCE_TYPE);
        assertThat(testPersistentIdentifier.getResource().getLocator()).isEqualTo(UPDATED_LOCATOR);
        assertThat(testPersistentIdentifier.getRegistration().getProcessStatus()).isEqualTo(UPDATED_PROCESS_STATUS);
        assertThat(testPersistentIdentifier.getRegistration().getItemStatus()).isEqualTo(UPDATED_ITEM_STATUS);
        assertThat(testPersistentIdentifier.getRegistration().getLastChangeDate()).isEqualTo(UPDATED_LAST_CHANGE_DATE);
        assertThat(testPersistentIdentifier.getRegistration().getRegistrationDate()).isEqualTo(UPDATED_REGISTRATION_DATE);
        assertThat(testPersistentIdentifier.getRegistration().getLastRevisionDate()).isEqualTo(UPDATED_LAST_REVISION_DATE);
        assertThat(testPersistentIdentifier.getRegistration().getNextRenewalDate()).isEqualTo(UPDATED_NEXT_RENEWAL_DATE);
        assertThat(testPersistentIdentifier.getRegistration().getAnnullationDate()).isEqualTo(UPDATED_ANNULLATION_DATE);

        // Must be invariant
        assertThat(testPersistentIdentifier.getExternalUrn()).isEqualTo(DEFAULT_EXTERNAL_URN);
        assertThat(testPersistentIdentifier.getIdentifier().getNamespace()).isEqualTo(DEFAULT_NAMESPACE);
        assertThat(testPersistentIdentifier.getIdentifier().getLocalId()).isEqualTo(DEFAULT_LOCAL_ID);
        assertThat(testPersistentIdentifier.getIdentifier().getVersionId()).isEqualTo(DEFAULT_VERSION_ID);
    }

    @Test
    public void updateNonExistingPersistentIdentifierFails() throws Exception {
        // Create the PersistentIdentifier
        persistentIdentifier.autoId();
        PersistentIdentifierDTO persistentIdentifierDTO = persistentIdentifierMapper.toDto(persistentIdentifier);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPersistentIdentifierMockMvc.perform(put("/api/persistent-identifiers/{id}", persistentIdentifier.getId())
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(persistentIdentifierDTO)))
            .andExpect(status().isNotFound());
    }

    @Test
    public void deletePersistentIdentifier() throws Exception {
        // Initialize the database
        PersistentIdentifierDTO persistentIdentifierDTO = persistentIdentifierMapper.toDto(persistentIdentifier);
        persistentIdentifierDTO = persistentIdentifierService.save(persistentIdentifierDTO);

        // Get the persistentIdentifier
        restPersistentIdentifierMockMvc.perform(get("/api/persistent-identifiers/{id}", persistentIdentifierDTO.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        restPersistentIdentifierMockMvc.perform(delete("/api/persistent-identifiers/{id}", persistentIdentifierDTO.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        restPersistentIdentifierMockMvc.perform(get("/api/persistent-identifiers/{id}", persistentIdentifierDTO.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNotFound());
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PersistentIdentifier.class);
        PersistentIdentifier persistentIdentifier1 = new PersistentIdentifier();
        persistentIdentifier1.setId(UUID_1);
        PersistentIdentifier persistentIdentifier2 = new PersistentIdentifier();
        persistentIdentifier2.setId(persistentIdentifier1.getId());
        assertThat(persistentIdentifier1).isEqualTo(persistentIdentifier2);
        persistentIdentifier2.setId(UUID_2);
        assertThat(persistentIdentifier1).isNotEqualTo(persistentIdentifier2);
        persistentIdentifier1.setId(null);
        assertThat(persistentIdentifier1).isNotEqualTo(persistentIdentifier2);
    }

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PersistentIdentifierDTO.class);
        PersistentIdentifierDTO persistentIdentifierDTO1 = new PersistentIdentifierDTO();
        persistentIdentifierDTO1.setId(UUID_1);
        PersistentIdentifierDTO persistentIdentifierDTO2 = new PersistentIdentifierDTO();
        assertThat(persistentIdentifierDTO1).isNotEqualTo(persistentIdentifierDTO2);
        persistentIdentifierDTO2.setId(persistentIdentifierDTO1.getId());
        assertThat(persistentIdentifierDTO1).isEqualTo(persistentIdentifierDTO2);
        persistentIdentifierDTO2.setId(UUID_2);
        assertThat(persistentIdentifierDTO1).isNotEqualTo(persistentIdentifierDTO2);
        persistentIdentifierDTO1.setId(null);
        assertThat(persistentIdentifierDTO1).isNotEqualTo(persistentIdentifierDTO2);
    }

    @Test
    public void testEntityFromId() {
        assertThat(persistentIdentifierMapper.fromId(UUID_42).getId()).isEqualTo(UUID_42);
        assertThat(persistentIdentifierMapper.fromId(null)).isNull();
    }
}
