package es.unizar.iaaa.pid.domain;

import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import es.unizar.iaaa.pid.domain.enumeration.ChangeAction;

import es.unizar.iaaa.pid.domain.enumeration.ResourceType;

/**
 * Entity Change
 */
@ApiModel(description = "Entity Change")
@Entity
@Table(name = "change")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Change implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "change_timestamp")
    private Instant changeTimestamp;

    @Enumerated(EnumType.STRING)
    @Column(name = "action")
    private ChangeAction action;

    @Column(name = "feature")
    private String feature;

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

    @ManyToOne
    private Task task;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getChangeTimestamp() {
        return changeTimestamp;
    }

    public Change changeTimestamp(Instant changeTimestamp) {
        this.changeTimestamp = changeTimestamp;
        return this;
    }

    public void setChangeTimestamp(Instant changeTimestamp) {
        this.changeTimestamp = changeTimestamp;
    }

    public ChangeAction getAction() {
        return action;
    }

    public Change action(ChangeAction action) {
        this.action = action;
        return this;
    }

    public void setAction(ChangeAction action) {
        this.action = action;
    }

    public String getFeature() {
        return feature;
    }

    public Change feature(String feature) {
        this.feature = feature;
        return this;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getNamespace() {
        return namespace;
    }

    public Change namespace(String namespace) {
        this.namespace = namespace;
        return this;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getLocalId() {
        return localId;
    }

    public Change localId(String localId) {
        this.localId = localId;
        return this;
    }

    public void setLocalId(String localId) {
        this.localId = localId;
    }

    public String getVersionId() {
        return versionId;
    }

    public Change versionId(String versionId) {
        this.versionId = versionId;
        return this;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public Instant getBeginLifespanVersion() {
        return beginLifespanVersion;
    }

    public Change beginLifespanVersion(Instant beginLifespanVersion) {
        this.beginLifespanVersion = beginLifespanVersion;
        return this;
    }

    public void setBeginLifespanVersion(Instant beginLifespanVersion) {
        this.beginLifespanVersion = beginLifespanVersion;
    }

    public Instant getEndLifespanVersion() {
        return endLifespanVersion;
    }

    public Change endLifespanVersion(Instant endLifespanVersion) {
        this.endLifespanVersion = endLifespanVersion;
        return this;
    }

    public void setEndLifespanVersion(Instant endLifespanVersion) {
        this.endLifespanVersion = endLifespanVersion;
    }

    public String getAlternateId() {
        return alternateId;
    }

    public Change alternateId(String alternateId) {
        this.alternateId = alternateId;
        return this;
    }

    public void setAlternateId(String alternateId) {
        this.alternateId = alternateId;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public Change resourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
        return this;
    }

    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public String getLocator() {
        return locator;
    }

    public Change locator(String locator) {
        this.locator = locator;
        return this;
    }

    public void setLocator(String locator) {
        this.locator = locator;
    }

    public Task getTask() {
        return task;
    }

    public Change task(Task task) {
        this.task = task;
        return this;
    }

    public void setTask(Task task) {
        this.task = task;
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
        Change change = (Change) o;
        if (change.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), change.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Change{" +
            "id=" + getId() +
            ", changeTimestamp='" + getChangeTimestamp() + "'" +
            ", action='" + getAction() + "'" +
            ", feature='" + getFeature() + "'" +
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
