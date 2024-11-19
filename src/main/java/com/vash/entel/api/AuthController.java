package com.vash.entel.api;

import com.vash.entel.model.entity.User;
import com.vash.entel.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserServiceImpl userService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> login(@RequestBody User loginRequest) {
        User user = userService.findByEmail(loginRequest.getEmail());

        // Comprobamos que el usuario exista y la contraseña sea correcta
        if (user != null && user.getPassword().equals(loginRequest.getPassword())) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("role", user.getRole()); // Rol del usuario
            response.put("moduleId", user.getModule() != null ? user.getModule().getId() : null); // ID del módulo si está asociado
            response.put("id", user.getId()); // ID del usuario
            response.put("name", user.getName()); // Nombre del usuario

            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(Map.of("message", "Login failed"), HttpStatus.UNAUTHORIZED);
        }
    }
}
