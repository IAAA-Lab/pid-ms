package es.unizar.iaaa.pid.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;

@Embeddable
public class Identifier implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String DEFAULT_NAMESPACE = "catalogo";

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

    // Was gmlid
    @Column(name = "alternate_id")
    private String alternateId;

    public String getNamespace() {
        return namespace;
    }

    public Identifier namespace(String namespace) {
        this.namespace = namespace;
        return this;
    }

    public Identifier defaultNamespace() {
        this.namespace = DEFAULT_NAMESPACE;
        return this;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getLocalId() {
        return localId;
    }

    public Identifier localId(String localId) {
        this.localId = localId;
        return this;
    }

    public void setLocalId(String localId) {
        this.localId = localId;
    }

    public String getVersionId() {
        return versionId;
    }

    public Identifier versionId(String versionId) {
        this.versionId = versionId;
        return this;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public Instant getBeginLifespanVersion() {
        return beginLifespanVersion;
    }

    public Identifier beginLifespanVersion(Instant beginLifespanVersion) {
        this.beginLifespanVersion = beginLifespanVersion;
        return this;
    }

    public void setBeginLifespanVersion(Instant beginLifespanVersion) {
        this.beginLifespanVersion = beginLifespanVersion;
    }

    public Instant getEndLifespanVersion() {
        return endLifespanVersion;
    }

    public Identifier endLifespanVersion(Instant endLifespanVersion) {
        this.endLifespanVersion = endLifespanVersion;
        return this;
    }

    public void setEndLifespanVersion(Instant endLifespanVersion) {
        this.endLifespanVersion = endLifespanVersion;
    }

    public String getAlternateId() {
        return alternateId;
    }

    public Identifier alternateId(String alternateId) {
        this.alternateId = alternateId;
        return this;
    }

    public void setAlternateId(String alternateId) {
        this.alternateId = alternateId;
    }

    @Override
    public String toString() {
        return "Identifier{" +
            "namespace=" + getNamespace() +
            ", localId='" + getLocalId() + "'" +
            ", versionId='" + getVersionId() + "'" +
            ", beginLifespanVersion='" + getBeginLifespanVersion() + "'" +
            ", endLifespanVersion='" + getEndLifespanVersion() + "'" +
            ", alternateId='" + getAlternateId() + "'" +
            "}";
    }

    public boolean sameAs(Identifier remoteIdentifier) {
        if (remoteIdentifier == null)
            return false;
        if (!namespace.equals(remoteIdentifier.namespace))
            return false;
        if (!localId.equals(remoteIdentifier.localId))
            return false;
        if (versionId != null)
            return versionId.equals(remoteIdentifier.versionId);
        return remoteIdentifier.versionId == null;
    }
}
