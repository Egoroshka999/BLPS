package com.lab.blps.services;


import com.lab.blps.dtos.AuthenticationDto;
import com.lab.blps.dtos.SignInDto;
import com.lab.blps.dtos.SignUpDto;
import com.lab.blps.security.JwtService;
import com.lab.blps.utils.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.lab.blps.models.applications.User;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthenticationDto signUp(SignUpDto request) {
        var user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        userService.create(user);

        var jwt = jwtService.generateToken(new CustomUserDetails(user));
        return new AuthenticationDto(jwt, user);
    }


    @Transactional
    public AuthenticationDto signIn(SignInDto request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));

        var user = userService.getByUsername(request.getUsername());

        var jwt = jwtService.generateToken(new CustomUserDetails(user));
        return new AuthenticationDto(jwt, user);
    }
}
