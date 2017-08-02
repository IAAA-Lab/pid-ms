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
 * A Group is he owner of a set of namespaces.
 */
@ApiModel(description = "A Group is he owner of a set of namespaces.")
@Entity
@Table(name = "jhi_group")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Group implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "jhi_organization")
    private Boolean organization;

    @OneToMany(mappedBy = "group")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<GroupMember> members = new HashSet<>();

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

    public Group name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isOrganization() {
        return organization;
    }

    public Group organization(Boolean organization) {
        this.organization = organization;
        return this;
    }

    public void setOrganization(Boolean organization) {
        this.organization = organization;
    }

    public Set<GroupMember> getMembers() {
        return members;
    }

    public Group members(Set<GroupMember> groupMembers) {
        this.members = groupMembers;
        return this;
    }

    public Group addMember(GroupMember groupMember) {
        this.members.add(groupMember);
        groupMember.setGroup(this);
        return this;
    }

    public Group removeMember(GroupMember groupMember) {
        this.members.remove(groupMember);
        groupMember.setGroup(null);
        return this;
    }

    public void setMembers(Set<GroupMember> groupMembers) {
        this.members = groupMembers;
    }

    public Set<Namespace> getNamespaces() {
        return namespaces;
    }

    public Group namespaces(Set<Namespace> namespaces) {
        this.namespaces = namespaces;
        return this;
    }

    public Group addNamespace(Namespace namespace) {
        this.namespaces.add(namespace);
        namespace.setOwner(this);
        return this;
    }

    public Group removeNamespace(Namespace namespace) {
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
        Group group = (Group) o;
        if (group.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), group.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Group{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", organization='" + isOrganization() + "'" +
            "}";
    }
}
