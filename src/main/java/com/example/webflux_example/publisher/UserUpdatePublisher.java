package com.example.webflux_example.publisher;

import com.example.webflux_example.model.UserDTO;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Sinks;

@Component
public class UserUpdatePublisher {
    private final Sinks.Many<UserDTO> userModelUpdatesSinks;

    public UserUpdatePublisher() {
        this.userModelUpdatesSinks = Sinks.many().multicast().onBackpressureBuffer();
    }

    public void publish(UserDTO userDTO) {
        userModelUpdatesSinks.tryEmitNext(userDTO);
    }
    public Sinks.Many<UserDTO> getUpdateSinks() {
        return userModelUpdatesSinks;
    }
}