package com.example.webflux_example.mapper;

import com.example.webflux_example.entity.TaskEntity;
import com.example.webflux_example.model.TaskDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    default TaskEntity toEntity(TaskDTO taskDto) {
        if (taskDto == null) {
            return null;
        }
        TaskEntity entity = new TaskEntity();
        entity.setId(taskDto.getId());
        entity.setName(taskDto.getName());
        entity.setDescription(taskDto.getDescription());
        entity.setCreatedAt(taskDto.getCreatedAt());
        entity.setUpdatedAt(taskDto.getUpdatedAt());
        entity.setStatus(taskDto.getStatus());
        entity.setAuthorId(taskDto.getAuthorId());
        entity.setAssigneeId(taskDto.getAssigneeId());

        entity.setObserverIds(taskDto.getObserverIds());

        return entity;
    }

    default TaskDTO toDto(TaskEntity taskEntity) {
        if (taskEntity == null) {
            return null;
        }
        TaskDTO dto = new TaskDTO();
        dto.setId(taskEntity.getId());
        dto.setName(taskEntity.getName());
        dto.setDescription(taskEntity.getDescription());
        dto.setCreatedAt(taskEntity.getCreatedAt());
        dto.setUpdatedAt(taskEntity.getUpdatedAt());
        dto.setStatus(taskEntity.getStatus());
        dto.setAuthorId(taskEntity.getAuthorId());
        dto.setAssigneeId(taskEntity.getAssigneeId());

        dto.setObserverIds(taskEntity.getObserverIds());

        return dto;
    }
}