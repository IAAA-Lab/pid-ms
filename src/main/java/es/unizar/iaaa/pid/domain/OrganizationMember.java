package es.unizar.iaaa.pid.domain;

import es.unizar.iaaa.pid.domain.enumeration.Capacity;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * When a group member is created a user joins to a group with a role
 */
@ApiModel(description = "When a group member is created a user joins to a group with a role")
@Entity
@Table(name = "organization_member")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class OrganizationMember implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "capacity", nullable = false)
    private Capacity capacity;

    @ManyToOne
    private User user;

    @ManyToOne
    private Organization organization;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Capacity getCapacity() {
        return capacity;
    }

    public OrganizationMember capacity(Capacity capacity) {
        this.capacity = capacity;
        return this;
    }

    public void setCapacity(Capacity capacity) {
        this.capacity = capacity;
    }

    public User getUser() {
        return user;
    }

    public OrganizationMember user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Organization getOrganization() {
        return organization;
    }

    public OrganizationMember organization(Organization organization) {
        this.organization = organization;
        return this;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
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
        OrganizationMember organizationMember = (OrganizationMember) o;
        if (organizationMember.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), organizationMember.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OrganizationMember{" +
            "id=" + getId() +
            ", capacity='" + getCapacity() + "'" +
            ", user.id='" + (getUser() == null? "null" : getUser().getId()) + "'" +
            ", organization.id='" + (getOrganization() == null? "null" : getOrganization().getId()) + "'" +
            "}";
    }
}
