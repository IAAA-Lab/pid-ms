package es.unizar.iaaa.pid.service.dto;


import es.unizar.iaaa.pid.domain.enumeration.ChangeAction;
import es.unizar.iaaa.pid.domain.enumeration.ResourceType;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the Change entity.
 */
public class ChangeDTO implements Serializable {

    private Long id;

    private Instant changeTimestamp;

    private ChangeAction action;

    private Long featureId;

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

    private Long taskId;

    private String featureType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getChangeTimestamp() {
        return changeTimestamp;
    }

    public void setChangeTimestamp(Instant changeTimestamp) {
        this.changeTimestamp = changeTimestamp;
    }

    public ChangeAction getAction() {
        return action;
    }

    public void setAction(ChangeAction action) {
        this.action = action;
    }

    public Long getFeatureId() {
		return featureId;
	}

	public void setFeatureId(Long featureId) {
		this.featureId = featureId;
	}

    public String getFeatureType() {
        return featureType;
    }

    public void setFeatureType(String featureType) {
        this.featureType = featureType;
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

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ChangeDTO changeDTO = (ChangeDTO) o;
        return changeDTO.getId() != null && getId() != null && Objects.equals(getId(), changeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ChangeDTO{" +
            "id=" + getId() +
            ", changeTimestamp='" + getChangeTimestamp() + "'" +
            ", action='" + getAction() + "'" +
            ", feature='" + getFeatureId() + "'" +
            ", featureType='" + getFeatureType() + "'" +
            ", namespace='" + getNamespace() + "'" +
            ", localId='" + getLocalId() + "'" +
            ", versionId='" + getVersionId() + "'" +
            ", beginLifespanVersion='" + getBeginLifespanVersion() + "'" +
            ", endLifespanVersion='" + getEndLifespanVersion() + "'" +
            ", alternateId='" + getAlternateId() + "'" +
            ", resourceType='" + getResourceType() + "'" +
            ", locator='" + getLocator() + "'" +
            "}";
    }
}
