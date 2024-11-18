package com.familiahuecas.backend.rest.response;


import com.familiahuecas.backend.entity.User;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponse {
    private String token;
    
    private UserResponse user;
}
