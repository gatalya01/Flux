package com.example.webflux_example.service;

import com.example.webflux_example.entity.TaskEntity;
import com.example.webflux_example.entity.UserEntity;
import com.example.webflux_example.mapper.TaskMapper;
import com.example.webflux_example.mapper.UserMapper;
import com.example.webflux_example.model.TaskDTO;
import com.example.webflux_example.publisher.TaskUpdatePublisher;
import com.example.webflux_example.repository.TaskRepository;
import com.example.webflux_example.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.HashSet;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TaskMapper taskMapper;

    public Flux<TaskDTO> findAllTasks() {
        return taskRepository.findAll()
                .map(taskMapper::toDto);
    }

    public Mono<TaskDTO> findTaskById(String id) {
        return taskRepository.findById(id)
                .map(taskMapper::toDto);
    }

    private Mono<TaskDTO> enrichTaskWithUsers(TaskEntity task) {
        Mono<UserEntity> authorMono = userRepository.findById(task.getAuthorId())
                .doOnError(error -> log.error("Error fetching author", error));
        Mono<UserEntity> assigneeMono = userRepository.findById(task.getAssigneeId())
                .doOnError(error -> log.error("Error fetching assignee", error));
        Flux<UserEntity> observersFlux = userRepository.findAllById(task.getObserverIds())
                .doOnError(error -> log.error("Error fetching observers", error));

        return Mono.zip(authorMono, assigneeMono, observersFlux.collectList())
                .map(tuple -> {
                    TaskDTO taskDto = taskMapper.toDto(task);
                    taskDto.setAuthor(tuple.getT1() != null ? userMapper.toDTO(tuple.getT1()) : null);
                    taskDto.setAssignee(tuple.getT2() != null ? userMapper.toDTO(tuple.getT2()) : null);
                    taskDto.setObservers(new HashSet<>(tuple.getT3().stream()
                            .map(userMapper::toDTO)
                            .toList()));
                    return taskDto;
                })
                .doOnError(error -> log.error("Error enriching task with users", error));
    }

    public Mono<TaskDTO> createTask(TaskDTO taskDTO) {
        TaskEntity task = taskMapper.toEntity(taskDTO);
        task.setCreatedAt(Instant.now());
        task.setUpdatedAt(Instant.now());
        return taskRepository.save(task)
                .map(taskMapper::toDto);
    }

    public Mono<TaskDTO> updateTask(String id, TaskDTO taskDto) {
        return taskRepository.findById(id)
                .flatMap(existingTask -> {
                    existingTask.setName(taskDto.getName());
                    existingTask.setDescription(taskDto.getDescription());
                    existingTask.setStatus(taskDto.getStatus());
                    existingTask.setAssigneeId(taskDto.getAssigneeId());
                    existingTask.setObserverIds(taskDto.getObserverIds());
                    existingTask.setUpdatedAt(Instant.now());
                    return taskRepository.save(existingTask);
                })
                .map(taskMapper::toDto);
    }

    public Mono<TaskDTO> addObserver(String taskId, String observerId) {
        return taskRepository.findById(taskId)
                .flatMap(task -> {
                    task.getObserverIds().add(observerId);
                    return taskRepository.save(task);
                })
                .map(taskMapper::toDto);
    }

    public Mono<Void> deleteTaskById(String id) {
        return taskRepository.deleteById(id);
    }}