package es.unizar.iaaa.pid.domain;

import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import es.unizar.iaaa.pid.domain.enumeration.ResourceType;

import es.unizar.iaaa.pid.domain.enumeration.ProcessStatus;

import es.unizar.iaaa.pid.domain.enumeration.ItemStatus;

/**
 * Entity PersistentIdentifier
 */
@ApiModel(description = "Entity PersistentIdentifier")
@Entity
@Table(name = "persistent_identifier")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PersistentIdentifier implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "external_urn", nullable = false)
    private String externalUrn;

    @NotNull
    @Column(name = "feature", nullable = false)
    private String feature;

    @Column(name = "resolver_proxy_mode")
    private Boolean resolverProxyMode;

    @NotNull
    @Column(name = "namespace", nullable = false)
    private String namespace;

    @NotNull
    @Column(name = "local_id", nullable = false)
    private String localId;

    @Column(name = "version_id")
    private String versionId;

    @Column(name = "begin_lifespan_version")
    private Instant beginLifespanVersion;

    @Column(name = "end_lifespan_version")
    private Instant endLifespanVersion;

    @Column(name = "alternate_id")
    private String alternateId;

    @Enumerated(EnumType.STRING)
    @Column(name = "resource_type")
    private ResourceType resourceType;

    @Column(name = "locator")
    private String locator;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExternalUrn() {
        return externalUrn;
    }

    public PersistentIdentifier externalUrn(String externalUrn) {
        this.externalUrn = externalUrn;
        return this;
    }

    public void setExternalUrn(String externalUrn) {
        this.externalUrn = externalUrn;
    }

    public String getFeature() {
        return feature;
    }

    public PersistentIdentifier feature(String feature) {
        this.feature = feature;
        return this;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public Boolean isResolverProxyMode() {
        return resolverProxyMode;
    }

    public PersistentIdentifier resolverProxyMode(Boolean resolverProxyMode) {
        this.resolverProxyMode = resolverProxyMode;
        return this;
    }

    public void setResolverProxyMode(Boolean resolverProxyMode) {
        this.resolverProxyMode = resolverProxyMode;
    }

    public String getNamespace() {
        return namespace;
    }

    public PersistentIdentifier namespace(String namespace) {
        this.namespace = namespace;
        return this;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getLocalId() {
        return localId;
    }

    public PersistentIdentifier localId(String localId) {
        this.localId = localId;
        return this;
    }

    public void setLocalId(String localId) {
        this.localId = localId;
    }

    public String getVersionId() {
        return versionId;
    }

    public PersistentIdentifier versionId(String versionId) {
        this.versionId = versionId;
        return this;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public Instant getBeginLifespanVersion() {
        return beginLifespanVersion;
    }

    public PersistentIdentifier beginLifespanVersion(Instant beginLifespanVersion) {
        this.beginLifespanVersion = beginLifespanVersion;
        return this;
    }

    public void setBeginLifespanVersion(Instant beginLifespanVersion) {
        this.beginLifespanVersion = beginLifespanVersion;
    }

    public Instant getEndLifespanVersion() {
        return endLifespanVersion;
    }

    public PersistentIdentifier endLifespanVersion(Instant endLifespanVersion) {
        this.endLifespanVersion = endLifespanVersion;
        return this;
    }

    public void setEndLifespanVersion(Instant endLifespanVersion) {
        this.endLifespanVersion = endLifespanVersion;
    }

    public String getAlternateId() {
        return alternateId;
    }

    public PersistentIdentifier alternateId(String alternateId) {
        this.alternateId = alternateId;
        return this;
    }

    public void setAlternateId(String alternateId) {
        this.alternateId = alternateId;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public PersistentIdentifier resourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
        return this;
    }

    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public String getLocator() {
        return locator;
    }

    public PersistentIdentifier locator(String locator) {
        this.locator = locator;
        return this;
    }

    public void setLocator(String locator) {
        this.locator = locator;
    }

    public ProcessStatus getProcessStatus() {
        return processStatus;
    }

    public PersistentIdentifier processStatus(ProcessStatus processStatus) {
        this.processStatus = processStatus;
        return this;
    }

    public void setProcessStatus(ProcessStatus processStatus) {
        this.processStatus = processStatus;
    }

    public ItemStatus getItemStatus() {
        return itemStatus;
    }

    public PersistentIdentifier itemStatus(ItemStatus itemStatus) {
        this.itemStatus = itemStatus;
        return this;
    }

    public void setItemStatus(ItemStatus itemStatus) {
        this.itemStatus = itemStatus;
    }

    public Instant getLastChangeDate() {
        return lastChangeDate;
    }

    public PersistentIdentifier lastChangeDate(Instant lastChangeDate) {
        this.lastChangeDate = lastChangeDate;
        return this;
    }

    public void setLastChangeDate(Instant lastChangeDate) {
        this.lastChangeDate = lastChangeDate;
    }

    public Instant getRegistrationDate() {
        return registrationDate;
    }

    public PersistentIdentifier registrationDate(Instant registrationDate) {
        this.registrationDate = registrationDate;
        return this;
    }

    public void setRegistrationDate(Instant registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Instant getLastRevisionDate() {
        return lastRevisionDate;
    }

    public PersistentIdentifier lastRevisionDate(Instant lastRevisionDate) {
        this.lastRevisionDate = lastRevisionDate;
        return this;
    }

    public void setLastRevisionDate(Instant lastRevisionDate) {
        this.lastRevisionDate = lastRevisionDate;
    }

    public Instant getNextRenewalDate() {
        return nextRenewalDate;
    }

    public PersistentIdentifier nextRenewalDate(Instant nextRenewalDate) {
        this.nextRenewalDate = nextRenewalDate;
        return this;
    }

    public void setNextRenewalDate(Instant nextRenewalDate) {
        this.nextRenewalDate = nextRenewalDate;
    }

    public Instant getAnnullationDate() {
        return annullationDate;
    }

    public PersistentIdentifier annullationDate(Instant annullationDate) {
        this.annullationDate = annullationDate;
        return this;
    }

    public void setAnnullationDate(Instant annullationDate) {
        this.annullationDate = annullationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PersistentIdentifier persistentIdentifier = (PersistentIdentifier) o;
        if (persistentIdentifier.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), persistentIdentifier.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PersistentIdentifier{" +
            "id=" + getId() +
            ", externalUrn='" + getExternalUrn() + "'" +
            ", feature='" + getFeature() + "'" +
            ", resolverProxyMode='" + isResolverProxyMode() + "'" +
            ", namespace='" + getNamespace() + "'" +
            ", localId='" + getLocalId() + "'" +
            ", versionId='" + getVersionId() + "'" +
            ", beginLifespanVersion='" + getBeginLifespanVersion() + "'" +
            ", endLifespanVersion='" + getEndLifespanVersion() + "'" +
            ", alternateId='" + getAlternateId() + "'" +
            ", resourceType='" + getResourceType() + "'" +
            ", locator='" + getLocator() + "'" +
            ", processStatus='" + getProcessStatus() + "'" +
            ", itemStatus='" + getItemStatus() + "'" +
            ", lastChangeDate='" + getLastChangeDate() + "'" +
            ", registrationDate='" + getRegistrationDate() + "'" +
            ", lastRevisionDate='" + getLastRevisionDate() + "'" +
            ", nextRenewalDate='" + getNextRenewalDate() + "'" +
            ", annullationDate='" + getAnnullationDate() + "'" +
            "}";
    }
}
