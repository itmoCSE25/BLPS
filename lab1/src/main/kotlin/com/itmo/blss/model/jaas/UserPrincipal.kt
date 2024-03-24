package com.itmo.blss.model.jaas

import java.security.Principal
import javax.security.auth.Subject

data class UserPrincipal(val userName: String) : Principal {
    override fun getName(): String {
        return userName
    }

    override fun implies(subject: Subject): Boolean {
        return super.implies(subject)
    }
}