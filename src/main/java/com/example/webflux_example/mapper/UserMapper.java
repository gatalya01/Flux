package com.example.webflux_example.mapper;

import com.example.webflux_example.entity.UserEntity;
import com.example.webflux_example.model.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {


    UserDTO toDto(UserEntity user);

    UserEntity toEntity(UserDTO userDto);
}