package com.itmo.blss.api.response

import com.itmo.blss.model.jaas.Role

data class RegistrationDto(@JvmField val username: String, @JvmField val password: String, @JvmField val role: Role)