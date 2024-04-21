package com.itmo.blss.api

import com.itmo.blss.api.response.RegistrationDto
import com.itmo.blss.service.jaas.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(private val authenticationService: AuthService) {



    @PostMapping("/registration")
    fun register(@RequestBody request: RegistrationDto?): ResponseEntity<Boolean> {
        return ResponseEntity.ok(authenticationService.register(request!!))
    }

    @GetMapping("/login")
    fun login(): ResponseEntity<String> {
        return ResponseEntity.ok("Need to login")
    }
}