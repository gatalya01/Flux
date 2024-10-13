package com.example.webflux_example.controller;

import com.example.webflux_example.model.TaskDTO;
import com.example.webflux_example.service.TaskService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/tasks")
@Validated
@Slf4j
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<TaskDTO> getAllTasks() {
        return taskService.findAllTasks()
                .doOnError(e -> log.error("Error fetching all tasks", e))
                .onErrorResume(e -> Flux.empty());
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<TaskDTO>> getTaskById(@PathVariable String id) {
        return taskService.findTaskById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .doOnError(e -> log.error("Error fetching task with id: {}", id, e));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<TaskDTO> createTask(@Valid @RequestBody TaskDTO taskDTO) {
        return taskService.createTask(taskDTO);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<TaskDTO>> updateTask(@PathVariable String id, @Valid @RequestBody TaskDTO taskDto) {
        return taskService.updateTask(id, taskDto)
                .map(updatedTask -> ResponseEntity.ok(updatedTask))
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .doOnError(e -> log.error("Error updating task with id: {}", id, e))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()));
    }

    @PatchMapping(value = "/{taskId}/observers/{observerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<TaskDTO>> addObserver(@PathVariable String taskId, @PathVariable String observerId) {
        return taskService.addObserver(taskId, observerId)
                .map(updatedTask -> ResponseEntity.ok(updatedTask))
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .doOnError(e -> log.error("Error adding observer to task with id: {}", taskId, e))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteTask(@PathVariable String id) {
        return taskService.deleteTaskById(id)
                .then(Mono.just(ResponseEntity.ok().<Void>build()))
                .onErrorResume(e -> {
                    log.error("Error deleting task with id: {}", id, e); // Log errors
                    return Mono.just(ResponseEntity.notFound().build());
                });
    }
}