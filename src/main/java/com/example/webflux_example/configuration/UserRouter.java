package com.example.webflux_example.configuration;

import com.example.webflux_example.handler.UserHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@Slf4j
public class UserRouter {

    @Bean
    public RouterFunction<ServerResponse> customUserRouter(UserHandler userHandler) {
        return RouterFunctions.route()
                .GET("/api/users", userHandler::getAllUsers)
                .GET("/api/users/{id}", userHandler::findUserById)
                .POST("/api/users", userHandler::createUser)
                .PUT("/api/users/{id}", userHandler::updateUser)
                .DELETE("/api/users/{id}", userHandler::deleteUser)
                .build();
    }
}