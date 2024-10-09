package com.example.webflux_example.model;

import com.example.webflux_example.entity.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.Instant;
import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {

    private String id;

    @NotBlank(message = "Title is mandatory")
    @Size(max = 100, message = "Title must be less than or equal to 100 characters")
    private String name;

    private String description;

    private Instant createdAt;
    private Instant updatedAt;
    private TaskStatus status;

    @NotNull(message = "Author ID is mandatory")
    private String authorId;

    @NotNull(message = "Assignee ID is mandatory")
    private String assigneeId;

    private Set<String> observerIds;


    private UserDTO author;
    private UserDTO assignee;
    private Set<UserDTO> observers;
}