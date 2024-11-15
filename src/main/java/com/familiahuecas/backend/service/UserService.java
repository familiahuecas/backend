package com.familiahuecas.backend.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.familiahuecas.backend.entity.Rol;
import com.familiahuecas.backend.entity.User;
import com.familiahuecas.backend.exception.UserAlreadyExistsException;
import com.familiahuecas.backend.repository.RolRepository;
import com.familiahuecas.backend.repository.UserRepository;
import com.familiahuecas.backend.rest.response.UserResponse;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final RolRepository rolRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RolRepository rolRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public UserResponse saveOrUpdate(User user, Set<Long> roleIds) {
        // Verificar si el email ya existe
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("El email ya está en uso: " + user.getEmail());
        }

        // Asignar roles al usuario
        Set<Rol> roles = roleIds.stream()
                .map(roleId -> rolRepository.findById(roleId)
                        .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado con ID: " + roleId)))
                .collect(Collectors.toSet());
        user.setRoles(roles);

        
     // Codificar la contraseña antes de guardar
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // Guardar el usuario
        User savedUser = userRepository.save(user);

        // Crear y devolver el UserResponse
        Set<String> roleNames = savedUser.getRoles().stream()
                .map(Rol::getNombre)
                .collect(Collectors.toSet());

        return new UserResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getEnabled(),
                roleNames,
                "OK"
        );
    }
    
    public void deleteUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + id));

        userRepository.delete(user);
    }
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
