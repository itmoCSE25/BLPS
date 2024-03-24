package com.itmo.blss.service.jaas

import com.itmo.blss.api.response.RegistrationDto
import com.itmo.blss.model.jaas.User
import com.itmo.blss.service.UserDbService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userService: UserDbService,
    private val passwordEncoder: PasswordEncoder
) {
    fun register(request: RegistrationDto): Boolean {
        val user = User()
            .setUsername(request.username)
            .setPassword(passwordEncoder.encode(request.password))
            .setRole(request.role)
        userService.save(user)
        return true
    }
}