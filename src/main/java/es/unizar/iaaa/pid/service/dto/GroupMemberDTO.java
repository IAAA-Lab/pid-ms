package es.unizar.iaaa.pid.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import es.unizar.iaaa.pid.domain.enumeration.Capacity;

/**
 * A DTO for the GroupMember entity.
 */
public class GroupMemberDTO implements Serializable {

    private Long id;

    @NotNull
    private Capacity capacity;

    private Long userId;

    private Long groupId;

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

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GroupMemberDTO groupMemberDTO = (GroupMemberDTO) o;
        if(groupMemberDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), groupMemberDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "GroupMemberDTO{" +
            "id=" + getId() +
            ", capacity='" + getCapacity() + "'" +
            "}";
    }
}
