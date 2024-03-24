package com.itmo.blss.service.jaas;

import com.itmo.blss.api.response.RegistrationDto;
import com.itmo.blss.model.jaas.User;
import com.itmo.blss.service.UserDbService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserDbService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserDbService userService,
                       PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean register(RegistrationDto request) {
        var user = new User()
                .setUsername(request.username())
                .setPassword(passwordEncoder.encode(request.password()))
                .setRole(request.role());
        userService.save(user);
        return true;
    }
}
