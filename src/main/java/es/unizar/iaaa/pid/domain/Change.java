package es.unizar.iaaa.pid.domain;

import es.unizar.iaaa.pid.domain.enumeration.ChangeAction;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

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

    @ManyToOne
    private Feature feature;

    @Embedded
    private Identifier identifier;

    @Embedded
    private Resource resource;

    @ManyToOne
    private Task task;

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

    public Feature getFeature() {
        return feature;
    }

    public Change feature(Feature feature) {
        this.feature = feature;
        return this;
    }

    public void setFeature(Feature feature) {
        this.feature = feature;
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public Change identifier(Identifier identifier) {
        this.identifier = identifier;
        return this;
    }

    public void setIdentifier(Identifier identifier) {
        this.identifier = identifier;
    }

    public Resource getResource() {
        return resource;
    }

    public Change resource(Resource resource) {
        this.resource = resource;
        return this;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
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
            ", identifier='" + getIdentifier() + "'" +
            ", resource='" + getResource() + "'" +
            "}";
    }
}
