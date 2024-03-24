package com.itmo.blss.model.jaas;

import java.security.Principal;

import javax.security.auth.Subject;

public record RolePrincipal(
        String name
) implements Principal {

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean implies(Subject subject) {
        return Principal.super.implies(subject);
    }
}
