package com.vash.entel.api;

import com.vash.entel.dto.AuthResponseDTO;
import com.vash.entel.dto.LoginDTO;
import com.vash.entel.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        AuthResponseDTO authResponse = userService.login(loginDTO);
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }
}
