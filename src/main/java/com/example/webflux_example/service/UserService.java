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

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper mapper;

    public Flux<UserDTO> findAll() {
        log.info("Fetching all users");
        return userRepository.findAll()
                .map(mapper::toDto);
    }

    public Mono<UserDTO> finById(String id) {
        log.info(" User id: {}", id);
        return userRepository.findById(id)
                .map(mapper::toDto);
    }

    public Mono<UserDTO> createUser(UserDTO userDto) {
        log.info("User create");
        UserEntity user = mapper.toEntity(userDto);
        return userRepository.save(user)
                .map(mapper::toDto);
    }

    public Mono<UserDTO> updateUser(String id, UserDTO userDTO) {
        log.info("Update user with id: {}", id);
        return userRepository.findById(id)
                .flatMap(existingUser -> {
                    existingUser.setUsername(userDTO.getUsername());
                    existingUser.setEmail(userDTO.getEmail());
                    return userRepository.save(existingUser);
                })
                .map(mapper::toDto);
    }

    public Mono<Void> deleteUser(String id) {
        log.info("Delete: {}", id);
        return userRepository.deleteById(id);
    }
}