package com.vash.entel.api;

import com.vash.entel.dto.UserDTO;
import com.vash.entel.model.entity.User;
import com.vash.entel.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserServiceImpl userService;

    @PostMapping
    public ResponseEntity<String> login (@RequestBody User loginRequest){
        User user = userService.findByEmail(loginRequest.getEmail());

        if(user != null && user.getPassword().equals(loginRequest.getPassword())){
            return new ResponseEntity<>("Login successful", HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Login failed", HttpStatus.UNAUTHORIZED);
        }
    }
}
