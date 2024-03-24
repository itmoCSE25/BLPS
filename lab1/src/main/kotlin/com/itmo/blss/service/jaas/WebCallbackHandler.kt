package com.itmo.blss.service.jaas

import org.springframework.security.authentication.jaas.JaasAuthenticationCallbackHandler
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import javax.security.auth.callback.Callback
import javax.security.auth.callback.NameCallback
import javax.security.auth.callback.PasswordCallback
import javax.security.auth.callback.UnsupportedCallbackException

@Service
class WebCallbackHandler : JaasAuthenticationCallbackHandler {

    override fun handle(callback: Callback, auth: Authentication) {
        when (callback) {
            is NameCallback -> callback.name = auth.name
            is PasswordCallback -> callback.password = (auth.credentials as String).toCharArray()
            else -> throw UnsupportedCallbackException(callback, "The submitted Callback is unsupported")
        }
    }
}