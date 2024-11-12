package com.familiahuecas.backend.rest.request;

import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
public class UserRequest {
    private String name;
    private String email;
    private String password;
    private Boolean enabled;
    private Set<Long> roleIds; // IDs de los roles asignados
    private String messaje; // Campo para el mensaje de error
}
