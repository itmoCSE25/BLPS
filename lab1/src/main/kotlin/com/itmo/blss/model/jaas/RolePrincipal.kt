package com.itmo.blss.model.jaas

import java.security.Principal
import javax.security.auth.Subject

@JvmRecord
data class RolePrincipal(val name: String) : Principal {
    override fun getName(): String {
        return name
    }

    override fun implies(subject: Subject): Boolean {
        return super<Principal>.implies(subject)
    }
}