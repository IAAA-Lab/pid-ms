package es.unizar.iaaa.pid.service.dto;


import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import es.unizar.iaaa.pid.domain.enumeration.RenewalPolicy;
import es.unizar.iaaa.pid.domain.enumeration.NamespaceStatus;
import es.unizar.iaaa.pid.domain.enumeration.ProcessStatus;
import es.unizar.iaaa.pid.domain.enumeration.ItemStatus;
import es.unizar.iaaa.pid.domain.enumeration.MethodType;
import es.unizar.iaaa.pid.domain.enumeration.SourceType;

/**
 * A DTO for the Namespace entity.
 */
public class NamespaceDTO implements Serializable {

    private Long id;

    @NotNull
    private String namespace;

    private String title;

    @NotNull
    private Boolean publicNamespace;

    @NotNull
    private RenewalPolicy renewalPolicy;

    private NamespaceStatus namespaceStatus;

    private ProcessStatus processStatus;

    private ItemStatus itemStatus;

    private Instant lastChangeDate;

    private Instant registrationDate;

    private Instant lastRevisionDate;

    private Instant nextRenewalDate;

    private Instant annullationDate;

    private MethodType methodType;

    private SourceType sourceType;

    private String endpointLocation;

    private String srsName;

    private String schemaUri;

    private String schemaUriGML;

    private String schemaUriBase;

    private String schemaPrefix;

    private String featureType;

    private String geometryProperty;

    private String beginLifespanVersionProperty;

    private Integer featuresThreshold;

    private Boolean resolverProxyMode;

    private Boolean hitsRequest;

    private Integer factorK;

    private String xpath;

    private String nameItem;

    @Min(value = 0)
    private Integer maxNumRequest;

    private Double minX;

    private Double minY;

    private Double maxX;

    private Double maxY;

    private Long ownerId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean isPublicNamespace() {
        return publicNamespace;
    }

    public void setPublicNamespace(Boolean publicNamespace) {
        this.publicNamespace = publicNamespace;
    }

    public RenewalPolicy getRenewalPolicy() {
        return renewalPolicy;
    }

    public void setRenewalPolicy(RenewalPolicy renewalPolicy) {
        this.renewalPolicy = renewalPolicy;
    }

    public NamespaceStatus getNamespaceStatus() {
        return namespaceStatus;
    }

    public void setNamespaceStatus(NamespaceStatus namespaceStatus) {
        this.namespaceStatus = namespaceStatus;
    }

    public ProcessStatus getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(ProcessStatus processStatus) {
        this.processStatus = processStatus;
    }

    public ItemStatus getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(ItemStatus itemStatus) {
        this.itemStatus = itemStatus;
    }

    public Instant getLastChangeDate() {
        return lastChangeDate;
    }

    public void setLastChangeDate(Instant lastChangeDate) {
        this.lastChangeDate = lastChangeDate;
    }

    public Instant getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Instant registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Instant getLastRevisionDate() {
        return lastRevisionDate;
    }

    public void setLastRevisionDate(Instant lastRevisionDate) {
        this.lastRevisionDate = lastRevisionDate;
    }

    public Instant getNextRenewalDate() {
        return nextRenewalDate;
    }

    public void setNextRenewalDate(Instant nextRenewalDate) {
        this.nextRenewalDate = nextRenewalDate;
    }

    public Instant getAnnullationDate() {
        return annullationDate;
    }

    public void setAnnullationDate(Instant annullationDate) {
        this.annullationDate = annullationDate;
    }

    public MethodType getMethodType() {
        return methodType;
    }

    public void setMethodType(MethodType methodType) {
        this.methodType = methodType;
    }

    public SourceType getSourceType() {
        return sourceType;
    }

    public void setSourceType(SourceType sourceType) {
        this.sourceType = sourceType;
    }

    public String getEndpointLocation() {
        return endpointLocation;
    }

    public void setEndpointLocation(String endpointLocation) {
        this.endpointLocation = endpointLocation;
    }

    public String getSrsName() {
        return srsName;
    }

    public void setSrsName(String srsName) {
        this.srsName = srsName;
    }

    public String getSchemaUri() {
        return schemaUri;
    }

    public void setSchemaUri(String schemaUri) {
        this.schemaUri = schemaUri;
    }

    public String getSchemaUriGML() {
        return schemaUriGML;
    }

    public void setSchemaUriGML(String schemaUriGML) {
        this.schemaUriGML = schemaUriGML;
    }

    public String getSchemaUriBase() {
        return schemaUriBase;
    }

    public void setSchemaUriBase(String schemaUriBase) {
        this.schemaUriBase = schemaUriBase;
    }

    public String getSchemaPrefix() {
        return schemaPrefix;
    }

    public void setSchemaPrefix(String schemaPrefix) {
        this.schemaPrefix = schemaPrefix;
    }

    public String getFeatureType() {
        return featureType;
    }

    public void setFeatureType(String featureType) {
        this.featureType = featureType;
    }

    public String getGeometryProperty() {
        return geometryProperty;
    }

    public void setGeometryProperty(String geometryProperty) {
        this.geometryProperty = geometryProperty;
    }

    public String getBeginLifespanVersionProperty() {
        return beginLifespanVersionProperty;
    }

    public void setBeginLifespanVersionProperty(String beginLifespanVersionProperty) {
        this.beginLifespanVersionProperty = beginLifespanVersionProperty;
    }

    public Integer getFeaturesThreshold() {
        return featuresThreshold;
    }

    public void setFeaturesThreshold(Integer featuresThreshold) {
        this.featuresThreshold = featuresThreshold;
    }

    public Boolean isResolverProxyMode() {
        return resolverProxyMode;
    }

    public void setResolverProxyMode(Boolean resolverProxyMode) {
        this.resolverProxyMode = resolverProxyMode;
    }

    public Boolean isHitsRequest() {
        return hitsRequest;
    }

    public void setHitsRequest(Boolean hitsRequest) {
        this.hitsRequest = hitsRequest;
    }

    public Integer getFactorK() {
        return factorK;
    }

    public void setFactorK(Integer factorK) {
        this.factorK = factorK;
    }

    public String getXpath() {
        return xpath;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    public String getNameItem() {
        return nameItem;
    }

    public void setNameItem(String nameItem) {
        this.nameItem = nameItem;
    }

    public Integer getMaxNumRequest() {
        return maxNumRequest;
    }

    public void setMaxNumRequest(Integer maxNumRequest) {
        this.maxNumRequest = maxNumRequest;
    }

    public Double getMinX() {
        return minX;
    }

    public void setMinX(Double minX) {
        this.minX = minX;
    }

    public Double getMinY() {
        return minY;
    }

    public void setMinY(Double minY) {
        this.minY = minY;
    }

    public Double getMaxX() {
        return maxX;
    }

    public void setMaxX(Double maxX) {
        this.maxX = maxX;
    }

    public Double getMaxY() {
        return maxY;
    }

    public void setMaxY(Double maxY) {
        this.maxY = maxY;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long organizationId) {
        this.ownerId = organizationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        NamespaceDTO namespaceDTO = (NamespaceDTO) o;
        if(namespaceDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), namespaceDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "NamespaceDTO{" +
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
