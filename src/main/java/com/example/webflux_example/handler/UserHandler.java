package com.example.webflux_example.handler;
import com.example.webflux_example.model.UserDTO;
import com.example.webflux_example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
public class UserHandler {

    @Autowired
    private UserService userService;

    public Mono<ServerResponse> getAllUsers(ServerRequest request) {
        Flux<UserDTO> users = userService.findAll();
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(users, UserDTO.class);
    }

    public Mono<ServerResponse> findUserById(ServerRequest request) {
        String userId = request.pathVariable("id");
        return userService.finById(userId)
                .flatMap(user -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(user))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> createUser(ServerRequest request) {
        Mono<UserDTO> userDtoMono = request.bodyToMono(UserDTO.class);
        return userDtoMono.flatMap(userDTO ->
                userService.createUser(userDTO)
                        .flatMap(user -> ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(user)));
    }

    public Mono<ServerResponse> updateUser(ServerRequest request) {
        String userId = request.pathVariable("id");
        Mono<UserDTO> userDtoMono = request.bodyToMono(UserDTO.class);
        return userDtoMono.flatMap(userDTO ->
                userService.updateUser(userId, userDTO)
                        .flatMap(user -> ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(user))
                        .switchIfEmpty(ServerResponse.notFound().build()));
    }

    public Mono<ServerResponse> deleteUser(ServerRequest request) {
        String userId = request.pathVariable("id");
        return userService.deleteUser(userId)
                .then(ServerResponse.ok().<Void>build())
                .onErrorReturn((ServerResponse) ResponseEntity.notFound().build());
    }
}