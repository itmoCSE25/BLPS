package com.itmo.blss.api.response;

import com.itmo.blss.model.jaas.Role;

public record RegistrationDto(String username, String password, Role role) {
}
