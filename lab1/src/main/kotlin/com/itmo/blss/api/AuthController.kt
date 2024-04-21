package com.itmo.blss.api;

import com.itmo.blss.api.response.RegistrationDto;
import com.itmo.blss.service.jaas.AuthService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authenticationService;

    public AuthController(AuthService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/registration")
    public boolean register(@RequestBody RegistrationDto request) {
        return authenticationService.register(request);
    }

    @GetMapping("/login")
    public String login() {
        return "Need to login";
    }
}
