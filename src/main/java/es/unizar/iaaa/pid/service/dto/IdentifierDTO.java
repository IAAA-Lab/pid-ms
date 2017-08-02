package es.unizar.iaaa.pid.service.dto;


import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Identifier entity.
 */
public class IdentifierDTO implements Serializable {

    private Long id;

    @NotNull
    private String namespace;

    @NotNull
    private String localId;

    private String versionId;

    private Instant beginLifespanVersion;

    private Instant endLifespanVersion;

    private String alternateId;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        IdentifierDTO identifierDTO = (IdentifierDTO) o;
        if(identifierDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), identifierDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "IdentifierDTO{" +
            "id=" + getId() +
            ", namespace='" + getNamespace() + "'" +
            ", localId='" + getLocalId() + "'" +
            ", versionId='" + getVersionId() + "'" +
            ", beginLifespanVersion='" + getBeginLifespanVersion() + "'" +
            ", endLifespanVersion='" + getEndLifespanVersion() + "'" +
            ", alternateId='" + getAlternateId() + "'" +
            "}";
    }
}
