package es.unizar.iaaa.pid.service.dto;


import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import es.unizar.iaaa.pid.domain.enumeration.ChangeAction;
import es.unizar.iaaa.pid.domain.enumeration.ResourceType;

/**
 * A DTO for the Change entity.
 */
public class ChangeDTO implements Serializable {

    private Long id;

    private Instant timestamp;

    private ChangeAction action;

    private String feature;

    @NotNull
    private String namespace;

    @NotNull
    private String localId;

    private String versionId;

    private Instant beginLifespanVersion;

    private Instant endLifespanVersion;

    private String alternateId;

    private ResourceType type;

    private String locator;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public ChangeAction getAction() {
        return action;
    }

    public void setAction(ChangeAction action) {
        this.action = action;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
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

    public ResourceType getType() {
        return type;
    }

    public void setType(ResourceType type) {
        this.type = type;
    }

    public String getLocator() {
        return locator;
    }

    public void setLocator(String locator) {
        this.locator = locator;
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
        if(changeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), changeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ChangeDTO{" +
            "id=" + getId() +
            ", timestamp='" + getTimestamp() + "'" +
            ", action='" + getAction() + "'" +
            ", feature='" + getFeature() + "'" +
            ", namespace='" + getNamespace() + "'" +
            ", localId='" + getLocalId() + "'" +
            ", versionId='" + getVersionId() + "'" +
            ", beginLifespanVersion='" + getBeginLifespanVersion() + "'" +
            ", endLifespanVersion='" + getEndLifespanVersion() + "'" +
            ", alternateId='" + getAlternateId() + "'" +
            ", type='" + getType() + "'" +
            ", locator='" + getLocator() + "'" +
            "}";
    }
}
