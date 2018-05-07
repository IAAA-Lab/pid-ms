package es.unizar.iaaa.pid.web.rest;

import es.unizar.iaaa.pid.PidmsApp;
import es.unizar.iaaa.pid.domain.Change;
import es.unizar.iaaa.pid.domain.Feature;
import es.unizar.iaaa.pid.domain.Namespace;
import es.unizar.iaaa.pid.domain.Task;
import es.unizar.iaaa.pid.service.*;
import es.unizar.iaaa.pid.service.dto.ChangeDTO;
import es.unizar.iaaa.pid.service.mapper.*;
import es.unizar.iaaa.pid.web.rest.errors.ExceptionTranslator;
import es.unizar.iaaa.pid.web.rest.util.MvcResultUtils;
import es.unizar.iaaa.pid.web.rest.util.TestUtil;
import org.assertj.core.api.SoftAssertions;
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

import static es.unizar.iaaa.pid.web.rest.util.HeaderUtil.ERROR_ID_ALREADY_EXIST;
import static es.unizar.iaaa.pid.web.rest.util.ResourceFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ChangeResource REST controller.
 *
 * @see ChangeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PidmsApp.class)
@Transactional
@WithMockUser(username=UserResourceIntTest.DEFAULT_LOGIN,password=UserResourceIntTest.DEFAULT_PASSWORD)
public class ChangeResourceIntTest extends LoggedUser {

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private TaskDTOService taskDTOService;

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
    private NamespaceDTOService namespaceDTOService;

    @Autowired
    private NamespaceMapper namespaceMapper;

    @Autowired
    private ChangeMapper changeMapper;

    @Autowired
    private ChangeDTOService changeService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restChangeMockMvc;

    private Change change;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ChangeResource changeResource = new ChangeResource(changeService, featureDTOService,
        		namespaceDTOService, organizationMemberDTOService);
        this.restChangeMockMvc = MockMvcBuilders.standaloneSetup(changeResource)
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

        Task task = task(namespace);
        Long taskId = taskDTOService.save(taskMapper.toDto(task)).getId();
        task.setId(taskId);

