package com.lab.blps.controllers;


import com.lab.blps.dtos.AuthenticationDto;
import com.lab.blps.dtos.SignInDto;
import com.lab.blps.dtos.SignUpDto;
import com.lab.blps.services.AuthenticationService;
import com.lab.blps.xml.XmlUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/sign-up")
    public AuthenticationDto signUp(@RequestBody SignUpDto request) {
            return authenticationService.signUp(request);
    }

    @PostMapping("/sign-in")
    public AuthenticationDto signIn(@RequestBody SignInDto request) {
        return authenticationService.signIn(request);
    }
}
