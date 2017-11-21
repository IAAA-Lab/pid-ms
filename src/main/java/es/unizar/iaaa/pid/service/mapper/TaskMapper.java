package es.unizar.iaaa.pid.service.mapper;

import com.google.common.collect.ImmutableMap;
import es.unizar.iaaa.pid.domain.*;
import es.unizar.iaaa.pid.service.dto.TaskDTO;

import org.mapstruct.*;

import java.util.Map;

/**
 * Mapper for the entity Task and its DTO TaskDTO.
 */
@Mapper(componentModel = "spring", uses = {NamespaceMapper.class, })
public interface TaskMapper extends EntityMapper <TaskDTO, Task> {

    @Mapping(source = "namespace.id", target = "namespaceId")
    @Mapping(source = "namespace.namespace", target = "namespaceName")
    TaskDTO toDto(Task task);

    @Mapping(source = "namespaceId", target = "namespace.id")
    Task toEntity(TaskDTO taskDTO);

    default Task fromId(Long id) {
        if (id == null) {
            return null;
        }
        Task task = new Task();
        task.setId(id);
        return task;
    }

    Map<String, String> conversions = ImmutableMap.<String, String>builder().
        put("namespaceName", "namespace.namespace").
        put("namespaceId","namespace.id").
        build();

    @Override
    default Map<String, String> getConversions() {
        return conversions;
    }
}
