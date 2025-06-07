package com.lab.blps.components;

import com.lab.blps.dtos.AuthenticationDto;
import com.lab.blps.models.applications.Role;
import com.lab.blps.models.applications.User;
import com.lab.blps.security.JwtService;
import com.lab.blps.services.UserService;
import com.lab.blps.utils.CustomUserDetails;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Delegate for user registration (sign-up).
 */
@Component("SignUpDelegate")
@Transactional
public class SignUpDelegate implements JavaDelegate {

    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public SignUpDelegate(UserService userService,
                          JwtService jwtService,
                          PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String username = (String) execution.getVariable("username");
        String rawPassword = (String) execution.getVariable("password");
        Role role = (Role) execution.getVariable("role"); // ЭТА ХУЙНЯ МОЖЕТ НЕ ЗАРАБОТАТЬ ПРОВЕРИТЬ!!!
        String email = (String) execution.getVariable("email");

        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(rawPassword))
                .role(role)
                .email(email)
                .build();

        userService.create(user);

        String jwt = jwtService.generateToken(new CustomUserDetails(user));
        AuthenticationDto authDto = new AuthenticationDto(jwt, user);

        execution.setVariable("authenticationDto", authDto);
        execution.setVariable("jwt", jwt);
        execution.setVariable("user", user);
    }
}
