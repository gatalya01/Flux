package com.example.webflux_example.mapper;

import com.example.webflux_example.entity.TaskEntity;
import com.example.webflux_example.model.TaskDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface TaskMapper {

    TaskDTO toDto(TaskEntity task);


    TaskEntity toEntity(TaskDTO taskDto);
}