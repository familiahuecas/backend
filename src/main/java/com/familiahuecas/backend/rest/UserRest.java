package com.familiahuecas.backend.rest;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.familiahuecas.backend.entity.Rol;
import com.familiahuecas.backend.entity.User;
import com.familiahuecas.backend.exception.UserAlreadyExistsException;
import com.familiahuecas.backend.rest.request.UserRequest;
import com.familiahuecas.backend.rest.response.RecaudacionesResponse;
import com.familiahuecas.backend.rest.response.UserResponse;
import com.familiahuecas.backend.service.UserService;

@RestController
@RequestMapping("/users")
public class UserRest {
    private final UserService userService;

    public UserRest(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody UserRequest userRequest) {
        try {
            // Crear un nuevo usuario y asignar los valores de UserRequest
            User user = new User();
            user.setId(userRequest.getId());
            user.setName(userRequest.getName());
            user.setEmail(userRequest.getEmail());
            user.setPassword(userRequest.getPassword());
            user.setEnabled(userRequest.getEnabled());

            // Llamar al servicio para guardar o actualizar el usuario y obtener la respuesta
            UserResponse response = userService.saveOrUpdate(user, userRequest.getRoles());

            // Agregar el mensaje de éxito
            response.setMessaje("OK");
            return ResponseEntity.ok(response);

        } catch (UserAlreadyExistsException e) {
            // Si hay un error, crea un UserRequest solo con el mensaje de error
            UserRequest errorResponse = new UserRequest();
            errorResponse.setMessaje(e.getMessage()); // Asegúrate de asignar el mensaje de error aquí
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUserById(id);
            return ResponseEntity.ok("Usuario eliminado con éxito");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al eliminar el usuario: " + e.getMessage());
        }
    }
    @GetMapping("/list")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        // Obtener todos los usuarios desde el servicio
        List<User> users = userService.getAllUsers();

        // Convertir la lista de usuarios a UserResponse
        List<UserResponse> userResponses = users.stream().map(user -> {
            // Convertir Set<Rol> a Set<String> con los nombres de los roles
            Set<String> roleNames = user.getRoles().stream()
                    .map(Rol::getNombre)
                    .collect(Collectors.toSet());

            return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getEnabled(),
                roleNames, // Pasar el Set<String> con los nombres de los roles
                "OK",
                user.getSecuencia()
            );
        }).collect(Collectors.toList());

        return ResponseEntity.ok(userResponses);
    }
    @GetMapping("/listpaginated")
    public ResponseEntity<Page<UserResponse>> getAllRecaudaciones(
            @PageableDefault() Pageable pageable) {

       
        // Llamar al servicio con el pageable ordenado
        Page<UserResponse> usuarios = userService.getAllPaginated(pageable);

        // Devolver la respuesta
        return ResponseEntity.ok(usuarios);
    }
}
