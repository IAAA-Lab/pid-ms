package es.unizar.iaaa.pid.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * An Organization is the owner of a set of namespaces.
 */
@ApiModel(description = "An Organization is the owner of a set of namespaces.")
@Entity
@Table(name = "organization")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Organization implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "title")
    private String title;

    @OneToMany(mappedBy = "organization")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<OrganizationMember> members = new HashSet<>();

    @OneToMany(mappedBy = "owner")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Namespace> namespaces = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Organization name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public Organization title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<OrganizationMember> getMembers() {
        return members;
    }

    public Organization members(Set<OrganizationMember> organizationMembers) {
        this.members = organizationMembers;
        return this;
    }

    public Organization addMember(OrganizationMember organizationMember) {
        this.members.add(organizationMember);
        organizationMember.setOrganization(this);
        return this;
    }

    public Organization removeMember(OrganizationMember organizationMember) {
        this.members.remove(organizationMember);
        organizationMember.setOrganization(null);
        return this;
    }

    public void setMembers(Set<OrganizationMember> organizationMembers) {
        this.members = organizationMembers;
    }

    public Set<Namespace> getNamespaces() {
        return namespaces;
    }

    public Organization namespaces(Set<Namespace> namespaces) {
        this.namespaces = namespaces;
        return this;
    }

    public Organization addNamespace(Namespace namespace) {
        this.namespaces.add(namespace);
        namespace.setOwner(this);
        return this;
    }

    public Organization removeNamespace(Namespace namespace) {
        this.namespaces.remove(namespace);
        namespace.setOwner(null);
        return this;
    }

    public void setNamespaces(Set<Namespace> namespaces) {
        this.namespaces = namespaces;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Organization organization = (Organization) o;
        if (organization.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), organization.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Organization{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", title='" + getTitle() + "'" +
            "}";
    }
}
