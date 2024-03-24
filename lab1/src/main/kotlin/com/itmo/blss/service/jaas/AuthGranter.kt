package com.itmo.blss.service.jaas;

import java.security.Principal;
import java.util.Set;

import org.springframework.security.authentication.jaas.AuthorityGranter;
import org.springframework.stereotype.Service;

@Service
public class AuthGranter implements AuthorityGranter {
    @Override
    public Set<String> grant(Principal principal) {
        return Set.of(principal.getName());
    }
}
