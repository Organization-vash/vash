package com.vash.entel.service.impl;

import com.vash.entel.model.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import com.vash.entel.repository.UserRepository;
import com.vash.entel.service.AuthService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;

    @Override
    public User getAuthenticatedUser(HttpServletRequest request) {
        String username = request.getHeader("username"); // Obtener el nombre de usuario desde el encabezado
        if (username == null || username.isEmpty()) {
            throw new RuntimeException("Usuario no autenticado: Cabecera 'username' no encontrada");
        }

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario autenticado no encontrado"));
    }
}
