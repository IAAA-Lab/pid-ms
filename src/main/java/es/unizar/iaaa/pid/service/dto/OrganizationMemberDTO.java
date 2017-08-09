package es.unizar.iaaa.pid.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import es.unizar.iaaa.pid.domain.enumeration.Capacity;

/**
 * A DTO for the OrganizationMember entity.
 */
public class OrganizationMemberDTO implements Serializable {

    private Long id;

    @NotNull
    private Capacity capacity;

    private Long userId;

    private Long organizationId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Capacity getCapacity() {
        return capacity;
    }

    public void setCapacity(Capacity capacity) {
        this.capacity = capacity;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OrganizationMemberDTO organizationMemberDTO = (OrganizationMemberDTO) o;
        if(organizationMemberDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), organizationMemberDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OrganizationMemberDTO{" +
            "id=" + getId() +
            ", capacity='" + getCapacity() + "'" +
            "}";
    }
}
