package com.example.webflux_example.configuration;

import com.example.webflux_example.handler.ItemHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@Slf4j
public class ItemRouter {
    @Bean(name = "customItemRouter")
    public RouterFunction<ServerResponse> customItemRouter(ItemHandler itemHandler) {
        return RouterFunctions.route()
                .GET("api/v1/functions/item", itemHandler::getAllItem)
                .GET("api/v1/functions/item/{id}", itemHandler::findById)
                .POST("api/v1/functions/item", itemHandler::createItem)
                .GET("api/v1/functions/error", itemHandler::errorRequest)
                .build();
    }
}
