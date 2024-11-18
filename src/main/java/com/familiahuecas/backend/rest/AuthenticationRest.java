package com.familiahuecas.backend.rest;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.familiahuecas.backend.entity.User;
import com.familiahuecas.backend.rest.request.AuthRequest;
import com.familiahuecas.backend.rest.response.AuthResponse;
import com.familiahuecas.backend.rest.response.UserResponse;
import com.familiahuecas.backend.securiry.JwtService;
import com.familiahuecas.backend.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationRest {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        try {
            // Autenticar el usuario
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getName(), authRequest.getPassword())
            );

            // Cargar los detalles del usuario
            UserDetails userDetails = (UserDetails) authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(authRequest.getName(), authRequest.getPassword()))
                .getPrincipal();

            // Generar el token JWT
            String token = jwtService.generateToken(userDetails);
            
            // Obtener la información del usuario y transformar en UserResponse
            User user = userService.findByName(authRequest.getName()).get();
            Set<String> roleNames = user.getRoles().stream()
                .map(role -> role.getNombre())
                .collect(Collectors.toSet());

            UserResponse userResponse = new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getEnabled(),
                roleNames,
                "¡Inicio de sesión exitoso!" // Mensaje de éxito
            );

            // Crear la respuesta con el token y UserResponse
            AuthResponse authResponse = new AuthResponse(token, userResponse);
            return ResponseEntity.ok(authResponse);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body("Credenciales inválidas");
        }
    }
}
