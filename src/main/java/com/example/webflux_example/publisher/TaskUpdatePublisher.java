package com.example.webflux_example.publisher;

import com.example.webflux_example.model.TaskDTO;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Sinks;

@Component
public class TaskUpdatePublisher {
    private final Sinks.Many<TaskDTO> taskModelUpdatesSinks;

    public TaskUpdatePublisher() {
        this.taskModelUpdatesSinks = Sinks.many().multicast().onBackpressureBuffer();
    }

    public void publish(TaskDTO taskDTO) {
        taskModelUpdatesSinks.tryEmitNext(taskDTO);
    }
    public Sinks.Many<TaskDTO> getUpdateSinks() {
        return taskModelUpdatesSinks;
    }
}
