package es.unizar.iaaa.pid.service.dto;


import es.unizar.iaaa.pid.domain.enumeration.ItemStatus;
import es.unizar.iaaa.pid.domain.enumeration.ProcessStatus;
import es.unizar.iaaa.pid.domain.enumeration.ResourceType;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the PersistentIdentifier entity.
 */
public class PersistentIdentifierDTO implements Serializable {

    private UUID id;

    private String externalUrn;

    @NotNull
    private Long featureId;

    private Boolean resolverProxyMode;

    @NotNull
    private String namespace;

    @NotNull
    private String localId;

    private String versionId;

    private Instant beginLifespanVersion;

    private Instant endLifespanVersion;

    private String alternateId;

    private ResourceType resourceType;

    private String locator;

    private ProcessStatus processStatus;

    private ItemStatus itemStatus;

    private Instant lastChangeDate;

    private Instant registrationDate;

    private Instant lastRevisionDate;

    private Instant nextRenewalDate;

    private Instant annullationDate;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getExternalUrn() {
        return externalUrn;
    }

    public void setExternalUrn(String externalUrn) {
        this.externalUrn = externalUrn;
    }

    public Long getFeatureId() {
		return featureId;
	}

	public void setFeatureId(Long featureId) {
		this.featureId = featureId;
	}

	public Boolean isResolverProxyMode() {
        return resolverProxyMode;
    }

    public void setResolverProxyMode(Boolean resolverProxyMode) {
        this.resolverProxyMode = resolverProxyMode;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getLocalId() {
        return localId;
    }

    public void setLocalId(String localId) {
        this.localId = localId;
    }

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public Instant getBeginLifespanVersion() {
        return beginLifespanVersion;
    }

    public void setBeginLifespanVersion(Instant beginLifespanVersion) {
        this.beginLifespanVersion = beginLifespanVersion;
    }

    public Instant getEndLifespanVersion() {
        return endLifespanVersion;
    }

    public void setEndLifespanVersion(Instant endLifespanVersion) {
        this.endLifespanVersion = endLifespanVersion;
    }

    public String getAlternateId() {
        return alternateId;
    }

    public void setAlternateId(String alternateId) {
        this.alternateId = alternateId;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public String getLocator() {
        return locator;
    }

    public void setLocator(String locator) {
        this.locator = locator;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PersistentIdentifierDTO persistentIdentifierDTO = (PersistentIdentifierDTO) o;
        if(persistentIdentifierDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), persistentIdentifierDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PersistentIdentifierDTO{" +
            "id=" + getId() +
            ", externalUrn='" + getExternalUrn() + "'" +
            ", feature='" + getFeatureId() + "'" +
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
