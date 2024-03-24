package com.itmo.blss.model.jaas

import java.security.Principal
import javax.security.auth.Subject

data class UserPrincipal(var name: String?) : Principal {
    override fun getName(): String? {
        return name
    }

    override fun implies(subject: Subject): Boolean {
        return super.implies(subject)
    }
}