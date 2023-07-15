package com.msvc.authservice.services;

import com.msvc.authservice.dto.AuthUserDTO;
import com.msvc.authservice.dto.TokenDTO;
import com.msvc.authservice.entities.AuthUser;
import com.msvc.authservice.repository.AuthUserRepository;
import com.msvc.authservice.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private AuthUserRepository authUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtProvider jwtProvider;

    public AuthUser save(AuthUserDTO authUserDTO) {
        Optional<AuthUser> user = authUserRepository.findByUsername(authUserDTO.getUsername());
        if (user.isPresent()) {
//            throw new RuntimeException("El usuario ya existe");
            return null;
        }

        String password = passwordEncoder.encode(authUserDTO.getPassword());
        AuthUser authUser = AuthUser.builder()
                .username(authUserDTO.getUsername())
                .password(password)
                .build();
        return authUserRepository.save(authUser);
    }

    public TokenDTO login(AuthUserDTO authUserDTO) {
        Optional<AuthUser> user = authUserRepository
                .findByUsername(authUserDTO.getUsername());

        if (user.isPresent()) {
            if (passwordEncoder.matches(authUserDTO.getPassword(),
                    user.get().getPassword())) {

                return TokenDTO.builder()
                        .token("Bearer " + jwtProvider.createToken(user.get()))
                        .build();
            }
        }

        return null;
    }

    public TokenDTO validate(String token) {
        if (jwtProvider.validate(token)) {
            String user = jwtProvider.getUsername(token);
            if(authUserRepository.findByUsername(user).isEmpty()) {
                return null;
            }
            return TokenDTO.builder()
                    .token("Bearer " + token)
                    .build();
        }
        return null;
    }

}
