package com.itmo.blss.service.jaas;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import com.itmo.blss.model.jaas.RolePrincipal;
import com.itmo.blss.model.jaas.User;
import com.itmo.blss.model.jaas.UserPrincipal;
import com.itmo.blss.service.UserDbService;
import org.springframework.security.crypto.password.PasswordEncoder;

@SuppressWarnings("unused")
public class JaasLoginModule implements LoginModule {

    private UserDbService userService;
    private PasswordEncoder passwordEncoder;
    private CallbackHandler handler;
    private Subject subject;
    private ArrayList<String> userGroups;
    private String login;
    private UserPrincipal userPrincipal;
    private RolePrincipal rolePrincipal;

    @Override
    public void initialize(Subject subject,
                           CallbackHandler callbackHandler,
                           Map<String, ?> sharedState,
                           Map<String, ?> options) {
        handler = callbackHandler;
        this.subject = subject;
        this.userService = (UserDbService) options.get("userService");
        this.passwordEncoder = (PasswordEncoder) options.get("passwordEncoder");
    }

    @Override
    public boolean login() throws LoginException {
        // Добавляем колбэки
        Callback[] callbacks = new Callback[2];
        callbacks[0] = new NameCallback("Username");
        callbacks[1] = new PasswordCallback("Password", true);
        // При помощи колбэков получаем через CallbackHandler логин и пароль
        try {
            handler.handle(callbacks);
            String name = ((NameCallback) callbacks[0]).getName();
            String password = String.valueOf(((PasswordCallback) callbacks[1]).getPassword());
            User user = userService.getByUsername(name);
            if (passwordEncoder.matches(password, user.getPassword())) {
                login = name;
                userGroups = new ArrayList<>();
                userGroups.add(user.getRole().toString());
                return true;
            } else {
                throw new LoginException("Invalid credentials for user: %s");
            }
        } catch (IOException | UnsupportedCallbackException e) {
            throw new LoginException(e.getMessage());
        }
    }

    @Override
    public boolean commit() {
        userPrincipal = new UserPrincipal(login);
        subject.getPrincipals().add(userPrincipal);
        if (userGroups != null && userGroups.size() > 0) {
            for (String groupName : userGroups) {
                rolePrincipal = new RolePrincipal(groupName);
                subject.getPrincipals().add(rolePrincipal);
            }
        }
        return true;
    }

    @Override
    public boolean abort() {
        return false;
    }

    @Override
    public boolean logout() {
        subject.getPrincipals().remove(userPrincipal);
        subject.getPrincipals().remove(rolePrincipal);
        return true;
    }
}
