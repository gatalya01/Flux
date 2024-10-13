package com.example.webflux_example.repository;

import com.example.webflux_example.entity.TaskEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface TaskRepository extends ReactiveMongoRepository<TaskEntity, String> {
}