package com.itmo.blss.api.response

import com.itmo.blss.model.jaas.Role

data class RegistrationDto(val username: String, val password: String, val role: Role)