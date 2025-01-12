package com.familiahuecas.backend.rest;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.familiahuecas.backend.entity.Rol;
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
            if (authRequest.getName() != null && authRequest.getPassword() != null) {
                return authenticateWithUsernameAndPassword(authRequest);
            } else if (authRequest.getSecuencia() != null) {
                return authenticateWithSequence(authRequest.getSecuencia());
            } else {
                return ResponseEntity.badRequest().body("Parámetros insuficientes para el inicio de sesión.");
            }
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas.");
        }
    }

    private ResponseEntity<?> authenticateWithUsernameAndPassword(AuthRequest authRequest) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                authRequest.getName(), authRequest.getPassword()
            )
        );

        // Obtener el usuario
        User user = userService.findByName(authRequest.getName())
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return buildAuthResponse(user, "¡Inicio de sesión exitoso!");
    }

    private ResponseEntity<?> authenticateWithSequence(String sequence) {
        User user = userService.findBySequence(sequence);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Secuencia no encontrada.");
        }

        return buildAuthResponse(user, "¡Inicio de sesión exitoso por secuencia!");
    }

    private ResponseEntity<?> buildAuthResponse(User user, String message) {
        String token = jwtService.generateToken(user);

        Set<String> roleNames = user.getRoles().stream()
            .map(Rol::getNombre)
            .collect(Collectors.toSet());

        UserResponse userResponse = new UserResponse(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getEnabled(),
            roleNames,
            message,
            user.getSecuencia()
        );

        AuthResponse authResponse = new AuthResponse(token, userResponse);
        return ResponseEntity.ok(authResponse);
    }

}
