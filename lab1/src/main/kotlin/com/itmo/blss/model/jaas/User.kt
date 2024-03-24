package com.itmo.blss.model.jaas

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class User : UserDetails {
    var id: Long? = null
        private set
    private var username: String? = null
    private var password: String? = null
    var role: Role? = null
        private set

    fun setId(id: Long?): User {
        this.id = id
        return this
    }

    override fun getUsername(): String {
        return username!!
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

    fun setUsername(username: String?): User {
        this.username = username
        return this
    }

    override fun getAuthorities(): Collection<GrantedAuthority?> {
        return listOf(SimpleGrantedAuthority(role!!.name))
    }

    override fun getPassword(): String {
        return password!!
    }

    fun setPassword(password: String?): User {
        this.password = password
        return this
    }

    fun setRole(role: Role?): User {
        this.role = role
        return this
    }
}