package es.unizar.iaaa.pid.web.rest;

import es.unizar.iaaa.pid.PidmsApp;
import es.unizar.iaaa.pid.domain.Namespace;
import es.unizar.iaaa.pid.service.NamespaceDTOService;
import es.unizar.iaaa.pid.service.OrganizationDTOService;
import es.unizar.iaaa.pid.service.OrganizationMemberDTOService;
import es.unizar.iaaa.pid.service.dto.NamespaceDTO;
import es.unizar.iaaa.pid.service.mapper.NamespaceMapper;
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

import static es.unizar.iaaa.pid.web.rest.util.ResourceFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
/**
 * Test class for the NamespaceResource REST controller.
 *
 * @see NamespaceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PidmsApp.class)
@Transactional
@WithMockUser(username=UserResourceIntTest.DEFAULT_LOGIN,password=UserResourceIntTest.DEFAULT_PASSWORD)
public class NamespaceResourceIntTest extends LoggedUser {


    @Autowired
    private NamespaceMapper namespaceMapper;

    @Autowired
    private NamespaceDTOService namespaceService;

    @Autowired
    private OrganizationMemberDTOService organizationMemberDTOService;

    @Autowired
    private OrganizationMapper organizationMapper;

    @Autowired
    private OrganizationDTOService organizationDTOService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private NamespaceDTOService namespaceDTOService;

    private MockMvc restNamespaceMockMvc;

    private Namespace namespace;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        NamespaceResource namespaceResource = new NamespaceResource(namespaceService,
        		         		organizationMemberDTOService);
        this.restNamespaceMockMvc = MockMvcBuilders.standaloneSetup(namespaceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }


    @Before
    public void initTest() {
    	//create namespace
        namespace = namespace();

        // Save organization
        Long organizationId = organizationDTOService.save(organizationMapper.toDto(namespace.getOwner())).getId();
        namespace.getOwner().setId(organizationId);
    }

    @Test
    public void createNamespace() throws Exception {

        // Create the Namespace
        NamespaceDTO namespaceDTO = namespaceMapper.toDto(namespace);

        MvcResult result = restNamespaceMockMvc.perform(post("/api/namespaces")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(namespaceDTO)))
            .andExpect(status().isCreated()).andReturn();

        // Extract current id from Location
        Long id = MvcResultUtils.extractIdFromLocation(result);

        // Validate the Namespace in the database
        Namespace testNamespace = namespaceMapper.toEntity(namespaceDTOService.findOne(id));
        assertThat(testNamespace.getNamespace()).isEqualTo(DEFAULT_NAMESPACE);
        assertThat(testNamespace.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testNamespace.isPublicNamespace()).isEqualTo(DEFAULT_PUBLIC_NAMESPACE);
        assertThat(testNamespace.getRenewalPolicy()).isEqualTo(DEFAULT_RENEWAL_POLICY);
        assertThat(testNamespace.getNamespaceStatus()).isEqualTo(DEFAULT_NAMESPACE_STATUS);
        assertThat(testNamespace.getRegistration().getProcessStatus()).isEqualTo(DEFAULT_PROCESS_STATUS);
        assertThat(testNamespace.getRegistration().getItemStatus()).isEqualTo(DEFAULT_ITEM_STATUS);
//      assertThat(testNamespace.getRegistration().getLastChangeDate()).isEqualTo(DEFAULT_LAST_CHANGE_DATE);
//      assertThat(testNamespace.getRegistration().getRegistrationDate()).isEqualTo(DEFAULT_REGISTRATION_DATE);
        assertThat(testNamespace.getRegistration().getLastRevisionDate()).isEqualTo(DEFAULT_LAST_REVISION_DATE);
        assertThat(testNamespace.getRegistration().getNextRenewalDate()).isEqualTo(DEFAULT_NEXT_RENEWAL_DATE);
        assertThat(testNamespace.getRegistration().getAnnullationDate()).isEqualTo(DEFAULT_ANNULLATION_DATE);
        assertThat(testNamespace.getSource().getMethodType()).isEqualTo(DEFAULT_METHOD_TYPE);
        assertThat(testNamespace.getSource().getSourceType()).isEqualTo(DEFAULT_SOURCE_TYPE);
        assertThat(testNamespace.getSource().getEndpointLocation()).isEqualTo(DEFAULT_ENDPOINT_LOCATION);
        assertThat(testNamespace.getSource().isResolverProxyMode()).isEqualTo(DEFAULT_RESOLVER_PROXY_MODE);
        assertThat(testNamespace.getSource().getMaxNumRequest()).isEqualTo(DEFAULT_MAX_NUM_REQUEST);
        assertThat(testNamespace.getVersion()).isEqualTo(FIRST_VERSION);
    }

    @Test
    public void createNamespaceWithIdMustFail() throws Exception {
        // Create the Namespace with an existing ID
        Namespace namespace = namespace();
        namespace.setId(1L);
        NamespaceDTO namespaceDTO = namespaceMapper.toDto(namespace);

        // An entity with an existing ID cannot be created, so this API call must fail
        restNamespaceMockMvc.perform(post("/api/namespaces")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(namespaceDTO)))
            .andExpect(status().isBadRequest())
            .andExpect(header().string(ERROR_HEADER,ERROR_VALUE_NAMESPACE_ALREADY_EXIST));
    }

    @Test
    public void checkNamespaceIsRequired() throws Exception {
        // Create the Namespace, which fails.
        Namespace namespace = namespace();
        NamespaceDTO namespaceDTO = namespaceMapper.toDto(namespace);
        namespaceDTO.setNamespace(null);

        restNamespaceMockMvc.perform(post("/api/namespaces")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(namespaceDTO)))
            .andExpect(status().isBadRequest())
            .andExpect(TestUtil.checkError(FIELD_NAMESPACE, ERROR_NULL_FIELD));
    }


    @Test
    public void checkPublicNamespaceIsRequired() throws Exception {
        // Create the Namespace, which fails.
        NamespaceDTO namespaceDTO = namespaceMapper.toDto(namespace);
        namespaceDTO.setPublicNamespace(null);

        restNamespaceMockMvc.perform(post("/api/namespaces")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(namespaceDTO)))
            .andExpect(status().isBadRequest())
            .andExpect(TestUtil.checkError(FIELD_PUBLIC_NAMESPACE, ERROR_NULL_FIELD));
    }

    @Test
    public void checkRenewalPolicyIsRequired() throws Exception {
        // Create the Namespace, which fails.
        NamespaceDTO namespaceDTO = namespaceMapper.toDto(namespace());
        namespaceDTO.setRenewalPolicy(null);

        restNamespaceMockMvc.perform(post("/api/namespaces")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(namespaceDTO)))
            .andExpect(status().isBadRequest())
            .andExpect(TestUtil.checkError(FIELD_RENEWALPOLICY, ERROR_NULL_FIELD));
    }

    @Test
    public void getAllNamespaces() throws Exception {
        NamespaceDTO namespaceDTO = namespaceMapper.toDto(namespace);
        namespaceDTO = namespaceDTOService.save(namespaceDTO);

        // Get all the namespaceList
        restNamespaceMockMvc.perform(get("/api/namespaces?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(namespaceDTO.getId().intValue())))
            .andExpect(jsonPath("$.[*].namespace").value(hasItem(DEFAULT_NAMESPACE)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].publicNamespace").value(hasItem(DEFAULT_PUBLIC_NAMESPACE)))
            .andExpect(jsonPath("$.[*].renewalPolicy").value(hasItem(DEFAULT_RENEWAL_POLICY.toString())))
            .andExpect(jsonPath("$.[*].namespaceStatus").value(hasItem(DEFAULT_NAMESPACE_STATUS.toString())))
            .andExpect(jsonPath("$.[*].processStatus").value(hasItem(DEFAULT_PROCESS_STATUS.toString())))
            .andExpect(jsonPath("$.[*].itemStatus").value(hasItem(DEFAULT_ITEM_STATUS.toString())))
            .andExpect(jsonPath("$.[*].lastChangeDate").value(hasItem(DEFAULT_LAST_CHANGE_DATE.toString())))
            .andExpect(jsonPath("$.[*].registrationDate").value(hasItem(DEFAULT_REGISTRATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastRevisionDate").value(hasItem(DEFAULT_LAST_REVISION_DATE.toString())))
            .andExpect(jsonPath("$.[*].nextRenewalDate").value(hasItem(DEFAULT_NEXT_RENEWAL_DATE.toString())))
            .andExpect(jsonPath("$.[*].annullationDate").value(hasItem(DEFAULT_ANNULLATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].methodType").value(hasItem(DEFAULT_METHOD_TYPE.toString())))
            .andExpect(jsonPath("$.[*].sourceType").value(hasItem(DEFAULT_SOURCE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].endpointLocation").value(hasItem(DEFAULT_ENDPOINT_LOCATION)))
            .andExpect(jsonPath("$.[*].resolverProxyMode").value(hasItem(DEFAULT_RESOLVER_PROXY_MODE)))
            .andExpect(jsonPath("$.[*].maxNumRequest").value(hasItem(DEFAULT_MAX_NUM_REQUEST)))
            .andExpect(jsonPath("$.[*].version").value(hasItem(FIRST_VERSION)));
    }

    @Test
    public void getNamespace() throws Exception {
        // Initialize the database
        NamespaceDTO namespaceDTO = namespaceMapper.toDto(namespace);
        namespaceDTO = namespaceDTOService.save(namespaceDTO);

        // Get the namespace
        restNamespaceMockMvc.perform(get("/api/namespaces/{id}", namespaceDTO.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(namespaceDTO.getId().intValue()))
            .andExpect(jsonPath("$.namespace").value(DEFAULT_NAMESPACE))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.publicNamespace").value(DEFAULT_PUBLIC_NAMESPACE))
            .andExpect(jsonPath("$.renewalPolicy").value(DEFAULT_RENEWAL_POLICY.toString()))
            .andExpect(jsonPath("$.namespaceStatus").value(DEFAULT_NAMESPACE_STATUS.toString()))
            .andExpect(jsonPath("$.processStatus").value(DEFAULT_PROCESS_STATUS.toString()))
            .andExpect(jsonPath("$.itemStatus").value(DEFAULT_ITEM_STATUS.toString()))
            .andExpect(jsonPath("$.lastChangeDate").value(DEFAULT_LAST_CHANGE_DATE.toString()))
            .andExpect(jsonPath("$.registrationDate").value(DEFAULT_REGISTRATION_DATE.toString()))
            .andExpect(jsonPath("$.lastRevisionDate").value(DEFAULT_LAST_REVISION_DATE.toString()))
            .andExpect(jsonPath("$.nextRenewalDate").value(DEFAULT_NEXT_RENEWAL_DATE.toString()))
            .andExpect(jsonPath("$.annullationDate").value(DEFAULT_ANNULLATION_DATE.toString()))
            .andExpect(jsonPath("$.methodType").value(DEFAULT_METHOD_TYPE.toString()))
            .andExpect(jsonPath("$.sourceType").value(DEFAULT_SOURCE_TYPE.toString()))
            .andExpect(jsonPath("$.endpointLocation").value(DEFAULT_ENDPOINT_LOCATION))
            .andExpect(jsonPath("$.resolverProxyMode").value(DEFAULT_RESOLVER_PROXY_MODE))
            .andExpect(jsonPath("$.maxNumRequest").value(DEFAULT_MAX_NUM_REQUEST))
            .andExpect(jsonPath("$.version").value(FIRST_VERSION));
    }

    @Test
    public void getNonExistingNamespace() throws Exception {
        // Get the namespace
        restNamespaceMockMvc.perform(get("/api/namespaces/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateNamespace() throws Exception {
        // Initialize the database
        NamespaceDTO namespaceDTO = namespaceMapper.toDto(namespace);
        namespaceDTO = namespaceDTOService.save(namespaceDTO);

        // Update the database
        Namespace updatedNamespace = namespaceMapper.toEntity(namespaceDTOService.findOne(namespaceDTO.getId()));

        updatedNamespace
            .namespace(UPDATED_NAMESPACE)
            .title(UPDATED_TITLE)
//          .publicNamespace(UPDATED_PUBLIC_NAMESPACE)
            .renewalPolicy(UPDATED_RENEWAL_POLICY)
            .namespaceStatus(UPDATED_NAMESPACE_STATUS)
            .getSource()
            .methodType(UPDATED_METHOD_TYPE)
            .sourceType(UPDATED_SOURCE_TYPE)
            .endpointLocation(UPDATED_ENDPOINT_LOCATION)
            .resolverProxyMode(UPDATED_RESOLVER_PROXY_MODE)
            .maxNumRequest(UPDATED_MAX_NUM_REQUEST);

        updatedNamespace.getRegistration()
            .processStatus(UPDATED_PROCESS_STATUS)
            .itemStatus(UPDATED_ITEM_STATUS)
            .lastChangeDate(UPDATED_LAST_CHANGE_DATE)
            .registrationDate(UPDATED_REGISTRATION_DATE)
            .lastRevisionDate(UPDATED_LAST_REVISION_DATE)
            .nextRenewalDate(UPDATED_NEXT_RENEWAL_DATE)
            .annullationDate(UPDATED_ANNULLATION_DATE);

        restNamespaceMockMvc.perform(put("/api/namespaces/{id}", namespaceDTO.getId())
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(namespaceMapper.toDto(updatedNamespace))))
            .andExpect(status().isOk());

        // Validate the Namespace in the database
        Namespace testNamespace = namespaceMapper.toEntity(namespaceDTOService.findOne(namespaceDTO.getId()));
        assertThat(testNamespace.getNamespace()).isEqualTo(UPDATED_NAMESPACE);
        assertThat(testNamespace.getTitle()).isEqualTo(UPDATED_TITLE);
//      assertThat(testNamespace.isPublicNamespace()).isEqualTo(UPDATED_PUBLIC_NAMESPACE);
        assertThat(testNamespace.getRenewalPolicy()).isEqualTo(UPDATED_RENEWAL_POLICY);
        assertThat(testNamespace.getNamespaceStatus()).isEqualTo(UPDATED_NAMESPACE_STATUS);
        assertThat(testNamespace.getRegistration().getProcessStatus()).isEqualTo(UPDATED_PROCESS_STATUS);
        assertThat(testNamespace.getRegistration().getItemStatus()).isEqualTo(UPDATED_ITEM_STATUS);
        assertThat(testNamespace.getRegistration().getLastChangeDate()).isEqualTo(UPDATED_LAST_CHANGE_DATE);
        assertThat(testNamespace.getRegistration().getRegistrationDate()).isEqualTo(UPDATED_REGISTRATION_DATE);
        assertThat(testNamespace.getRegistration().getLastRevisionDate()).isEqualTo(UPDATED_LAST_REVISION_DATE);
        assertThat(testNamespace.getRegistration().getNextRenewalDate()).isEqualTo(UPDATED_NEXT_RENEWAL_DATE);
        assertThat(testNamespace.getRegistration().getAnnullationDate()).isEqualTo(UPDATED_ANNULLATION_DATE);
        assertThat(testNamespace.getSource().getMethodType()).isEqualTo(UPDATED_METHOD_TYPE);
        assertThat(testNamespace.getSource().getSourceType()).isEqualTo(UPDATED_SOURCE_TYPE);
        assertThat(testNamespace.getSource().getEndpointLocation()).isEqualTo(UPDATED_ENDPOINT_LOCATION);
        assertThat(testNamespace.getSource().isResolverProxyMode()).isEqualTo(UPDATED_RESOLVER_PROXY_MODE);
        assertThat(testNamespace.getSource().getMaxNumRequest()).isEqualTo(UPDATED_MAX_NUM_REQUEST);
//      assertThat(testNamespace.getVersion()).isEqualTo(NEXT_VERSION);
    }

    @Test
    public void updateNonExistingNamespaceFails() throws Exception {
        // Initialize the database
        NamespaceDTO namespaceDTO = namespaceMapper.toDto(namespace());
        Long id = Long.MAX_VALUE;

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restNamespaceMockMvc.perform(put("/api/namespaces/{id}", id)
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(namespaceDTO)))
            .andExpect(status().isNotFound());
    }

    @Test
    public void deleteNamespace() throws Exception {
        // Initialize the database
        NamespaceDTO namespaceDTO = namespaceMapper.toDto(namespace);
        namespaceDTO = namespaceDTOService.save(namespaceDTO);

        restNamespaceMockMvc.perform(get("/api/namespaces/{id}", namespaceDTO.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        restNamespaceMockMvc.perform(delete("/api/namespaces/{id}", namespaceDTO.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        restNamespaceMockMvc.perform(get("/api/namespaces/{id}", namespaceDTO.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNotFound());
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Namespace.class);
        Namespace namespace1 = new Namespace();
        namespace1.setId(1L);
        Namespace namespace2 = new Namespace();
        namespace2.setId(namespace1.getId());
        assertThat(namespace1).isEqualTo(namespace2);
        namespace2.setId(2L);
        assertThat(namespace1).isNotEqualTo(namespace2);
        namespace1.setId(null);
        assertThat(namespace1).isNotEqualTo(namespace2);
    }

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NamespaceDTO.class);
        NamespaceDTO namespaceDTO1 = new NamespaceDTO();
        namespaceDTO1.setId(1L);
        NamespaceDTO namespaceDTO2 = new NamespaceDTO();
        assertThat(namespaceDTO1).isNotEqualTo(namespaceDTO2);
        namespaceDTO2.setId(namespaceDTO1.getId());
        assertThat(namespaceDTO1).isEqualTo(namespaceDTO2);
        namespaceDTO2.setId(2L);
        assertThat(namespaceDTO1).isNotEqualTo(namespaceDTO2);
        namespaceDTO1.setId(null);
        assertThat(namespaceDTO1).isNotEqualTo(namespaceDTO2);
    }

    @Test
    public void testEntityFromId() {
        assertThat(namespaceMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(namespaceMapper.fromId(null)).isNull();
    }

}
