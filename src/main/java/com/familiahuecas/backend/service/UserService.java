package com.familiahuecas.backend.service;

import java.util.List;

import java.util.Optional;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        // Verificar si el email ya existe para creación
        if (user.getId() == null || user.getId() == 0) {
            if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                throw new UserAlreadyExistsException("El email ya está en uso: " + user.getEmail());
            }
        }

        // Asignar roles al usuario
        Set<Rol> roles = roleIds.stream()
                .map(roleId -> rolRepository.findById(roleId)
                        .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado con ID: " + roleId)))
                .collect(Collectors.toSet());
        user.setRoles(roles);
     // Si es una actualización, mantener la secuencia existente
        if (user.getId() != null) {
            userRepository.findById(user.getId()).ifPresent(existingUser -> {
                user.setSecuencia(existingUser.getSecuencia());
            });
        }
        // Codificar la contraseña si está presente
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else if (user.getId() != null) {
            // Si es una actualización y la contraseña no se proporciona, mantener la existente
            user.setPassword(userRepository.findById(user.getId())
                    .map(User::getPassword)
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + user.getId())));
        }

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
                "OK",
                savedUser.getSecuencia()
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



	public Optional<User> findByName(String name) {
		return userRepository.findByName(name);
	}


	public Page<UserResponse> getAllPaginated(Pageable pageable) {
	    // Obtener usuarios paginados desde el repositorio
	    Page<User> usersPage = userRepository.findAll(pageable);

	    // Convertir las entidades en UserResponse, incluyendo la conversión de roles
	    return usersPage.map(this::convertToUserResponse);
	}

	private UserResponse convertToUserResponse(User user) {
	    // Convertir Set<Rol> a Set<String> con los nombres de los roles
	    Set<String> roleNames = user.getRoles().stream()
	            .map(Rol::getNombre) // Asumiendo que Rol tiene un método getNombre()
	            .collect(Collectors.toSet());

	    return new UserResponse(
	            user.getId(),
	            user.getName(),
	            user.getEmail(),
	            user.isEnabled(),
	            roleNames, // Pasar el Set<String> con los nombres de los roles
	            "OK" ,// Mensaje fijo o dinámico si es necesario
	            user.getSecuencia()
	    );
	}


	public User findBySequence(String sequence) {
		return userRepository.findBySecuencia(sequence);
	}


}