        change = change(task, feature);
    }

    @Test
    public void createChange() throws Exception {
        // Create the Change
        ChangeDTO changeDTO = changeMapper.toDto(change);

        // Create the Change
        MvcResult result = restChangeMockMvc.perform(post("/api/changes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(changeDTO)))
            .andExpect(status().isCreated()).andReturn();

        // Extract current id from Location
        Long id = MvcResultUtils.extractIdFromLocation(result);

        // Validate the Task in the database
        Change testChange = changeMapper.toEntity(changeService.findOne(id));
        assertThat(testChange.getChangeTimestamp()).isEqualTo(DEFAULT_CHANGE_TIMESTAMP);
        assertThat(testChange.getAction()).isEqualTo(DEFAULT_ACTION);
        assertThat(testChange.getFeature().getId()).isEqualTo(changeDTO.getFeatureId());
        assertThat(testChange.getIdentifier().getNamespace()).isEqualTo(DEFAULT_NAMESPACE);
        assertThat(testChange.getIdentifier().getLocalId()).isEqualTo(DEFAULT_LOCAL_ID);
        assertThat(testChange.getIdentifier().getVersionId()).isEqualTo(DEFAULT_VERSION_ID);
        assertThat(testChange.getIdentifier().getBeginLifespanVersion()).isEqualTo(DEFAULT_BEGIN_LIFESPAN_VERSION);
        assertThat(testChange.getIdentifier().getEndLifespanVersion()).isEqualTo(DEFAULT_END_LIFESPAN_VERSION);
        assertThat(testChange.getIdentifier().getAlternateId()).isEqualTo(DEFAULT_ALTERNATE_ID);
        assertThat(testChange.getResource().getResourceType()).isEqualTo(DEFAULT_RESOURCE_TYPE);
        assertThat(testChange.getResource().getLocator()).isEqualTo(DEFAULT_LOCATOR);
    }

    @Test
    public void createChangeWithIdMustFail() throws Exception {
        // Create the Change
        ChangeDTO changeDTO = changeMapper.toDto(change);
        changeDTO.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restChangeMockMvc.perform(post("/api/changes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(changeDTO)))
            .andExpect(status().isBadRequest())
            .andExpect(header().string(ERROR_HEADER,ERROR_ID_ALREADY_EXIST));
    }

    @Test
    public void checkNamespaceIsRequired() throws Exception {
        // Create the Change
        ChangeDTO changeDTO = changeMapper.toDto(change);
        changeDTO.setNamespace(null);

        restChangeMockMvc.perform(post("/api/changes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(changeDTO)))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void checkLocalIdIsRequired() throws Exception {
        // Create the Change
        change.getIdentifier().setLocalId(null);
        ChangeDTO changeDTO = changeMapper.toDto(change);

        restChangeMockMvc.perform(post("/api/changes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(changeDTO)))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void getAllChanges() throws Exception {
        // Initialize the database
        ChangeDTO changeDTO = changeService.save(changeMapper.toDto(change));

        // Get all the changeList
        restChangeMockMvc.perform(get("/api/changes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(changeDTO.getId().intValue())))
            .andExpect(jsonPath("$.[*].changeTimestamp").value(hasItem(DEFAULT_CHANGE_TIMESTAMP.toString())))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION.toString())))
            .andExpect(jsonPath("$.[*].featureId").value(hasItem(changeDTO.getFeatureId().intValue())))
            .andExpect(jsonPath("$.[*].namespace").value(hasItem(DEFAULT_NAMESPACE)))
            .andExpect(jsonPath("$.[*].localId").value(hasItem(DEFAULT_LOCAL_ID)))
            .andExpect(jsonPath("$.[*].versionId").value(hasItem(DEFAULT_VERSION_ID)))
            .andExpect(jsonPath("$.[*].beginLifespanVersion").value(hasItem(DEFAULT_BEGIN_LIFESPAN_VERSION.toString())))
            .andExpect(jsonPath("$.[*].endLifespanVersion").value(hasItem(DEFAULT_END_LIFESPAN_VERSION.toString())))
            .andExpect(jsonPath("$.[*].alternateId").value(hasItem(DEFAULT_ALTERNATE_ID)))
            .andExpect(jsonPath("$.[*].resourceType").value(hasItem(DEFAULT_RESOURCE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].locator").value(hasItem(DEFAULT_LOCATOR)));
    }

    @Test
    public void getChange() throws Exception {
        // Initialize the database
        ChangeDTO changeDTO = changeService.save(changeMapper.toDto(change));

        // Get the change
        restChangeMockMvc.perform(get("/api/changes/{id}", changeDTO.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(changeDTO.getId().intValue()))
            .andExpect(jsonPath("$.changeTimestamp").value(DEFAULT_CHANGE_TIMESTAMP.toString()))
            .andExpect(jsonPath("$.action").value(DEFAULT_ACTION.toString()))
            .andExpect(jsonPath("$.featureId").value(changeDTO.getFeatureId()))
            .andExpect(jsonPath("$.namespace").value(DEFAULT_NAMESPACE))
            .andExpect(jsonPath("$.localId").value(DEFAULT_LOCAL_ID))
            .andExpect(jsonPath("$.versionId").value(DEFAULT_VERSION_ID))
            .andExpect(jsonPath("$.beginLifespanVersion").value(DEFAULT_BEGIN_LIFESPAN_VERSION.toString()))
            .andExpect(jsonPath("$.endLifespanVersion").value(DEFAULT_END_LIFESPAN_VERSION.toString()))
            .andExpect(jsonPath("$.alternateId").value(DEFAULT_ALTERNATE_ID))
            .andExpect(jsonPath("$.resourceType").value(DEFAULT_RESOURCE_TYPE.toString()))
            .andExpect(jsonPath("$.locator").value(DEFAULT_LOCATOR));
    }

    @Test
    public void getNonExistingChange() throws Exception {
        // Get the change
        restChangeMockMvc.perform(get("/api/changes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateChange() throws Exception {
        // Initialize the database
        ChangeDTO changeDTO = changeService.save(changeMapper.toDto(change));

        // Update the change
        Change updatedChange = changeMapper.toEntity(changeService.findOne(changeDTO.getId()));
        updatedChange
            .changeTimestamp(UPDATED_CHANGE_TIMESTAMP)
            .action(UPDATED_ACTION)
            .getResource()
            .resourceType(UPDATED_RESOURCE_TYPE)
            .locator(UPDATED_LOCATOR);

        updatedChange
            .getIdentifier()
            .namespace(UPDATED_NAMESPACE)
            .localId(UPDATED_LOCAL_ID)
            .versionId(UPDATED_VERSION_ID)
            .beginLifespanVersion(UPDATED_BEGIN_LIFESPAN_VERSION)
            .endLifespanVersion(UPDATED_END_LIFESPAN_VERSION)
            .alternateId(UPDATED_ALTERNATE_ID);

        ChangeDTO updatedChangeDTO = changeMapper.toDto(updatedChange);

        restChangeMockMvc.perform(put("/api/changes/{id}", changeDTO.getId())
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedChangeDTO)))
            .andExpect(status().isOk());

        // Collect all failed assertions of the change
        Change testChange = changeMapper.toEntity(changeService.findOne(changeDTO.getId()));
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(testChange.getChangeTimestamp()).isEqualTo(UPDATED_CHANGE_TIMESTAMP);
        softly.assertThat(testChange.getAction()).isEqualTo(UPDATED_ACTION);
        softly.assertThat(testChange.getIdentifier().getNamespace()).isEqualTo(UPDATED_NAMESPACE);
        softly.assertThat(testChange.getIdentifier().getLocalId()).isEqualTo(UPDATED_LOCAL_ID);
        softly.assertThat(testChange.getIdentifier().getVersionId()).isEqualTo(UPDATED_VERSION_ID);
        softly.assertThat(testChange.getIdentifier().getBeginLifespanVersion()).isEqualTo(UPDATED_BEGIN_LIFESPAN_VERSION);
        softly.assertThat(testChange.getIdentifier().getEndLifespanVersion()).isEqualTo(UPDATED_END_LIFESPAN_VERSION);
        softly.assertThat(testChange.getIdentifier().getAlternateId()).isEqualTo(UPDATED_ALTERNATE_ID);
        softly.assertThat(testChange.getResource().getResourceType()).isEqualTo(UPDATED_RESOURCE_TYPE);
        softly.assertThat(testChange.getResource().getLocator()).isEqualTo(UPDATED_LOCATOR);
        softly.assertAll();
    }

    @Test
    public void updateNonExistingChangeFails() throws Exception {
        // Create the Change
        ChangeDTO changeDTO = changeMapper.toDto(change);
        Long id = Long.MAX_VALUE;

        // Update it
        restChangeMockMvc.perform(put("/api/changes/{id}", id)
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(changeDTO)))
            .andExpect(status().isNotFound());
    }

    @Test
    public void deleteChange() throws Exception {
        // Initialize the database
        ChangeDTO changeDTO = changeService.save(changeMapper.toDto(change));

        restChangeMockMvc.perform(get("/api/changes/{id}", changeDTO.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        restChangeMockMvc.perform(delete("/api/changes/{id}", changeDTO.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        restChangeMockMvc.perform(get("/api/changes/{id}", changeDTO.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNotFound());

    }

    @Test
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
    public void testEntityFromId() {
        assertThat(changeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(changeMapper.fromId(null)).isNull();
    }
}
