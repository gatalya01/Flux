package com.example.webflux_example.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    @NotNull
    private String id;

    @NotBlank(message = "Username is mandatory")
    private String username;

    @Email(message = "Email should be valid")
    private String email;
}