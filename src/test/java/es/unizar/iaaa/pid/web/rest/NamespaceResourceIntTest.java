package es.unizar.iaaa.pid.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.persistence.EntityManager;

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

import es.unizar.iaaa.pid.PidmsApp;
import es.unizar.iaaa.pid.domain.BoundingBox;
import es.unizar.iaaa.pid.domain.Feature;
import es.unizar.iaaa.pid.domain.Namespace;
import es.unizar.iaaa.pid.domain.Organization;
import es.unizar.iaaa.pid.domain.OrganizationMember;
import es.unizar.iaaa.pid.domain.Registration;
import es.unizar.iaaa.pid.domain.Source;
import es.unizar.iaaa.pid.domain.User;
import es.unizar.iaaa.pid.domain.enumeration.Capacity;
import es.unizar.iaaa.pid.domain.enumeration.ItemStatus;
import es.unizar.iaaa.pid.domain.enumeration.MethodType;
import es.unizar.iaaa.pid.domain.enumeration.NamespaceStatus;
import es.unizar.iaaa.pid.domain.enumeration.ProcessStatus;
import es.unizar.iaaa.pid.domain.enumeration.RenewalPolicy;
import es.unizar.iaaa.pid.domain.enumeration.SourceType;
import es.unizar.iaaa.pid.repository.NamespaceRepository;
import es.unizar.iaaa.pid.service.NamespaceDTOService;
import es.unizar.iaaa.pid.service.OrganizationDTOService;
import es.unizar.iaaa.pid.service.OrganizationMemberDTOService;
import es.unizar.iaaa.pid.service.UserService;
import es.unizar.iaaa.pid.service.dto.NamespaceDTO;
import es.unizar.iaaa.pid.service.mapper.NamespaceMapper;
import es.unizar.iaaa.pid.service.mapper.OrganizationMapper;
import es.unizar.iaaa.pid.service.mapper.OrganizationMemberMapper;
import es.unizar.iaaa.pid.service.mapper.UserMapper;
import es.unizar.iaaa.pid.web.rest.errors.ExceptionTranslator;
/**
 * Test class for the NamespaceResource REST controller.
 *
 * @see NamespaceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PidmsApp.class)
public class NamespaceResourceIntTest {

//    private static final String DEFAULT_NAMESPACE = "AAAAAAAAAA";
//    private static final String UPDATED_NAMESPACE = "BBBBBBBBBB";
//
//    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
//    private static final String UPDATED_TITLE = "BBBBBBBBBB";
//
//    private static final Boolean DEFAULT_PUBLIC_NAMESPACE = true;
//    // private static final Boolean UPDATED_PUBLIC_NAMESPACE = true;
//
//    private static final RenewalPolicy DEFAULT_RENEWAL_POLICY = RenewalPolicy.NONE;
//    private static final RenewalPolicy UPDATED_RENEWAL_POLICY = RenewalPolicy.CONTINUOUS;
//
//    private static final NamespaceStatus DEFAULT_NAMESPACE_STATUS = NamespaceStatus.STOP;
//    private static final NamespaceStatus UPDATED_NAMESPACE_STATUS = NamespaceStatus.GO;
//
//    private static final ProcessStatus DEFAULT_PROCESS_STATUS = ProcessStatus.NONE;
//    private static final ProcessStatus UPDATED_PROCESS_STATUS = ProcessStatus.PREPARING_HARVEST;
//
//    private static final ItemStatus DEFAULT_ITEM_STATUS = ItemStatus.PENDING_VALIDATION;
//    private static final ItemStatus UPDATED_ITEM_STATUS = ItemStatus.LAPSED;
//
//    private static final Instant DEFAULT_LAST_CHANGE_DATE = Instant.ofEpochMilli(0L);
//    private static final Instant UPDATED_LAST_CHANGE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);
//
//    private static final Instant DEFAULT_REGISTRATION_DATE = Instant.ofEpochMilli(0L);
//    private static final Instant UPDATED_REGISTRATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);
//
//    private static final Instant DEFAULT_LAST_REVISION_DATE = Instant.ofEpochMilli(0L);
//    private static final Instant UPDATED_LAST_REVISION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);
//
//    private static final Instant DEFAULT_NEXT_RENEWAL_DATE = Instant.ofEpochMilli(0L);
//    private static final Instant UPDATED_NEXT_RENEWAL_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);
//
//    private static final Instant DEFAULT_ANNULLATION_DATE = Instant.ofEpochMilli(0L);
//    private static final Instant UPDATED_ANNULLATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);
//
//    private static final MethodType DEFAULT_METHOD_TYPE = MethodType.GET;
//    private static final MethodType UPDATED_METHOD_TYPE = MethodType.POST;
//
//    private static final SourceType DEFAULT_SOURCE_TYPE = SourceType.WFS;
//    private static final SourceType UPDATED_SOURCE_TYPE = SourceType.WFS;
//
//    private static final String DEFAULT_ENDPOINT_LOCATION = "AAAAAAAAAA";
//    private static final String UPDATED_ENDPOINT_LOCATION = "BBBBBBBBBB";
//
//    private static final String DEFAULT_SRS_NAME = "AAAAAAAAAA";
//    private static final String UPDATED_SRS_NAME = "BBBBBBBBBB";
//
//    private static final String DEFAULT_SCHEMA_URI = "AAAAAAAAAA";
//    private static final String UPDATED_SCHEMA_URI = "BBBBBBBBBB";
//
//    private static final String DEFAULT_SCHEMA_URI_GML = "AAAAAAAAAA";
//    private static final String UPDATED_SCHEMA_URI_GML = "BBBBBBBBBB";
//
//    private static final String DEFAULT_SCHEMA_URI_BASE = "AAAAAAAAAA";
//    private static final String UPDATED_SCHEMA_URI_BASE = "BBBBBBBBBB";
//
//    private static final String DEFAULT_SCHEMA_PREFIX = "AAAAAAAAAA";
//    private static final String UPDATED_SCHEMA_PREFIX = "BBBBBBBBBB";
//
//    private static final String DEFAULT_FEATURE_TYPE = "AAAAAAAAAA";
//    private static final String UPDATED_FEATURE_TYPE = "BBBBBBBBBB";
//
//    private static final String DEFAULT_GEOMETRY_PROPERTY = "AAAAAAAAAA";
//    private static final String UPDATED_GEOMETRY_PROPERTY = "BBBBBBBBBB";
//
//    private static final String DEFAULT_BEGIN_LIFESPAN_VERSION_PROPERTY = "AAAAAAAAAA";
//    private static final String UPDATED_BEGIN_LIFESPAN_VERSION_PROPERTY = "BBBBBBBBBB";
//
//    private static final Integer DEFAULT_FEATURES_THRESHOLD = 1;
//    private static final Integer UPDATED_FEATURES_THRESHOLD = 2;
//
//    private static final Boolean DEFAULT_RESOLVER_PROXY_MODE = false;
//    private static final Boolean UPDATED_RESOLVER_PROXY_MODE = true;
//
//    private static final Boolean DEFAULT_HITS_REQUEST = false;
//    private static final Boolean UPDATED_HITS_REQUEST = true;
//
//    private static final Integer DEFAULT_FACTOR_K = 1;
//    private static final Integer UPDATED_FACTOR_K = 2;
//
//    private static final String DEFAULT_XPATH = "AAAAAAAAAA";
//    private static final String UPDATED_XPATH = "BBBBBBBBBB";
//
//    private static final String DEFAULT_NAME_ITEM = "AAAAAAAAAA";
//    private static final String UPDATED_NAME_ITEM = "BBBBBBBBBB";
//
//    private static final Integer DEFAULT_MAX_NUM_REQUEST = 0;
//    private static final Integer UPDATED_MAX_NUM_REQUEST = 1;
//
//    private static final Double DEFAULT_MIN_X = 1D;
//    private static final Double UPDATED_MIN_X = 2D;
//
//    private static final Double DEFAULT_MIN_Y = 1D;
//    private static final Double UPDATED_MIN_Y = 2D;
//
//    private static final Double DEFAULT_MAX_X = 1D;
//    private static final Double UPDATED_MAX_X = 2D;
//
//    private static final Double DEFAULT_MAX_Y = 1D;
//    private static final Double UPDATED_MAX_Y = 2D;
//
//    private static final BoundingBox DEFAULT_BOUNDING_BOX = new BoundingBox().
//        maxX(DEFAULT_MAX_X).maxY(DEFAULT_MAX_Y).minY(DEFAULT_MIN_Y).minX(DEFAULT_MIN_X);
//
//    private static final BoundingBox UPDATED_BOUNDING_BOX = new BoundingBox().
//        maxX(UPDATED_MAX_X).maxY(UPDATED_MAX_Y).minY(UPDATED_MIN_Y).minX(UPDATED_MIN_X);
//
//    private static final Integer FIRST_VERSION = 0;
//    private static final Integer NEXT_VERSION = 1;
//    
//    private static final String ERROR_HEADER = "X-pidmsApp-error";
//    private static final String ERROR_VALUE_NAMESPACE_ALREADY_EXIST = "error.idexists";
//    
//    private static final String FIELD_NAMESPACE = "\"field\" : \"namespace\"";
//    private static final String FIELD_PUBLIC_NAMESPACE = "\"field\" : \"publicNamespace\"";
//    private static final String FIELD_RENEWALPOLICY = "\"field\" : \"renewalPolicy\"";
//    
//    private static final String ERROR_NULL_FIELD = "\"message\" : \"NotNull\"";
//
//    @Autowired
//    private NamespaceRepository namespaceRepository;
//
//    @Autowired
//    private NamespaceMapper namespaceMapper;
//
//    @Autowired
//    private NamespaceDTOService namespaceService;
//    
//    @Autowired
//    private OrganizationMemberDTOService organizationMemberDTOService;
//    
//    @Autowired
//    private OrganizationMemberMapper organizationMemberMapper;
//    
//    @Autowired
//    private OrganizationMapper organizationMapper;
//    
//    @Autowired
//    private OrganizationDTOService organizationDTOService;
//    
//    @Autowired
//    private UserService userService;
//    
//    @Autowired
//    private UserMapper userMapper;
//
//    @Autowired
//    private MappingJackson2HttpMessageConverter jacksonMessageConverter;
//
//    @Autowired
//    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;
//
//    @Autowired
//    private ExceptionTranslator exceptionTranslator;
//
//    @Autowired
//    private EntityManager em;
//
//    private MockMvc restNamespaceMockMvc;
//
//    private Namespace namespace;
//    
//    private User user;
//    
//    private Organization organization;
//    
//    private OrganizationMember organizationMemberAdmin;
//    
//    private OrganizationMember organizationMemberEditor;
//    
//    private OrganizationMember organizationMemberMember;
//
//    @Before
//    public void setup() {
//        MockitoAnnotations.initMocks(this);
//        NamespaceResource namespaceResource = new NamespaceResource(namespaceService, organizationMemberDTOService);
//        this.restNamespaceMockMvc = MockMvcBuilders.standaloneSetup(namespaceResource)
//            .setCustomArgumentResolvers(pageableArgumentResolver)
//            .setControllerAdvice(exceptionTranslator)
//            .setMessageConverters(jacksonMessageConverter).build();
//    }
//
//    /**
//     * Create an entity for this test.
//     *
//     * This is a static method, as tests for other entities might also need it,
//     * if they test an entity which requires the current entity.
//     */
//    public static Namespace createEntity(EntityManager em) {
//        Source source = new Source()
//        	.methodType(DEFAULT_METHOD_TYPE)
//            .sourceType(DEFAULT_SOURCE_TYPE)
//            .resolverProxyMode(DEFAULT_RESOLVER_PROXY_MODE)
//            .maxNumRequest(DEFAULT_MAX_NUM_REQUEST)
//            .endpointLocation(DEFAULT_ENDPOINT_LOCATION);
//            
//        
//        Feature feature = new Feature()
//        		.srsName(DEFAULT_SRS_NAME)
//                .schemaUri(DEFAULT_SCHEMA_URI)
//                .schemaUriGML(DEFAULT_SCHEMA_URI_GML)
//                .schemaUriBase(DEFAULT_SCHEMA_URI_BASE)
//                .schemaPrefix(DEFAULT_SCHEMA_PREFIX)
//                .featureType(DEFAULT_FEATURE_TYPE)
//                .geometryProperty(DEFAULT_GEOMETRY_PROPERTY)
//                .beginLifespanVersionProperty(DEFAULT_BEGIN_LIFESPAN_VERSION_PROPERTY)
//                .featuresThreshold(DEFAULT_FEATURES_THRESHOLD)
//                .hitsRequest(DEFAULT_HITS_REQUEST)
//                .factorK(DEFAULT_FACTOR_K)
//                .xpath(DEFAULT_XPATH)
//                .nameItem(DEFAULT_NAME_ITEM)
//                .boundingBox(DEFAULT_BOUNDING_BOX)
//                .source(source);
//        		
//
//        Registration registration = new Registration()
//            .processStatus(DEFAULT_PROCESS_STATUS)
//            .itemStatus(DEFAULT_ITEM_STATUS)
//            .lastChangeDate(DEFAULT_LAST_CHANGE_DATE)
//            .registrationDate(DEFAULT_REGISTRATION_DATE)
//            .lastRevisionDate(DEFAULT_LAST_REVISION_DATE)
//            .nextRenewalDate(DEFAULT_NEXT_RENEWAL_DATE)
//            .annullationDate(DEFAULT_ANNULLATION_DATE);
//
//        Namespace namespace = new Namespace()
//            .namespace(DEFAULT_NAMESPACE)
//            .title(DEFAULT_TITLE)
//            .publicNamespace(DEFAULT_PUBLIC_NAMESPACE)
//            .renewalPolicy(DEFAULT_RENEWAL_POLICY)
//            .namespaceStatus(DEFAULT_NAMESPACE_STATUS)
//            .registration(registration)
//            .source(source);
//        
//        return namespace;
//    }
//
//    @Before
//    public void initTest() {
//    	//create namespace
//        namespace = createEntity(em);
//        //add user
//        user = UserResourceIntTest.createEntity(em);
//        //create organization
//        organization = OrganizationResourceIntTest.createEntity(em);
//        //create organizationMembers
//        organizationMemberAdmin = new OrganizationMember().user(user).organization(organization).capacity(Capacity.ADMIN);
//        
//        organizationMemberEditor = new OrganizationMember().user(user).organization(organization).capacity(Capacity.EDITOR);
//        
//        organizationMemberMember = new OrganizationMember().user(user).organization(organization).capacity(Capacity.MEMBER);
//    }
//    
//
//    @Test
//    @Transactional
//    @WithMockUser(username=UserResourceIntTest.DEFAULT_LOGIN,password=UserResourceIntTest.DEFAULT_PASSWORD)
//    public void createNamespace() throws Exception {
//        int databaseSizeBeforeCreate = namespaceRepository.findAll().size();
//        
//        //add organization, user and organizationMember in the database
//        userService.createUser(userMapper.userToUserDTO(user));
//        organization = organizationMapper.toEntity(organizationDTOService.save(organizationMapper.toDto(organization)));
//        organizationMemberDTOService.save(organizationMemberMapper.toDto(organizationMemberAdmin));
//        
//        //asign owner to namespace
//        namespace.setOwner(organization);
//        
//        // Create the Namespace
//        NamespaceDTO namespaceDTO = namespaceMapper.toDto(namespace);
//        restNamespaceMockMvc.perform(post("/api/namespaces")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(namespaceDTO)))
//            .andExpect(status().isCreated());
//
//        // Validate the Namespace in the database
//        List<Namespace> namespaceList = namespaceRepository.findAll();
//        assertThat(namespaceList).hasSize(databaseSizeBeforeCreate + 1);
//        Namespace testNamespace = namespaceList.get(namespaceList.size() - 1);
//        assertThat(testNamespace.getNamespace()).isEqualTo(DEFAULT_NAMESPACE);
//        assertThat(testNamespace.getTitle()).isEqualTo(DEFAULT_TITLE);
//        assertThat(testNamespace.isPublicNamespace()).isEqualTo(DEFAULT_PUBLIC_NAMESPACE);
//        assertThat(testNamespace.getRenewalPolicy()).isEqualTo(DEFAULT_RENEWAL_POLICY);
//        assertThat(testNamespace.getNamespaceStatus()).isEqualTo(DEFAULT_NAMESPACE_STATUS);
//        assertThat(testNamespace.getRegistration().getProcessStatus()).isEqualTo(DEFAULT_PROCESS_STATUS);
//        assertThat(testNamespace.getRegistration().getItemStatus()).isEqualTo(DEFAULT_ITEM_STATUS);
////      assertThat(testNamespace.getRegistration().getLastChangeDate()).isEqualTo(DEFAULT_LAST_CHANGE_DATE);
////      assertThat(testNamespace.getRegistration().getRegistrationDate()).isEqualTo(DEFAULT_REGISTRATION_DATE);
//        assertThat(testNamespace.getRegistration().getLastRevisionDate()).isEqualTo(DEFAULT_LAST_REVISION_DATE);
//        assertThat(testNamespace.getRegistration().getNextRenewalDate()).isEqualTo(DEFAULT_NEXT_RENEWAL_DATE);
//        assertThat(testNamespace.getRegistration().getAnnullationDate()).isEqualTo(DEFAULT_ANNULLATION_DATE);
//        assertThat(testNamespace.getSource().getMethodType()).isEqualTo(DEFAULT_METHOD_TYPE);
//        assertThat(testNamespace.getSource().getSourceType()).isEqualTo(DEFAULT_SOURCE_TYPE);
//        assertThat(testNamespace.getSource().getEndpointLocation()).isEqualTo(DEFAULT_ENDPOINT_LOCATION);
//        assertThat(testNamespace.getSource().getSrsName()).isEqualTo(DEFAULT_SRS_NAME);
//        assertThat(testNamespace.getSource().getSchemaUri()).isEqualTo(DEFAULT_SCHEMA_URI);
//        assertThat(testNamespace.getSource().getSchemaUriGML()).isEqualTo(DEFAULT_SCHEMA_URI_GML);
//        assertThat(testNamespace.getSource().getSchemaUriBase()).isEqualTo(DEFAULT_SCHEMA_URI_BASE);
//        assertThat(testNamespace.getSource().getSchemaPrefix()).isEqualTo(DEFAULT_SCHEMA_PREFIX);
//        assertThat(testNamespace.getSource().getFeatureType()).isEqualTo(DEFAULT_FEATURE_TYPE);
//        assertThat(testNamespace.getSource().getGeometryProperty()).isEqualTo(DEFAULT_GEOMETRY_PROPERTY);
//        assertThat(testNamespace.getSource().getBeginLifespanVersionProperty()).isEqualTo(DEFAULT_BEGIN_LIFESPAN_VERSION_PROPERTY);
//        assertThat(testNamespace.getSource().getFeaturesThreshold()).isEqualTo(DEFAULT_FEATURES_THRESHOLD);
//        assertThat(testNamespace.getSource().isResolverProxyMode()).isEqualTo(DEFAULT_RESOLVER_PROXY_MODE);
//        assertThat(testNamespace.getSource().isHitsRequest()).isEqualTo(DEFAULT_HITS_REQUEST);
//        assertThat(testNamespace.getSource().getFactorK()).isEqualTo(DEFAULT_FACTOR_K);
//        assertThat(testNamespace.getSource().getXpath()).isEqualTo(DEFAULT_XPATH);
//        assertThat(testNamespace.getSource().getNameItem()).isEqualTo(DEFAULT_NAME_ITEM);
//        assertThat(testNamespace.getSource().getMaxNumRequest()).isEqualTo(DEFAULT_MAX_NUM_REQUEST);
//        assertThat(testNamespace.getSource().getBoundingBox().getMinX()).isEqualTo(DEFAULT_MIN_X);
//        assertThat(testNamespace.getSource().getBoundingBox().getMinY()).isEqualTo(DEFAULT_MIN_Y);
//        assertThat(testNamespace.getSource().getBoundingBox().getMaxX()).isEqualTo(DEFAULT_MAX_X);
//        assertThat(testNamespace.getSource().getBoundingBox().getMaxY()).isEqualTo(DEFAULT_MAX_Y);
//        assertThat(testNamespace.getVersion()).isEqualTo(FIRST_VERSION);
//    }
//
//    @Test
//    @Transactional
//    @WithMockUser(username=UserResourceIntTest.DEFAULT_LOGIN,password=UserResourceIntTest.DEFAULT_PASSWORD)
//    public void createNamespaceWithExistingId() throws Exception {
//        int databaseSizeBeforeCreate = namespaceRepository.findAll().size();
//
//        //add organization, user and organizationMember in the database
//        userService.createUser(userMapper.userToUserDTO(user));
//        organization = organizationMapper.toEntity(organizationDTOService.save(organizationMapper.toDto(organization)));
//        organizationMemberDTOService.save(organizationMemberMapper.toDto(organizationMemberAdmin));
//        
//        //asign owner to namespace
//        namespace.setOwner(organization);
//        
//        // Create the Namespace with an existing ID
//        namespace.setId(1L);
//        NamespaceDTO namespaceDTO = namespaceMapper.toDto(namespace);
//
//        // An entity with an existing ID cannot be created, so this API call must fail
//        restNamespaceMockMvc.perform(post("/api/namespaces")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(namespaceDTO)))
//            .andExpect(status().isBadRequest()).andExpect(header().string(ERROR_HEADER,ERROR_VALUE_NAMESPACE_ALREADY_EXIST ));
//
//        // Validate the Alice in the database
//        List<Namespace> namespaceList = namespaceRepository.findAll();
//        assertThat(namespaceList).hasSize(databaseSizeBeforeCreate);
//    }
//
//    @Test
//    @Transactional
//    @WithMockUser(username=UserResourceIntTest.DEFAULT_LOGIN,password=UserResourceIntTest.DEFAULT_PASSWORD)
//    public void checkNamespaceIsRequired() throws Exception {
//        int databaseSizeBeforeTest = namespaceRepository.findAll().size();
//        
//        //add organization, user and organizationMember in the database
//        userService.createUser(userMapper.userToUserDTO(user));
//        organization = organizationMapper.toEntity(organizationDTOService.save(organizationMapper.toDto(organization)));
//        organizationMemberDTOService.save(organizationMemberMapper.toDto(organizationMemberAdmin));
//        
//        // set the field null
//        namespace.setNamespace(null);
//
//        // Create the Namespace, which fails.
//        NamespaceDTO namespaceDTO = namespaceMapper.toDto(namespace);
//
//        MvcResult result = restNamespaceMockMvc.perform(post("/api/namespaces")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(namespaceDTO)))
//            .andExpect(status().isBadRequest()).andReturn();
//
//        List<Namespace> namespaceList = namespaceRepository.findAll();
//        assertThat(namespaceList).hasSize(databaseSizeBeforeTest);
//        
//        String content = result.getResponse().getContentAsString();
//        assertThat(content.contains(FIELD_NAMESPACE));
//        assertThat(content.contains(ERROR_NULL_FIELD));
//    }
//
//    @Test
//    @Transactional
//    @WithMockUser(username=UserResourceIntTest.DEFAULT_LOGIN,password=UserResourceIntTest.DEFAULT_PASSWORD)
//    public void checkPublicNamespaceIsRequired() throws Exception {
//        int databaseSizeBeforeTest = namespaceRepository.findAll().size();
//        
//        //add organization, user and organizationMember in the database
//        userService.createUser(userMapper.userToUserDTO(user));
//        organization = organizationMapper.toEntity(organizationDTOService.save(organizationMapper.toDto(organization)));
//        organizationMemberDTOService.save(organizationMemberMapper.toDto(organizationMemberAdmin));
//        
//        // set the field null
//        namespace.setPublicNamespace(null);
//
//        // Create the Namespace, which fails.
//        NamespaceDTO namespaceDTO = namespaceMapper.toDto(namespace);
//
//        MvcResult result = restNamespaceMockMvc.perform(post("/api/namespaces")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(namespaceDTO)))
//            .andExpect(status().isBadRequest()).andReturn();
//
//        List<Namespace> namespaceList = namespaceRepository.findAll();
//        assertThat(namespaceList).hasSize(databaseSizeBeforeTest);
//        
//        String content = result.getResponse().getContentAsString();
//        assertThat(content.contains(FIELD_PUBLIC_NAMESPACE));
//        assertThat(content.contains(ERROR_NULL_FIELD));
//    }
//
//    @Test
//    @Transactional
//    @WithMockUser(username=UserResourceIntTest.DEFAULT_LOGIN,password=UserResourceIntTest.DEFAULT_PASSWORD)
//    public void checkRenewalPolicyIsRequired() throws Exception {
//        int databaseSizeBeforeTest = namespaceRepository.findAll().size();
//        
//        //add organization, user and organizationMember in the database
//        userService.createUser(userMapper.userToUserDTO(user));
//        organization = organizationMapper.toEntity(organizationDTOService.save(organizationMapper.toDto(organization)));
//        organizationMemberDTOService.save(organizationMemberMapper.toDto(organizationMemberAdmin));
//        
//        // set the field null
//        namespace.setRenewalPolicy(null);
//
//        // Create the Namespace, which fails.
//        NamespaceDTO namespaceDTO = namespaceMapper.toDto(namespace);
//
//        MvcResult result = restNamespaceMockMvc.perform(post("/api/namespaces")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(namespaceDTO)))
//            .andExpect(status().isBadRequest()).andReturn();
//
//        List<Namespace> namespaceList = namespaceRepository.findAll();
//        assertThat(namespaceList).hasSize(databaseSizeBeforeTest);
//        
//        String content = result.getResponse().getContentAsString();
//        assertThat(content.contains(FIELD_RENEWALPOLICY));
//        assertThat(content.contains(ERROR_NULL_FIELD));
//    }
//
//    @Test
//    @Transactional
//    public void getAllNamespaces() throws Exception {
//        // Initialize the database
//        namespace = namespaceRepository.saveAndFlush(namespace);
//
//        // Get all the namespaceList
//        restNamespaceMockMvc.perform(get("/api/namespaces?sort=id,desc"))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(jsonPath("$.[*].id").value(hasItem(namespace.getId().intValue())))
//            .andExpect(jsonPath("$.[*].namespace").value(hasItem(DEFAULT_NAMESPACE.toString())))
//            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
//            .andExpect(jsonPath("$.[*].publicNamespace").value(hasItem(DEFAULT_PUBLIC_NAMESPACE.booleanValue())))
//            .andExpect(jsonPath("$.[*].renewalPolicy").value(hasItem(DEFAULT_RENEWAL_POLICY.toString())))
//            .andExpect(jsonPath("$.[*].namespaceStatus").value(hasItem(DEFAULT_NAMESPACE_STATUS.toString())))
//            .andExpect(jsonPath("$.[*].processStatus").value(hasItem(DEFAULT_PROCESS_STATUS.toString())))
//            .andExpect(jsonPath("$.[*].itemStatus").value(hasItem(DEFAULT_ITEM_STATUS.toString())))
//            .andExpect(jsonPath("$.[*].lastChangeDate").value(hasItem(DEFAULT_LAST_CHANGE_DATE.toString())))
//            .andExpect(jsonPath("$.[*].registrationDate").value(hasItem(DEFAULT_REGISTRATION_DATE.toString())))
//            .andExpect(jsonPath("$.[*].lastRevisionDate").value(hasItem(DEFAULT_LAST_REVISION_DATE.toString())))
//            .andExpect(jsonPath("$.[*].nextRenewalDate").value(hasItem(DEFAULT_NEXT_RENEWAL_DATE.toString())))
//            .andExpect(jsonPath("$.[*].annullationDate").value(hasItem(DEFAULT_ANNULLATION_DATE.toString())))
//            .andExpect(jsonPath("$.[*].methodType").value(hasItem(DEFAULT_METHOD_TYPE.toString())))
//            .andExpect(jsonPath("$.[*].sourceType").value(hasItem(DEFAULT_SOURCE_TYPE.toString())))
//            .andExpect(jsonPath("$.[*].endpointLocation").value(hasItem(DEFAULT_ENDPOINT_LOCATION.toString())))
//            .andExpect(jsonPath("$.[*].srsName").value(hasItem(DEFAULT_SRS_NAME.toString())))
//            .andExpect(jsonPath("$.[*].schemaUri").value(hasItem(DEFAULT_SCHEMA_URI.toString())))
//            .andExpect(jsonPath("$.[*].schemaUriGML").value(hasItem(DEFAULT_SCHEMA_URI_GML.toString())))
//            .andExpect(jsonPath("$.[*].schemaUriBase").value(hasItem(DEFAULT_SCHEMA_URI_BASE.toString())))
//            .andExpect(jsonPath("$.[*].schemaPrefix").value(hasItem(DEFAULT_SCHEMA_PREFIX.toString())))
//            .andExpect(jsonPath("$.[*].featureType").value(hasItem(DEFAULT_FEATURE_TYPE.toString())))
//            .andExpect(jsonPath("$.[*].geometryProperty").value(hasItem(DEFAULT_GEOMETRY_PROPERTY.toString())))
//            .andExpect(jsonPath("$.[*].beginLifespanVersionProperty").value(hasItem(DEFAULT_BEGIN_LIFESPAN_VERSION_PROPERTY.toString())))
//            .andExpect(jsonPath("$.[*].featuresThreshold").value(hasItem(DEFAULT_FEATURES_THRESHOLD)))
//            .andExpect(jsonPath("$.[*].resolverProxyMode").value(hasItem(DEFAULT_RESOLVER_PROXY_MODE.booleanValue())))
//            .andExpect(jsonPath("$.[*].hitsRequest").value(hasItem(DEFAULT_HITS_REQUEST.booleanValue())))
//            .andExpect(jsonPath("$.[*].factorK").value(hasItem(DEFAULT_FACTOR_K)))
//            .andExpect(jsonPath("$.[*].xpath").value(hasItem(DEFAULT_XPATH.toString())))
//            .andExpect(jsonPath("$.[*].nameItem").value(hasItem(DEFAULT_NAME_ITEM.toString())))
//            .andExpect(jsonPath("$.[*].maxNumRequest").value(hasItem(DEFAULT_MAX_NUM_REQUEST)))
//            .andExpect(jsonPath("$.[*].minX").value(hasItem(DEFAULT_MIN_X.doubleValue())))
//            .andExpect(jsonPath("$.[*].minY").value(hasItem(DEFAULT_MIN_Y.doubleValue())))
//            .andExpect(jsonPath("$.[*].maxX").value(hasItem(DEFAULT_MAX_X.doubleValue())))
//            .andExpect(jsonPath("$.[*].maxY").value(hasItem(DEFAULT_MAX_Y.doubleValue())))
//            .andExpect(jsonPath("$.[*].version").value(hasItem(FIRST_VERSION)));
//    }
//
//    @Test
//    @Transactional
//    public void getNamespace() throws Exception {
//        // Initialize the database
//        namespace = namespaceRepository.saveAndFlush(namespace);
//
//        // Get the namespace
//        restNamespaceMockMvc.perform(get("/api/namespaces/{id}", namespace.getId()))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(jsonPath("$.id").value(namespace.getId().intValue()))
//            .andExpect(jsonPath("$.namespace").value(DEFAULT_NAMESPACE.toString()))
//            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
//            .andExpect(jsonPath("$.publicNamespace").value(DEFAULT_PUBLIC_NAMESPACE.booleanValue()))
//            .andExpect(jsonPath("$.renewalPolicy").value(DEFAULT_RENEWAL_POLICY.toString()))
//            .andExpect(jsonPath("$.namespaceStatus").value(DEFAULT_NAMESPACE_STATUS.toString()))
//            .andExpect(jsonPath("$.processStatus").value(DEFAULT_PROCESS_STATUS.toString()))
//            .andExpect(jsonPath("$.itemStatus").value(DEFAULT_ITEM_STATUS.toString()))
//            .andExpect(jsonPath("$.lastChangeDate").value(DEFAULT_LAST_CHANGE_DATE.toString()))
//            .andExpect(jsonPath("$.registrationDate").value(DEFAULT_REGISTRATION_DATE.toString()))
//            .andExpect(jsonPath("$.lastRevisionDate").value(DEFAULT_LAST_REVISION_DATE.toString()))
//            .andExpect(jsonPath("$.nextRenewalDate").value(DEFAULT_NEXT_RENEWAL_DATE.toString()))
//            .andExpect(jsonPath("$.annullationDate").value(DEFAULT_ANNULLATION_DATE.toString()))
//            .andExpect(jsonPath("$.methodType").value(DEFAULT_METHOD_TYPE.toString()))
//            .andExpect(jsonPath("$.sourceType").value(DEFAULT_SOURCE_TYPE.toString()))
//            .andExpect(jsonPath("$.endpointLocation").value(DEFAULT_ENDPOINT_LOCATION.toString()))
//            .andExpect(jsonPath("$.srsName").value(DEFAULT_SRS_NAME.toString()))
//            .andExpect(jsonPath("$.schemaUri").value(DEFAULT_SCHEMA_URI.toString()))
//            .andExpect(jsonPath("$.schemaUriGML").value(DEFAULT_SCHEMA_URI_GML.toString()))
//            .andExpect(jsonPath("$.schemaUriBase").value(DEFAULT_SCHEMA_URI_BASE.toString()))
//            .andExpect(jsonPath("$.schemaPrefix").value(DEFAULT_SCHEMA_PREFIX.toString()))
//            .andExpect(jsonPath("$.featureType").value(DEFAULT_FEATURE_TYPE.toString()))
//            .andExpect(jsonPath("$.geometryProperty").value(DEFAULT_GEOMETRY_PROPERTY.toString()))
//            .andExpect(jsonPath("$.beginLifespanVersionProperty").value(DEFAULT_BEGIN_LIFESPAN_VERSION_PROPERTY.toString()))
//            .andExpect(jsonPath("$.featuresThreshold").value(DEFAULT_FEATURES_THRESHOLD))
//            .andExpect(jsonPath("$.resolverProxyMode").value(DEFAULT_RESOLVER_PROXY_MODE.booleanValue()))
//            .andExpect(jsonPath("$.hitsRequest").value(DEFAULT_HITS_REQUEST.booleanValue()))
//            .andExpect(jsonPath("$.factorK").value(DEFAULT_FACTOR_K))
//            .andExpect(jsonPath("$.xpath").value(DEFAULT_XPATH.toString()))
//            .andExpect(jsonPath("$.nameItem").value(DEFAULT_NAME_ITEM.toString()))
//            .andExpect(jsonPath("$.maxNumRequest").value(DEFAULT_MAX_NUM_REQUEST))
//            .andExpect(jsonPath("$.minX").value(DEFAULT_MIN_X.doubleValue()))
//            .andExpect(jsonPath("$.minY").value(DEFAULT_MIN_Y.doubleValue()))
//            .andExpect(jsonPath("$.maxX").value(DEFAULT_MAX_X.doubleValue()))
//            .andExpect(jsonPath("$.maxY").value(DEFAULT_MAX_Y.doubleValue()))
//            .andExpect(jsonPath("$.version").value(FIRST_VERSION));
//    }
//
//    @Test
//    @Transactional
//    public void getNonExistingNamespace() throws Exception {
//        // Get the namespace
//        restNamespaceMockMvc.perform(get("/api/namespaces/{id}", Long.MAX_VALUE))
//            .andExpect(status().isNotFound());
//    }
//
//    @Test
//    @Transactional
//    @WithMockUser(username=UserResourceIntTest.DEFAULT_LOGIN,password=UserResourceIntTest.DEFAULT_PASSWORD)
//    public void updateNamespace() throws Exception {
//    	
//    	//add organization, user and organizationMember in the database
//        userService.createUser(userMapper.userToUserDTO(user));
//        organization = organizationMapper.toEntity(organizationDTOService.save(organizationMapper.toDto(organization)));
//        organizationMemberDTOService.save(organizationMemberMapper.toDto(organizationMemberAdmin));
//        
//    	//asign owner to namespace
//        namespace.setOwner(organization);
//        
//        // Initialize the database
//        namespace = namespaceRepository.saveAndFlush(namespace);
//        int databaseSizeBeforeUpdate = namespaceRepository.findAll().size();
//        
//        // Update the namespace
//        Namespace updatedNamespace = namespaceRepository.findOne(namespace.getId());
//
//        updatedNamespace
//            .namespace(UPDATED_NAMESPACE)
//            .title(UPDATED_TITLE)
////          .publicNamespace(UPDATED_PUBLIC_NAMESPACE)
//            .renewalPolicy(UPDATED_RENEWAL_POLICY)
//            .namespaceStatus(UPDATED_NAMESPACE_STATUS)
//            .getSource()
//            .methodType(UPDATED_METHOD_TYPE)
//            .sourceType(UPDATED_SOURCE_TYPE)
//            .endpointLocation(UPDATED_ENDPOINT_LOCATION)
//            .srsName(UPDATED_SRS_NAME)
//            .schemaUri(UPDATED_SCHEMA_URI)
//            .schemaUriGML(UPDATED_SCHEMA_URI_GML)
//            .schemaUriBase(UPDATED_SCHEMA_URI_BASE)
//            .schemaPrefix(UPDATED_SCHEMA_PREFIX)
//            .featureType(UPDATED_FEATURE_TYPE)
//            .geometryProperty(UPDATED_GEOMETRY_PROPERTY)
//            .beginLifespanVersionProperty(UPDATED_BEGIN_LIFESPAN_VERSION_PROPERTY)
//            .featuresThreshold(UPDATED_FEATURES_THRESHOLD)
//            .resolverProxyMode(UPDATED_RESOLVER_PROXY_MODE)
//            .hitsRequest(UPDATED_HITS_REQUEST)
//            .factorK(UPDATED_FACTOR_K)
//            .xpath(UPDATED_XPATH)
//            .nameItem(UPDATED_NAME_ITEM)
//            .maxNumRequest(UPDATED_MAX_NUM_REQUEST)
//            .boundingBox(UPDATED_BOUNDING_BOX);
//
//        updatedNamespace.getRegistration()
//            .processStatus(UPDATED_PROCESS_STATUS)
//            .itemStatus(UPDATED_ITEM_STATUS)
//            .lastChangeDate(UPDATED_LAST_CHANGE_DATE)
//            .registrationDate(UPDATED_REGISTRATION_DATE)
//            .lastRevisionDate(UPDATED_LAST_REVISION_DATE)
//            .nextRenewalDate(UPDATED_NEXT_RENEWAL_DATE)
//            .annullationDate(UPDATED_ANNULLATION_DATE);
//
//        NamespaceDTO namespaceDTO = namespaceMapper.toDto(updatedNamespace);
//
//        restNamespaceMockMvc.perform(put("/api/namespaces")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(namespaceDTO)))
//            .andExpect(status().isOk());
//
//        // Validate the Namespace in the database
//        List<Namespace> namespaceList = namespaceRepository.findAll();
//        assertThat(namespaceList).hasSize(databaseSizeBeforeUpdate);
//        Namespace testNamespace = namespaceList.get(namespaceList.size() - 1);
//        assertThat(testNamespace.getNamespace()).isEqualTo(UPDATED_NAMESPACE);
//        assertThat(testNamespace.getTitle()).isEqualTo(UPDATED_TITLE);
////      assertThat(testNamespace.isPublicNamespace()).isEqualTo(UPDATED_PUBLIC_NAMESPACE);
//        assertThat(testNamespace.getRenewalPolicy()).isEqualTo(UPDATED_RENEWAL_POLICY);
//        assertThat(testNamespace.getNamespaceStatus()).isEqualTo(UPDATED_NAMESPACE_STATUS);
//        assertThat(testNamespace.getRegistration().getProcessStatus()).isEqualTo(UPDATED_PROCESS_STATUS);
//        assertThat(testNamespace.getRegistration().getItemStatus()).isEqualTo(UPDATED_ITEM_STATUS);
//        assertThat(testNamespace.getRegistration().getLastChangeDate()).isEqualTo(UPDATED_LAST_CHANGE_DATE);
//        assertThat(testNamespace.getRegistration().getRegistrationDate()).isEqualTo(UPDATED_REGISTRATION_DATE);
//        assertThat(testNamespace.getRegistration().getLastRevisionDate()).isEqualTo(UPDATED_LAST_REVISION_DATE);
//        assertThat(testNamespace.getRegistration().getNextRenewalDate()).isEqualTo(UPDATED_NEXT_RENEWAL_DATE);
//        assertThat(testNamespace.getRegistration().getAnnullationDate()).isEqualTo(UPDATED_ANNULLATION_DATE);
//        assertThat(testNamespace.getSource().getMethodType()).isEqualTo(UPDATED_METHOD_TYPE);
//        assertThat(testNamespace.getSource().getSourceType()).isEqualTo(UPDATED_SOURCE_TYPE);
//        assertThat(testNamespace.getSource().getEndpointLocation()).isEqualTo(UPDATED_ENDPOINT_LOCATION);
//        assertThat(testNamespace.getSource().getSrsName()).isEqualTo(UPDATED_SRS_NAME);
//        assertThat(testNamespace.getSource().getSchemaUri()).isEqualTo(UPDATED_SCHEMA_URI);
//        assertThat(testNamespace.getSource().getSchemaUriGML()).isEqualTo(UPDATED_SCHEMA_URI_GML);
//        assertThat(testNamespace.getSource().getSchemaUriBase()).isEqualTo(UPDATED_SCHEMA_URI_BASE);
//        assertThat(testNamespace.getSource().getSchemaPrefix()).isEqualTo(UPDATED_SCHEMA_PREFIX);
//        assertThat(testNamespace.getSource().getFeatureType()).isEqualTo(UPDATED_FEATURE_TYPE);
//        assertThat(testNamespace.getSource().getGeometryProperty()).isEqualTo(UPDATED_GEOMETRY_PROPERTY);
//        assertThat(testNamespace.getSource().getBeginLifespanVersionProperty()).isEqualTo(UPDATED_BEGIN_LIFESPAN_VERSION_PROPERTY);
//        assertThat(testNamespace.getSource().getFeaturesThreshold()).isEqualTo(UPDATED_FEATURES_THRESHOLD);
//        assertThat(testNamespace.getSource().isResolverProxyMode()).isEqualTo(UPDATED_RESOLVER_PROXY_MODE);
//        assertThat(testNamespace.getSource().isHitsRequest()).isEqualTo(UPDATED_HITS_REQUEST);
//        assertThat(testNamespace.getSource().getFactorK()).isEqualTo(UPDATED_FACTOR_K);
//        assertThat(testNamespace.getSource().getXpath()).isEqualTo(UPDATED_XPATH);
//        assertThat(testNamespace.getSource().getNameItem()).isEqualTo(UPDATED_NAME_ITEM);
//        assertThat(testNamespace.getSource().getMaxNumRequest()).isEqualTo(UPDATED_MAX_NUM_REQUEST);
//        assertThat(testNamespace.getSource().getBoundingBox().getMinX()).isEqualTo(UPDATED_MIN_X);
//        assertThat(testNamespace.getSource().getBoundingBox().getMinY()).isEqualTo(UPDATED_MIN_Y);
//        assertThat(testNamespace.getSource().getBoundingBox().getMaxX()).isEqualTo(UPDATED_MAX_X);
//        assertThat(testNamespace.getSource().getBoundingBox().getMaxY()).isEqualTo(UPDATED_MAX_Y);
//        assertThat(testNamespace.getVersion()).isEqualTo(NEXT_VERSION);
//    }
//
//    @Test
//    @Transactional
//    @WithMockUser(username=UserResourceIntTest.DEFAULT_LOGIN,password=UserResourceIntTest.DEFAULT_PASSWORD)
//    public void updateNonExistingNamespace() throws Exception {
//    	
//    	//add organization, user and organizationMember in the database
//        userService.createUser(userMapper.userToUserDTO(user));
//        organization = organizationMapper.toEntity(organizationDTOService.save(organizationMapper.toDto(organization)));
//        organizationMemberDTOService.save(organizationMemberMapper.toDto(organizationMemberAdmin));
//        
//    	//asign owner to namespace
//        namespace.setOwner(organization);
//        
//        int databaseSizeBeforeUpdate = namespaceRepository.findAll().size();
//
//        // Create the Namespace
//        NamespaceDTO namespaceDTO = namespaceMapper.toDto(namespace);
//
//        // If the entity doesn't have an ID, it will be created instead of just being updated
//        restNamespaceMockMvc.perform(put("/api/namespaces")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(namespaceDTO)))
//            .andExpect(status().isCreated());
//
//        // Validate the Namespace in the database
//        List<Namespace> namespaceList = namespaceRepository.findAll();
//        assertThat(namespaceList).hasSize(databaseSizeBeforeUpdate + 1);
//    }
//
//    @Test
//    @Transactional
//    @WithMockUser(username=UserResourceIntTest.DEFAULT_LOGIN,password=UserResourceIntTest.DEFAULT_PASSWORD)
//    public void deleteNamespace() throws Exception {
//    	//add organization, user and organizationMember in the database
//        userService.createUser(userMapper.userToUserDTO(user));
//        organization = organizationMapper.toEntity(organizationDTOService.save(organizationMapper.toDto(organization)));
//        organizationMemberDTOService.save(organizationMemberMapper.toDto(organizationMemberAdmin));
//        
//    	//asign owner to namespace
//        namespace.setOwner(organization);
//    	
//        // Initialize the database
//        namespace = namespaceRepository.saveAndFlush(namespace);
//        int databaseSizeBeforeDelete = namespaceRepository.findAll().size();
//
//        // Get the namespace
//        restNamespaceMockMvc.perform(delete("/api/namespaces/{id}", namespace.getId())
//            .accept(TestUtil.APPLICATION_JSON_UTF8))
//            .andExpect(status().isOk());
//
//        // Validate the database is empty
//        List<Namespace> namespaceList = namespaceRepository.findAll();
//        assertThat(namespaceList).hasSize(databaseSizeBeforeDelete - 1);
//    }
//
//    @Test
//    @Transactional
//    public void equalsVerifier() throws Exception {
//        TestUtil.equalsVerifier(Namespace.class);
//        Namespace namespace1 = new Namespace();
//        namespace1.setId(1L);
//        Namespace namespace2 = new Namespace();
//        namespace2.setId(namespace1.getId());
//        assertThat(namespace1).isEqualTo(namespace2);
//        namespace2.setId(2L);
//        assertThat(namespace1).isNotEqualTo(namespace2);
//        namespace1.setId(null);
//        assertThat(namespace1).isNotEqualTo(namespace2);
//    }
//
//    @Test
//    @Transactional
//    public void dtoEqualsVerifier() throws Exception {
//        TestUtil.equalsVerifier(NamespaceDTO.class);
//        NamespaceDTO namespaceDTO1 = new NamespaceDTO();
//        namespaceDTO1.setId(1L);
//        NamespaceDTO namespaceDTO2 = new NamespaceDTO();
//        assertThat(namespaceDTO1).isNotEqualTo(namespaceDTO2);
//        namespaceDTO2.setId(namespaceDTO1.getId());
//        assertThat(namespaceDTO1).isEqualTo(namespaceDTO2);
//        namespaceDTO2.setId(2L);
//        assertThat(namespaceDTO1).isNotEqualTo(namespaceDTO2);
//        namespaceDTO1.setId(null);
//        assertThat(namespaceDTO1).isNotEqualTo(namespaceDTO2);
//    }
//
//    @Test
//    @Transactional
//    public void testEntityFromId() {
//        assertThat(namespaceMapper.fromId(42L).getId()).isEqualTo(42);
//        assertThat(namespaceMapper.fromId(null)).isNull();
//    }
}
