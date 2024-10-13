package com.example.webflux_example.mapper;

import com.example.webflux_example.entity.UserEntity;
import com.example.webflux_example.model.UserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    default UserEntity toEntity(UserDTO userDto) {
        if (userDto == null) {
            return null;
        }
        UserEntity entity = new UserEntity();
        entity.setId(userDto.getId());
        entity.setUsername(userDto.getUsername());
        entity.setEmail(userDto.getEmail());
        return entity;
    }

    default UserDTO toDTO(UserEntity userEntity) {
        if (userEntity == null) {
            return null;
        }
        UserDTO dto = new UserDTO();
        dto.setId(userEntity.getId());
        dto.setUsername(userEntity.getUsername());
        dto.setEmail(userEntity.getEmail());
        return dto;
    }
}