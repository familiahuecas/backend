package com.familiahuecas.backend.rest.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequest {
    private String name;
    private String password;
    private String secuencia;
}