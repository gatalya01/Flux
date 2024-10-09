package com.example.webflux_example.configuration;


import com.example.webflux_example.handler.TaskHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@Slf4j
public class TaskRouter {

    @Bean
    public RouterFunction<ServerResponse> customTaskRouter(TaskHandler taskHandler) {
        return RouterFunctions.route()
                .GET("/api/tasks", taskHandler::getAllTasks)
                .GET("/api/tasks/{id}", taskHandler::findTaskById)
                .POST("/api/tasks", taskHandler::createTask)
                .PUT("/api/tasks/{id}", taskHandler::updateTask)
                .PATCH("/api/tasks/{taskId}/observers/{observerId}", taskHandler::addObserver)
                .DELETE("/api/tasks/{id}", taskHandler::deleteTask)
                .build();
    }
}