package es.unizar.iaaa.pid.domain;

import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import es.unizar.iaaa.pid.domain.enumeration.Capacity;

/**
 * When a group member is created a user joins to a group with a role
 */
@ApiModel(description = "When a group member is created a user joins to a group with a role")
@Entity
@Table(name = "group_member")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class GroupMember implements Serializable {

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
    private Group group;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Capacity getCapacity() {
        return capacity;
    }

    public GroupMember capacity(Capacity capacity) {
        this.capacity = capacity;
        return this;
    }

    public void setCapacity(Capacity capacity) {
        this.capacity = capacity;
    }

    public User getUser() {
        return user;
    }

    public GroupMember user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Group getGroup() {
        return group;
    }

    public GroupMember group(Group group) {
        this.group = group;
        return this;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GroupMember groupMember = (GroupMember) o;
        if (groupMember.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), groupMember.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "GroupMember{" +
            "id=" + getId() +
            ", capacity='" + getCapacity() + "'" +
            "}";
    }
}
