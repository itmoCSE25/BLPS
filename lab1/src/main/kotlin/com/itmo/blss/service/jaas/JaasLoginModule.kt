package com.itmo.blss.service.jaas

import com.itmo.blss.model.jaas.RolePrincipal
import com.itmo.blss.model.jaas.UserPrincipal
import com.itmo.blss.service.UserDbService
import org.springframework.security.crypto.password.PasswordEncoder
import java.io.IOException
import javax.security.auth.Subject
import javax.security.auth.callback.Callback
import javax.security.auth.callback.CallbackHandler
import javax.security.auth.callback.NameCallback
import javax.security.auth.callback.PasswordCallback
import javax.security.auth.callback.UnsupportedCallbackException
import javax.security.auth.login.LoginException
import javax.security.auth.spi.LoginModule

@Suppress("unused")
class JaasLoginModule : LoginModule {
    private var userService: UserDbService? = null
    private var passwordEncoder: PasswordEncoder? = null
    private var handler: CallbackHandler? = null
    private var subject: Subject? = null
    private var userGroups: ArrayList<String>? = null
    private var login: String? = null
    private var userPrincipal: UserPrincipal? = null
    private var rolePrincipal: RolePrincipal? = null
    override fun initialize(
        subject: Subject,
        callbackHandler: CallbackHandler,
        sharedState: Map<String?, *>?,
        options: Map<String?, *>
    ) {
        handler = callbackHandler
        this.subject = subject
        userService = options["userService"] as UserDbService?
        passwordEncoder = options["passwordEncoder"] as PasswordEncoder?
    }

    @Throws(LoginException::class)
    override fun login(): Boolean {
        // Добавляем колбэки
        val callbacks = arrayOfNulls<Callback>(2)
        callbacks[0] = NameCallback("Username")
        callbacks[1] = PasswordCallback("Password", true)
        // При помощи колбэков получаем через CallbackHandler логин и пароль
        return try {
            handler!!.handle(callbacks)
            val name = (callbacks[0] as NameCallback?)!!.name
            val password = String((callbacks[1] as PasswordCallback?)!!.password)
            val user = userService!!.getByUsername(name)
            if (passwordEncoder!!.matches(password, user.password)) {
                login = name
                userGroups = ArrayList()
                userGroups!!.add(user.role.toString())
                true
            } else {
                throw LoginException("Invalid credentials for user: %s")
            }
        } catch (e: IOException) {
            throw LoginException(e.message)
        } catch (e: UnsupportedCallbackException) {
            throw LoginException(e.message)
        }
    }

    override fun commit(): Boolean {
        userPrincipal = UserPrincipal(login)
        subject!!.principals.add(userPrincipal)
        if (userGroups != null && userGroups!!.size > 0) {
            for (groupName in userGroups!!) {
                rolePrincipal = RolePrincipal(groupName)
                subject!!.principals.add(rolePrincipal)
            }
        }
        return true
    }

    override fun abort(): Boolean {
        return false
    }

    override fun logout(): Boolean {
        subject!!.principals.remove(userPrincipal)
        subject!!.principals.remove(rolePrincipal)
        return true
    }
}