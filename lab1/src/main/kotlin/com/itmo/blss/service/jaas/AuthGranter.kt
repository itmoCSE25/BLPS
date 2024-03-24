package com.itmo.blss.service.jaas

import org.springframework.security.authentication.jaas.AuthorityGranter
import org.springframework.stereotype.Service
import java.security.Principal

@Service
class AuthGranter : AuthorityGranter {
    override fun grant(principal: Principal): Set<String> {
        return java.util.Set.of(principal.name)
    }
}