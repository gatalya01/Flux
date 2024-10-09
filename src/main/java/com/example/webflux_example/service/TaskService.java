package com.example.webflux_example.service;

import com.example.webflux_example.entity.TaskEntity;
import com.example.webflux_example.entity.UserEntity;
import com.example.webflux_example.mapper.TaskMapper;
import com.example.webflux_example.mapper.UserMapper;
import com.example.webflux_example.model.TaskDTO;
import com.example.webflux_example.publisher.TaskUpdatePublisher;
import com.example.webflux_example.repository.TaskRepository;
import com.example.webflux_example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.HashSet;

@Service
public class TaskService {
    @Autowired
    private TaskUpdatePublisher taskUpdatePublisher;
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
                .flatMap(this::enrichTaskWithUsers);
    }

    public Mono<TaskDTO> findTaskById(String id) {
        return taskRepository.findById(id)
                .flatMap(this::enrichTaskWithUsers);
    }

    private Mono<TaskDTO> enrichTaskWithUsers(TaskEntity task) {
        Mono<UserEntity> authorMono = userRepository.findById(task.getAuthorId());
        Mono<UserEntity> assigneeMono = userRepository.findById(task.getAssigneeId());
        Flux<UserEntity> observersFlux = userRepository.findAllById(task.getObserverIds());

        return Mono.zip(authorMono, assigneeMono, observersFlux.collectList())
                .map(tuple -> {
                    TaskDTO taskDto = taskMapper.toDto(task); // Map Task to TaskDTO
                    taskDto.setAuthor(tuple.getT1() != null ? userMapper.toDto(tuple.getT1()) : null);
                    taskDto.setAssignee(tuple.getT2() != null ? userMapper.toDto(tuple.getT2()) : null);
                    taskDto.setObservers(new HashSet<>(tuple.getT3().stream()
                            .map(userMapper::toDto)
                            .toList()));
                    return taskDto;
                });
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
                .doOnSuccess(updatedTask -> {
                    taskUpdatePublisher.publish(taskMapper.toDto(updatedTask));
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
    }
}