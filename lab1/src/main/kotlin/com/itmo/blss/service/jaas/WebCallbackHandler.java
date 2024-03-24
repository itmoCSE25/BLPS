package com.itmo.blss.service.jaas;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.springframework.security.authentication.jaas.JaasAuthenticationCallbackHandler;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class WebCallbackHandler implements JaasAuthenticationCallbackHandler {

    @Override
    public void handle(Callback callback, Authentication auth) throws UnsupportedCallbackException {
        if (callback instanceof NameCallback nameCallback) {
            nameCallback.setName(auth.getName());
        } else if (callback instanceof PasswordCallback passwordCallback) {
            passwordCallback.setPassword(((String) auth.getCredentials()).toCharArray());
        } else {
            throw new UnsupportedCallbackException(callback, "The submitted Callback is unsupported");
        }
    }
}
