package es.unizar.iaaa.pid.service.mapper;

import es.unizar.iaaa.pid.domain.*;
import es.unizar.iaaa.pid.service.dto.TaskDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Task and its DTO TaskDTO.
 */
@Mapper(componentModel = "spring", uses = {NamespaceMapper.class, })
public interface TaskMapper extends EntityMapper <TaskDTO, Task> {

    @Mapping(source = "namespace.id", target = "namespaceId")
    TaskDTO toDto(Task task); 

    @Mapping(source = "namespaceId", target = "namespace")
    Task toEntity(TaskDTO taskDTO); 
    default Task fromId(Long id) {
        if (id == null) {
            return null;
        }
        Task task = new Task();
        task.setId(id);
        return task;
    }
}
