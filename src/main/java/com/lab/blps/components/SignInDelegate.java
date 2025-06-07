package com.lab.blps.components;

import com.lab.blps.dtos.AuthenticationDto;
import com.lab.blps.models.applications.User;
import com.lab.blps.security.JwtService;
import com.lab.blps.services.UserService;
import com.lab.blps.utils.CustomUserDetails;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Delegate for user authentication (sign-in).
 */
@Component("SignInDelegate")
@Transactional
public class SignInDelegate implements JavaDelegate {

    private final AuthenticationManager authManager;
    private final UserService userService;
    private final JwtService jwtService;

    public SignInDelegate(AuthenticationManager authManager,
                          UserService userService,
                          JwtService jwtService) {
        this.authManager = authManager;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String username = (String) execution.getVariable("username");
        String password = (String) execution.getVariable("password");

        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        User user = userService.getByUsername(username);
        String jwt = jwtService.generateToken(new CustomUserDetails(user));
        AuthenticationDto authDto = new AuthenticationDto(jwt, user);

        execution.setVariable("authenticationDto", authDto);
        execution.setVariable("jwt", jwt);
        execution.setVariable("user", user);
    }
}

