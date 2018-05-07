package es.unizar.iaaa.pid.web.rest.util;

import es.unizar.iaaa.pid.domain.*;
import es.unizar.iaaa.pid.domain.enumeration.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class ResourceFixtures {

    public static final String DEFAULT_NAME = "AAAAAAAAAA";
    public static final String UPDATED_NAME = "BBBBBBBBBB";

    public static final String DEFAULT_TITLE = "AAAAAAAAAA";
    public static final String UPDATED_TITLE = "BBBBBBBBBB";

    public static final Instant DEFAULT_TIMESTAMP = Instant.ofEpochMilli(0L);
    public static final Instant UPDATED_TIMESTAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    public static final ProcessStatus DEFAULT_TYPE = ProcessStatus.PENDING_PREPARING_HARVEST;
    public static final ProcessStatus UPDATED_TYPE = ProcessStatus.PREPARING_HARVEST;

    public static final TaskStatus DEFAULT_STATUS = TaskStatus.IN_QUEUE;
    public static final TaskStatus UPDATED_STATUS = TaskStatus.EXECUTING;

    public static final Integer DEFAULT_NUM_ERRORS = 0;
    public static final Integer UPDATED_NUM_ERRORS = 1;

    public static final String DEFAULT_NAMESPACE = "AAAAAAAAAA";
    public static final String UPDATED_NAMESPACE = "BBBBBBBBBB";

    public static final Boolean DEFAULT_PUBLIC_NAMESPACE = true;
    // public static final Boolean UPDATED_PUBLIC_NAMESPACE = true;

    public static final RenewalPolicy DEFAULT_RENEWAL_POLICY = RenewalPolicy.NONE;
    public static final RenewalPolicy UPDATED_RENEWAL_POLICY = RenewalPolicy.CONTINUOUS;

    public static final NamespaceStatus DEFAULT_NAMESPACE_STATUS = NamespaceStatus.STOP;
    public static final NamespaceStatus UPDATED_NAMESPACE_STATUS = NamespaceStatus.GO;

    public static final ProcessStatus DEFAULT_PROCESS_STATUS = ProcessStatus.NONE;
    public static final ProcessStatus UPDATED_PROCESS_STATUS = ProcessStatus.PREPARING_HARVEST;

    public static final ItemStatus DEFAULT_ITEM_STATUS = ItemStatus.PENDING_VALIDATION;
    public static final ItemStatus UPDATED_ITEM_STATUS = ItemStatus.LAPSED;

    public static final Instant DEFAULT_LAST_CHANGE_DATE = Instant.ofEpochMilli(0L);
    public static final Instant UPDATED_LAST_CHANGE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    public static final Instant DEFAULT_REGISTRATION_DATE = Instant.ofEpochMilli(0L);
    public static final Instant UPDATED_REGISTRATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    public static final Instant DEFAULT_LAST_REVISION_DATE = Instant.ofEpochMilli(0L);
    public static final Instant UPDATED_LAST_REVISION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    public static final Instant DEFAULT_NEXT_RENEWAL_DATE = Instant.ofEpochMilli(0L);
    public static final Instant UPDATED_NEXT_RENEWAL_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    public static final Instant DEFAULT_ANNULLATION_DATE = Instant.ofEpochMilli(0L);
    public static final Instant UPDATED_ANNULLATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    public static final MethodType DEFAULT_METHOD_TYPE = MethodType.GET;
    public static final MethodType UPDATED_METHOD_TYPE = MethodType.POST;

    public static final SourceType DEFAULT_SOURCE_TYPE = SourceType.WFS;
    public static final SourceType UPDATED_SOURCE_TYPE = SourceType.WFS;

    public static final String DEFAULT_ENDPOINT_LOCATION = "AAAAAAAAAA";
    public static final String UPDATED_ENDPOINT_LOCATION = "BBBBBBBBBB";

    public static final Boolean DEFAULT_RESOLVER_PROXY_MODE = false;
    public static final Boolean UPDATED_RESOLVER_PROXY_MODE = true;

    public static final Integer DEFAULT_MAX_NUM_REQUEST = 0;
    public static final Integer UPDATED_MAX_NUM_REQUEST = 1;

    public static final Integer FIRST_VERSION = 0;
    public static final Integer NEXT_VERSION = 1;

    public static final Capacity DEFAULT_CAPACITY = Capacity.ADMIN;
    public static final Capacity UPDATED_CAPACITY = Capacity.EDITOR;

    public static final String ERROR_HEADER = "X-pidmsApp-error";
    public static final String ERROR_VALUE_NAMESPACE_ALREADY_EXIST = "error.idexists";

    public static final String FIELD_NAMESPACE = "namespace";
    public static final String FIELD_PUBLIC_NAMESPACE = "publicNamespace";
    public static final String FIELD_RENEWALPOLICY = "renewalPolicy";
    public static final String FIELD_CAPACITY = "capacity";

    public static final String ERROR_NULL_FIELD = "NotNull";



    public static final Instant DEFAULT_CHANGE_TIMESTAMP = Instant.ofEpochMilli(0L);
    public static final Instant UPDATED_CHANGE_TIMESTAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    public static final ChangeAction DEFAULT_ACTION = ChangeAction.ISSUED;
    public static final ChangeAction UPDATED_ACTION = ChangeAction.CANCELLED;

    public static final String DEFAULT_FEATURE = "AAAAAAAAAA";
    public static final String UPDATED_FEATURE = "BBBBBBBBBB";

    public static final String DEFAULT_LOCAL_ID = "AAAAAAAAAA";
    public static final String UPDATED_LOCAL_ID = "BBBBBBBBBB";

    public static final String DEFAULT_VERSION_ID = "AAAAAAAAAA";
    public static final String UPDATED_VERSION_ID = "BBBBBBBBBB";

    public static final Instant DEFAULT_BEGIN_LIFESPAN_VERSION = Instant.ofEpochMilli(0L);
    public static final Instant UPDATED_BEGIN_LIFESPAN_VERSION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    public static final Instant DEFAULT_END_LIFESPAN_VERSION = Instant.ofEpochMilli(0L);
    public static final Instant UPDATED_END_LIFESPAN_VERSION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    public static final String DEFAULT_ALTERNATE_ID = "AAAAAAAAAA";
    public static final String UPDATED_ALTERNATE_ID = "BBBBBBBBBB";

    public static final ResourceType DEFAULT_RESOURCE_TYPE = ResourceType.DATASET;
    public static final ResourceType UPDATED_RESOURCE_TYPE = ResourceType.SPATIAL_OBJECT;

    public static final String DEFAULT_LOCATOR = "AAAAAAAAAA";
    public static final String UPDATED_LOCATOR = "BBBBBBBBBB";

    public static final String DEFAULT_SRS_NAME = "AAAAAAAAAA";
    public static final String DEFAULT_SCHEMA_URI = "AAAAAAAAAA";
    public static final String DEFAULT_SCHEMA_URI_GML = "AAAAAAAAAA";
    public static final String DEFAULT_SCHEMA_PREFIX = "AAAAAAAAAA";
    public static final String DEFAULT_SCHEMA_URI_BASE = "AAAAAAAAAA";
    public static final String DEFAULT_FEATURE_TYPE = "AAAAAAAAAA";
    public static final String DEFAULT_GEOMETRY_PROPERTY = "AAAAAAAAAA";
    public static final String DEFAULT_BEGIN_LIFESPAN_VERSION_PROPERTY = "AAAAAAAAAA";
    public static final Integer DEFAULT_FEATURES_THRESHOLD = 0;
    public static final boolean DEFAULT_HITS_REQUEST = false;
    public static final Integer DEFAULT_FACTOR_K = 0;
    public static final String DEFAULT_XPATH = "AAAAAAAAAA";
    public static final String DEFAULT_NAME_ITEM = "AAAAAAAAAA";
    public static final BoundingBox DEFAULT_BOUNDING_BOX = new BoundingBox(-180.0, -90.0, 180.0, 90.0);

    public static final String DEFAULT_ID = "0403a1e9-359d-3a62-b937-baddd0bf612f";
    public static final String UPDATED_ID = "0403a1e9-359d-3a62-b937-baddd0bf612f";

    public static final String DEFAULT_EXTERNAL_URN = "urn:inspire:ES:/AAAAAAAAAA/AAAAAAAAAA/AAAAAAAAAA";
    public static final String UPDATED_EXTERNAL_URN = "urn:inspire:ES:/BBBBBBBBBB/BBBBBBBBBB/BBBBBBBBBB";

    public static final UUID UUID_1 = UUID.nameUUIDFromBytes("1".getBytes());
    public static final UUID UUID_2 = UUID.nameUUIDFromBytes("2".getBytes());
    public static final UUID UUID_42 = UUID.nameUUIDFromBytes("42".getBytes());

    public static Organization organization() {
        return new Organization()
            .name(DEFAULT_NAME)
            .title(DEFAULT_TITLE);
    }

    public static Organization anonymousOrganization() {
        return new Organization()
            .name(null)
            .title(DEFAULT_TITLE);
    }

    public static Organization organization(long id) {
        Organization newOrganization = organization();
        newOrganization.setId(id);
        return newOrganization;
    }

    public static Task task(Namespace namespace) {
        return new Task()
            .timestamp(DEFAULT_TIMESTAMP)
            .type(DEFAULT_TYPE)
            .status(DEFAULT_STATUS)
            .numErrors(DEFAULT_NUM_ERRORS)
            .namespace(namespace);
    }


    public static Feature feature(Namespace namespace) {
        return new Feature()
        		.srsName(DEFAULT_SRS_NAME)
                .schemaUri(DEFAULT_SCHEMA_URI)
                .schemaUriGML(DEFAULT_SCHEMA_URI_GML)
                .schemaUriBase(DEFAULT_SCHEMA_URI_BASE)
                .schemaPrefix(DEFAULT_SCHEMA_PREFIX)
                .featureType(DEFAULT_FEATURE_TYPE)
                .geometryProperty(DEFAULT_GEOMETRY_PROPERTY)
                .beginLifespanVersionProperty(DEFAULT_BEGIN_LIFESPAN_VERSION_PROPERTY)
                .featuresThreshold(DEFAULT_FEATURES_THRESHOLD)
                .hitsRequest(DEFAULT_HITS_REQUEST)
                .factorK(DEFAULT_FACTOR_K)
                .xpath(DEFAULT_XPATH)
                .nameItem(DEFAULT_NAME_ITEM)
                .boundingBox(DEFAULT_BOUNDING_BOX)
                .namespace(namespace);
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Namespace namespace() {
        Source source = new Source()
            .methodType(DEFAULT_METHOD_TYPE)
            .sourceType(DEFAULT_SOURCE_TYPE)
            .resolverProxyMode(DEFAULT_RESOLVER_PROXY_MODE)
            .maxNumRequest(DEFAULT_MAX_NUM_REQUEST)
            .endpointLocation(DEFAULT_ENDPOINT_LOCATION);

        Registration registration = new Registration()
            .processStatus(DEFAULT_PROCESS_STATUS)
            .itemStatus(DEFAULT_ITEM_STATUS)
            .lastChangeDate(DEFAULT_LAST_CHANGE_DATE)
            .registrationDate(DEFAULT_REGISTRATION_DATE)
            .lastRevisionDate(DEFAULT_LAST_REVISION_DATE)
            .nextRenewalDate(DEFAULT_NEXT_RENEWAL_DATE)
            .annullationDate(DEFAULT_ANNULLATION_DATE);

        Organization owner = ResourceFixtures.organization();

        Namespace namespace = new Namespace()
            .namespace(DEFAULT_NAMESPACE)
            .title(DEFAULT_TITLE)
            .publicNamespace(DEFAULT_PUBLIC_NAMESPACE)
            .renewalPolicy(DEFAULT_RENEWAL_POLICY)
            .namespaceStatus(DEFAULT_NAMESPACE_STATUS)
            .registration(registration)
            .source(source)
            .owner(owner);


        return namespace;
    }


    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Change change(Task task, Feature feature) {
        Resource resource = new Resource()
            .resourceType(DEFAULT_RESOURCE_TYPE)
            .locator(DEFAULT_LOCATOR);

        Identifier identifier = new Identifier()
            .namespace(DEFAULT_NAMESPACE)
            .localId(DEFAULT_LOCAL_ID)
            .versionId(DEFAULT_VERSION_ID)
            .beginLifespanVersion(DEFAULT_BEGIN_LIFESPAN_VERSION)
            .endLifespanVersion(DEFAULT_END_LIFESPAN_VERSION)
            .alternateId(DEFAULT_ALTERNATE_ID);

        return new Change()
            .changeTimestamp(DEFAULT_CHANGE_TIMESTAMP)
            .action(DEFAULT_ACTION)
            .feature(feature)
            .identifier(identifier)
            .resource(resource)
            .task(task);
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PersistentIdentifier persistentIdentifier(Feature feature) {
        Resource resource = new Resource()
            .resourceType(DEFAULT_RESOURCE_TYPE)
            .locator(DEFAULT_LOCATOR);

        Registration registration = new Registration()
            .processStatus(DEFAULT_PROCESS_STATUS)
            .itemStatus(DEFAULT_ITEM_STATUS)
            .lastChangeDate(DEFAULT_LAST_CHANGE_DATE)
            .registrationDate(DEFAULT_REGISTRATION_DATE)
            .lastRevisionDate(DEFAULT_LAST_REVISION_DATE)
            .nextRenewalDate(DEFAULT_NEXT_RENEWAL_DATE)
            .annullationDate(DEFAULT_ANNULLATION_DATE);

        Identifier identifier = new Identifier()
            .namespace(DEFAULT_NAMESPACE)
            .localId(DEFAULT_LOCAL_ID)
            .versionId(DEFAULT_VERSION_ID)
            .beginLifespanVersion(DEFAULT_BEGIN_LIFESPAN_VERSION)
            .endLifespanVersion(DEFAULT_END_LIFESPAN_VERSION)
            .alternateId(DEFAULT_ALTERNATE_ID);

        PersistentIdentifier persistentIdentifier = new PersistentIdentifier()
            .feature(feature)
            .resolverProxyMode(DEFAULT_RESOLVER_PROXY_MODE)
            .identifier(identifier)
            .resource(resource)
            .registration(registration);

        return persistentIdentifier;
    }
}
