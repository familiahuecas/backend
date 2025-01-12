package com.familiahuecas.backend.rest.request;

import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
public class UserRequest {
	private long id;
    private String name;
    private String email;
    private String password;
    private Boolean enabled;
    private Set<Long> roles; // IDs de los roles asignados
    private String messaje; // Campo para el mensaje de error
    private String secuencia;
}
