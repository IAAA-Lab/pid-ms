package es.unizar.iaaa.pid.service.dto;


import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import es.unizar.iaaa.pid.domain.enumeration.ProcessStatus;
import es.unizar.iaaa.pid.domain.enumeration.TaskStatus;

/**
 * A DTO for the Task entity.
 */
public class TaskDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant timestamp;

    @NotNull
    private ProcessStatus type;

    @NotNull
    private TaskStatus status;

    @Min(value = 0)
    private Integer numErrors;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public ProcessStatus getType() {
        return type;
    }

    public void setType(ProcessStatus type) {
        this.type = type;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public Integer getNumErrors() {
        return numErrors;
    }

    public void setNumErrors(Integer numErrors) {
        this.numErrors = numErrors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TaskDTO taskDTO = (TaskDTO) o;
        if(taskDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), taskDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TaskDTO{" +
            "id=" + getId() +
            ", timestamp='" + getTimestamp() + "'" +
            ", type='" + getType() + "'" +
            ", status='" + getStatus() + "'" +
            ", numErrors='" + getNumErrors() + "'" +
            "}";
    }
}
