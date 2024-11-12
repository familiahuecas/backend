package com.familiahuecas.backend.rest;

import com.familiahuecas.backend.entity.User;
import com.familiahuecas.backend.exception.UserAlreadyExistsException;
import com.familiahuecas.backend.rest.request.UserRequest;
import com.familiahuecas.backend.rest.response.UserResponse;
import com.familiahuecas.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserRest {
    private final UserService userService;

    public UserRest(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/createUser")
    public ResponseEntity<?> createUser(@RequestBody UserRequest userRequest) {
        try {
            // Crear un nuevo usuario y asignar los valores de UserRequest
            User user = new User();
            user.setName(userRequest.getName());
            user.setEmail(userRequest.getEmail());
            user.setPassword(userRequest.getPassword());
            user.setEnabled(userRequest.getEnabled());

            // Llamar al servicio para guardar o actualizar el usuario y obtener la respuesta
            UserResponse response = userService.saveOrUpdate(user, userRequest.getRoleIds());

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
}
