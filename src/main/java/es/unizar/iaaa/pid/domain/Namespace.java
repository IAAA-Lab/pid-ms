package es.unizar.iaaa.pid.domain;

import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import es.unizar.iaaa.pid.domain.enumeration.RenewalPolicy;

import es.unizar.iaaa.pid.domain.enumeration.NamespaceStatus;

import es.unizar.iaaa.pid.domain.enumeration.ProcessStatus;

import es.unizar.iaaa.pid.domain.enumeration.ItemStatus;

import es.unizar.iaaa.pid.domain.enumeration.MethodType;

import es.unizar.iaaa.pid.domain.enumeration.SourceType;

/**
 * Entity Namespace
 */
@ApiModel(description = "Entity Namespace")
@Entity
@Table(name = "namespace")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Namespace implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "namespace", nullable = false)
    private String namespace;

    @Column(name = "title")
    private String title;

    @NotNull
    @Column(name = "public_namespace", nullable = false)
    private Boolean publicNamespace;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "renewal_policy", nullable = false)
    private RenewalPolicy renewalPolicy;

    @Enumerated(EnumType.STRING)
    @Column(name = "namespace_status")
    private NamespaceStatus namespaceStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "process_status")
    private ProcessStatus processStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "item_status")
    private ItemStatus itemStatus;

    @Column(name = "last_change_date")
    private Instant lastChangeDate;

    @Column(name = "registration_date")
    private Instant registrationDate;

    @Column(name = "last_revision_date")
    private Instant lastRevisionDate;

    @Column(name = "next_renewal_date")
    private Instant nextRenewalDate;

    @Column(name = "annullation_date")
    private Instant annullationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "method_type")
    private MethodType methodType;

    @Enumerated(EnumType.STRING)
    @Column(name = "source_type")
    private SourceType sourceType;

    @Column(name = "endpoint_location")
    private String endpointLocation;

    @Column(name = "srs_name")
    private String srsName;

    @Column(name = "schema_uri")
    private String schemaUri;

    @Column(name = "schema_uri_gml")
    private String schemaUriGML;

    @Column(name = "schema_uri_base")
    private String schemaUriBase;

    @Column(name = "schema_prefix")
    private String schemaPrefix;

    @Column(name = "feature_type")
    private String featureType;

    @Column(name = "geometry_property")
    private String geometryProperty;

    @Column(name = "begin_lifespan_version_property")
    private String beginLifespanVersionProperty;

    @Column(name = "features_threshold")
    private Integer featuresThreshold;

    @Column(name = "resolver_proxy_mode")
    private Boolean resolverProxyMode;

    @Column(name = "hits_request")
    private Boolean hitsRequest;

    @Column(name = "factor_k")
    private Integer factorK;

    @Column(name = "xpath")
    private String xpath;

    @Column(name = "name_item")
    private String nameItem;

    @Min(value = 0)
    @Column(name = "max_num_request")
    private Integer maxNumRequest;

    @Column(name = "min_x")
    private Double minX;

    @Column(name = "min_y")
    private Double minY;

    @Column(name = "max_x")
    private Double maxX;

    @Column(name = "max_y")
    private Double maxY;

    @ManyToOne
    private Organization owner;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNamespace() {
        return namespace;
    }

    public Namespace namespace(String namespace) {
        this.namespace = namespace;
        return this;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getTitle() {
        return title;
    }

    public Namespace title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean isPublicNamespace() {
        return publicNamespace;
    }

    public Namespace publicNamespace(Boolean publicNamespace) {
        this.publicNamespace = publicNamespace;
        return this;
    }

    public void setPublicNamespace(Boolean publicNamespace) {
        this.publicNamespace = publicNamespace;
    }

    public RenewalPolicy getRenewalPolicy() {
        return renewalPolicy;
    }

    public Namespace renewalPolicy(RenewalPolicy renewalPolicy) {
        this.renewalPolicy = renewalPolicy;
        return this;
    }

    public void setRenewalPolicy(RenewalPolicy renewalPolicy) {
        this.renewalPolicy = renewalPolicy;
    }

    public NamespaceStatus getNamespaceStatus() {
        return namespaceStatus;
    }

    public Namespace namespaceStatus(NamespaceStatus namespaceStatus) {
        this.namespaceStatus = namespaceStatus;
        return this;
    }

    public void setNamespaceStatus(NamespaceStatus namespaceStatus) {
        this.namespaceStatus = namespaceStatus;
    }

    public ProcessStatus getProcessStatus() {
        return processStatus;
    }

    public Namespace processStatus(ProcessStatus processStatus) {
        this.processStatus = processStatus;
        return this;
    }

    public void setProcessStatus(ProcessStatus processStatus) {
        this.processStatus = processStatus;
    }

    public ItemStatus getItemStatus() {
        return itemStatus;
    }

    public Namespace itemStatus(ItemStatus itemStatus) {
        this.itemStatus = itemStatus;
        return this;
    }

    public void setItemStatus(ItemStatus itemStatus) {
        this.itemStatus = itemStatus;
    }

    public Instant getLastChangeDate() {
        return lastChangeDate;
    }

    public Namespace lastChangeDate(Instant lastChangeDate) {
        this.lastChangeDate = lastChangeDate;
        return this;
    }

    public void setLastChangeDate(Instant lastChangeDate) {
        this.lastChangeDate = lastChangeDate;
    }

    public Instant getRegistrationDate() {
        return registrationDate;
    }

    public Namespace registrationDate(Instant registrationDate) {
        this.registrationDate = registrationDate;
        return this;
    }

    public void setRegistrationDate(Instant registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Instant getLastRevisionDate() {
        return lastRevisionDate;
    }

    public Namespace lastRevisionDate(Instant lastRevisionDate) {
        this.lastRevisionDate = lastRevisionDate;
        return this;
    }

    public void setLastRevisionDate(Instant lastRevisionDate) {
        this.lastRevisionDate = lastRevisionDate;
    }

    public Instant getNextRenewalDate() {
        return nextRenewalDate;
    }

    public Namespace nextRenewalDate(Instant nextRenewalDate) {
        this.nextRenewalDate = nextRenewalDate;
        return this;
    }

    public void setNextRenewalDate(Instant nextRenewalDate) {
        this.nextRenewalDate = nextRenewalDate;
    }

    public Instant getAnnullationDate() {
        return annullationDate;
    }

    public Namespace annullationDate(Instant annullationDate) {
        this.annullationDate = annullationDate;
        return this;
    }

    public void setAnnullationDate(Instant annullationDate) {
        this.annullationDate = annullationDate;
    }

    public MethodType getMethodType() {
        return methodType;
    }

    public Namespace methodType(MethodType methodType) {
        this.methodType = methodType;
        return this;
    }

    public void setMethodType(MethodType methodType) {
        this.methodType = methodType;
    }

    public SourceType getSourceType() {
        return sourceType;
    }

    public Namespace sourceType(SourceType sourceType) {
        this.sourceType = sourceType;
        return this;
    }

    public void setSourceType(SourceType sourceType) {
        this.sourceType = sourceType;
    }

    public String getEndpointLocation() {
        return endpointLocation;
    }

    public Namespace endpointLocation(String endpointLocation) {
        this.endpointLocation = endpointLocation;
        return this;
    }

    public void setEndpointLocation(String endpointLocation) {
        this.endpointLocation = endpointLocation;
    }

    public String getSrsName() {
        return srsName;
    }

    public Namespace srsName(String srsName) {
        this.srsName = srsName;
        return this;
    }

    public void setSrsName(String srsName) {
        this.srsName = srsName;
    }

    public String getSchemaUri() {
        return schemaUri;
    }

    public Namespace schemaUri(String schemaUri) {
        this.schemaUri = schemaUri;
        return this;
    }

    public void setSchemaUri(String schemaUri) {
        this.schemaUri = schemaUri;
    }

    public String getSchemaUriGML() {
        return schemaUriGML;
    }

    public Namespace schemaUriGML(String schemaUriGML) {
        this.schemaUriGML = schemaUriGML;
        return this;
    }

    public void setSchemaUriGML(String schemaUriGML) {
        this.schemaUriGML = schemaUriGML;
    }

    public String getSchemaUriBase() {
        return schemaUriBase;
    }

    public Namespace schemaUriBase(String schemaUriBase) {
        this.schemaUriBase = schemaUriBase;
        return this;
    }

    public void setSchemaUriBase(String schemaUriBase) {
        this.schemaUriBase = schemaUriBase;
    }

    public String getSchemaPrefix() {
        return schemaPrefix;
    }

    public Namespace schemaPrefix(String schemaPrefix) {
        this.schemaPrefix = schemaPrefix;
        return this;
    }

    public void setSchemaPrefix(String schemaPrefix) {
        this.schemaPrefix = schemaPrefix;
    }

    public String getFeatureType() {
        return featureType;
    }

    public Namespace featureType(String featureType) {
        this.featureType = featureType;
        return this;
    }

    public void setFeatureType(String featureType) {
        this.featureType = featureType;
    }

    public String getGeometryProperty() {
        return geometryProperty;
    }

    public Namespace geometryProperty(String geometryProperty) {
        this.geometryProperty = geometryProperty;
        return this;
    }

    public void setGeometryProperty(String geometryProperty) {
        this.geometryProperty = geometryProperty;
    }

    public String getBeginLifespanVersionProperty() {
        return beginLifespanVersionProperty;
    }

    public Namespace beginLifespanVersionProperty(String beginLifespanVersionProperty) {
        this.beginLifespanVersionProperty = beginLifespanVersionProperty;
        return this;
    }

    public void setBeginLifespanVersionProperty(String beginLifespanVersionProperty) {
        this.beginLifespanVersionProperty = beginLifespanVersionProperty;
    }

    public Integer getFeaturesThreshold() {
        return featuresThreshold;
    }

    public Namespace featuresThreshold(Integer featuresThreshold) {
        this.featuresThreshold = featuresThreshold;
        return this;
    }

    public void setFeaturesThreshold(Integer featuresThreshold) {
        this.featuresThreshold = featuresThreshold;
    }

    public Boolean isResolverProxyMode() {
        return resolverProxyMode;
    }

    public Namespace resolverProxyMode(Boolean resolverProxyMode) {
        this.resolverProxyMode = resolverProxyMode;
        return this;
    }

    public void setResolverProxyMode(Boolean resolverProxyMode) {
        this.resolverProxyMode = resolverProxyMode;
    }

    public Boolean isHitsRequest() {
        return hitsRequest;
    }

    public Namespace hitsRequest(Boolean hitsRequest) {
        this.hitsRequest = hitsRequest;
        return this;
    }

    public void setHitsRequest(Boolean hitsRequest) {
        this.hitsRequest = hitsRequest;
    }

    public Integer getFactorK() {
        return factorK;
    }

    public Namespace factorK(Integer factorK) {
        this.factorK = factorK;
        return this;
    }

    public void setFactorK(Integer factorK) {
        this.factorK = factorK;
    }

    public String getXpath() {
        return xpath;
    }

    public Namespace xpath(String xpath) {
        this.xpath = xpath;
        return this;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    public String getNameItem() {
        return nameItem;
    }

    public Namespace nameItem(String nameItem) {
        this.nameItem = nameItem;
        return this;
    }

    public void setNameItem(String nameItem) {
        this.nameItem = nameItem;
    }

    public Integer getMaxNumRequest() {
        return maxNumRequest;
    }

    public Namespace maxNumRequest(Integer maxNumRequest) {
        this.maxNumRequest = maxNumRequest;
        return this;
    }

    public void setMaxNumRequest(Integer maxNumRequest) {
        this.maxNumRequest = maxNumRequest;
    }

    public Double getMinX() {
        return minX;
    }

    public Namespace minX(Double minX) {
        this.minX = minX;
        return this;
    }

    public void setMinX(Double minX) {
        this.minX = minX;
    }

    public Double getMinY() {
        return minY;
    }

    public Namespace minY(Double minY) {
        this.minY = minY;
        return this;
    }

    public void setMinY(Double minY) {
        this.minY = minY;
    }

    public Double getMaxX() {
        return maxX;
    }

    public Namespace maxX(Double maxX) {
        this.maxX = maxX;
        return this;
    }

    public void setMaxX(Double maxX) {
        this.maxX = maxX;
    }

    public Double getMaxY() {
        return maxY;
    }

    public Namespace maxY(Double maxY) {
        this.maxY = maxY;
        return this;
    }

    public void setMaxY(Double maxY) {
        this.maxY = maxY;
    }

    public Organization getOwner() {
        return owner;
    }

    public Namespace owner(Organization organization) {
        this.owner = organization;
        return this;
    }

    public void setOwner(Organization organization) {
        this.owner = organization;
    }
    // jhipster-needle-entity-add-getters-setters - Jhipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Namespace namespace = (Namespace) o;
        if (namespace.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), namespace.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Namespace{" +
            "id=" + getId() +
            ", namespace='" + getNamespace() + "'" +
            ", title='" + getTitle() + "'" +
            ", publicNamespace='" + isPublicNamespace() + "'" +
            ", renewalPolicy='" + getRenewalPolicy() + "'" +
            ", namespaceStatus='" + getNamespaceStatus() + "'" +
            ", processStatus='" + getProcessStatus() + "'" +
            ", itemStatus='" + getItemStatus() + "'" +
            ", lastChangeDate='" + getLastChangeDate() + "'" +
            ", registrationDate='" + getRegistrationDate() + "'" +
            ", lastRevisionDate='" + getLastRevisionDate() + "'" +
            ", nextRenewalDate='" + getNextRenewalDate() + "'" +
            ", annullationDate='" + getAnnullationDate() + "'" +
            ", methodType='" + getMethodType() + "'" +
            ", sourceType='" + getSourceType() + "'" +
            ", endpointLocation='" + getEndpointLocation() + "'" +
            ", srsName='" + getSrsName() + "'" +
            ", schemaUri='" + getSchemaUri() + "'" +
            ", schemaUriGML='" + getSchemaUriGML() + "'" +
            ", schemaUriBase='" + getSchemaUriBase() + "'" +
            ", schemaPrefix='" + getSchemaPrefix() + "'" +
            ", featureType='" + getFeatureType() + "'" +
            ", geometryProperty='" + getGeometryProperty() + "'" +
            ", beginLifespanVersionProperty='" + getBeginLifespanVersionProperty() + "'" +
            ", featuresThreshold='" + getFeaturesThreshold() + "'" +
            ", resolverProxyMode='" + isResolverProxyMode() + "'" +
            ", hitsRequest='" + isHitsRequest() + "'" +
            ", factorK='" + getFactorK() + "'" +
            ", xpath='" + getXpath() + "'" +
            ", nameItem='" + getNameItem() + "'" +
            ", maxNumRequest='" + getMaxNumRequest() + "'" +
            ", minX='" + getMinX() + "'" +
            ", minY='" + getMinY() + "'" +
            ", maxX='" + getMaxX() + "'" +
            ", maxY='" + getMaxY() + "'" +
            "}";
    }
}
