package com.example.webflux_example.controller;


import com.example.webflux_example.model.TaskDTO;
import com.example.webflux_example.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/tasks")
@Validated
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<TaskDTO> getAllTasks() {
        return taskService.findAllTasks();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<TaskDTO>> getTaskById(@PathVariable String id) {
        return taskService.findTaskById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<TaskDTO> createTask(@Valid @RequestBody TaskDTO taskDTO) { // Использование валидации
        return taskService.createTask(taskDTO);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<TaskDTO>> updateTask(@PathVariable String id, @Valid @RequestBody TaskDTO taskDto) {
        return taskService.updateTask(id, taskDto)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PatchMapping(value = "/{taskId}/observers/{observerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<TaskDTO>> addObserver(@PathVariable String taskId, @PathVariable String observerId) {
        return taskService.addObserver(taskId, observerId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteTask(@PathVariable String id) {
        return taskService.deleteTaskById(id)
                .then(Mono.just(ResponseEntity.ok().<Void>build()))
                .onErrorReturn(ResponseEntity.notFound().build());
    }
}
