package com.familiahuecas.backend.rest.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private Boolean enabled;
    private Set<String> roles;
    private String messaje; // Campo para el mensaje de éxito
}
