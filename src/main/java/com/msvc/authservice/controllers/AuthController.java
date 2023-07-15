package com.msvc.authservice.controllers;

import com.msvc.authservice.dto.AuthUserDTO;
import com.msvc.authservice.dto.TokenDTO;
import com.msvc.authservice.entities.AuthUser;
import com.msvc.authservice.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    public ResponseEntity<TokenDTO> login(@RequestBody AuthUserDTO authUserDTO) {
        TokenDTO tokenDTO = authService.login(authUserDTO);
        if (tokenDTO == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(tokenDTO);
    }

    @PostMapping("/validate")
    public ResponseEntity<TokenDTO> validate(@RequestParam String token) {
        TokenDTO tokenDto = authService.validate(token);
        if (tokenDto == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(tokenDto);
    }

    @PostMapping("/create")
    public ResponseEntity<AuthUser> create(@RequestBody AuthUserDTO authUserDTO) {
        AuthUser authUser = authService.save(authUserDTO);
        if (authUser == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(authUser);
    }


}
