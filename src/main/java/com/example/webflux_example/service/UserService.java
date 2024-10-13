package com.example.webflux_example.service;

import com.example.webflux_example.entity.UserEntity;
import com.example.webflux_example.mapper.UserMapper;
import com.example.webflux_example.model.UserDTO;
import com.example.webflux_example.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper mapper;

    public Flux<UserDTO> findAll() {
        log.info("Fetching all users");
        return userRepository.findAll()
                .map(mapper::toDTO);
    }

    public Mono<UserDTO> finById(String id) {
        log.info(" User id: {}", id);
        return userRepository.findById(id)
                .map(mapper::toDTO);
    }

    public Mono<UserDTO> createUser(UserDTO userDto) {
        log.info("User create");
        UserEntity user = mapper.toEntity(userDto); // Здесь должен быть создан UserEntity с заполненными полями
        return userRepository.save(user)
                .map(mapper::toDTO); // Убедитесь, что mapper правильно отображает UserEntity на UserDTO
    }

    public Mono<UserDTO> updateUser(String id, UserDTO userDTO) {
        log.info("Update user with id: {}", id);
        return userRepository.findById(id)
                .flatMap(existingUser -> {
                    existingUser.setUsername(userDTO.getUsername());
                    existingUser.setEmail(userDTO.getEmail());
                    return userRepository.save(existingUser);
                })
                .map(mapper::toDTO);
    }

    public Mono<Void> deleteUser(String id) {
        log.info("Delete: {}", id);
        return userRepository.deleteById(id);
    }
}