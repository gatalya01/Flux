package com.example.webflux_example.handler;

import com.example.webflux_example.model.ItemModel;
import com.example.webflux_example.model.SubItemModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class ItemHandler {
    public Mono<ServerResponse> getAllItem(ServerRequest serverRequest){
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(Flux.just(
                        new ItemModel(UUID.randomUUID().toString(), "Name 1" , 10, Collections.emptyList()),
                        new ItemModel(UUID.randomUUID().toString(), "Name 2" , 20, List.of(
                                new SubItemModel("SubItem 1" , BigDecimal.valueOf(1001)),
                                new SubItemModel("SubItem 2" , BigDecimal.valueOf(2002))
                        ))), ItemModel.class);


    }
    public Mono<ServerResponse> findById(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");

        // Log the incoming request
        log.info("Fetching item with id: {}", id);

        // Here you would normally fetch the item from a database or service
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(new ItemModel(id, "Item name 1", 10, Collections.emptyList())), ItemModel.class)
                .switchIfEmpty(ServerResponse.notFound().build());
    }
    public Mono<ServerResponse> createItem(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(ItemModel.class)
                .flatMap(item -> {
                    log.info("Item for create: {}", item);
                    // Return the response with proper created status
                    return ServerResponse.created(URI.create("/api/v1/functions/item/" + item.getId())).build();
                })
                .onErrorResume(ex -> {
                    log.error("Error while creating item: {}", ex.getMessage());
                    return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue("Invalid item data.");
                });
    }

    public Mono<ServerResponse> errorRequest(ServerRequest serverRequest){
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(Mono.error(new RuntimeException("Ex in errorRequest")), String.class)
                .onErrorResume(ex -> {log.error("Error in errorRequest: {}",ex);
                    return ServerResponse.badRequest().body(Mono.error(ex),String.class);
    });
    }
}
