package com.example.webflux_example.handler;

import com.example.webflux_example.model.TaskDTO;
import com.example.webflux_example.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Component
public class TaskHandler {

    @Autowired
    private TaskService taskService;

    public Mono<ServerResponse> getAllTasks(ServerRequest request) {
        Flux<TaskDTO> tasks = taskService.findAllTasks();
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(tasks, TaskDTO.class);
    }

    public Mono<ServerResponse> findTaskById(ServerRequest request) {
        String taskId = request.pathVariable("id");
        return taskService.findTaskById(taskId)
                .flatMap(task -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(task))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> createTask(ServerRequest request) {
        Mono<TaskDTO> taskDtoMono = request.bodyToMono(TaskDTO.class);
        return taskDtoMono.flatMap(taskDTO ->
                taskService.createTask(taskDTO)
                        .flatMap(task -> ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(task)));
    }

    public Mono<ServerResponse> updateTask(ServerRequest request) {
        String taskId = request.pathVariable("id");
        Mono<TaskDTO> taskDtoMono = request.bodyToMono(TaskDTO.class);
        return taskDtoMono.flatMap(taskDTO ->
                taskService.updateTask(taskId, taskDTO)
                        .flatMap(task -> ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(task))
                        .switchIfEmpty(ServerResponse.notFound().build()));
    }

    public Mono<ServerResponse> addObserver(ServerRequest request) {
        String taskId = request.pathVariable("taskId");
        String observerId = request.pathVariable("observerId");
        return taskService.addObserver(taskId, observerId)
                .flatMap(task -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(task))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> deleteTask(ServerRequest request) {
        String taskId = request.pathVariable("id");
        return taskService.deleteTaskById(taskId)
                .then(ServerResponse.ok().<Void>build())
                .onErrorReturn((ServerResponse) ResponseEntity.notFound().build());
    }
}