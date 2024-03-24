package com.itmo.blss.config

import com.itmo.blss.model.jaas.Role
import com.itmo.blss.service.UserDbService
import com.itmo.blss.service.jaas.WebCallbackHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.jaas.AuthorityGranter
import org.springframework.security.authentication.jaas.DefaultJaasAuthenticationProvider
import org.springframework.security.authentication.jaas.JaasAuthenticationCallbackHandler
import org.springframework.security.authentication.jaas.memory.InMemoryConfiguration
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import javax.security.auth.login.AppConfigurationEntry

@Configuration
@EnableWebSecurity
class WebSecurityConfig(
    private val userService: UserDbService,
    private val authorityGranter: AuthorityGranter,
    private val webCallbackHandler: WebCallbackHandler
) {
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it
                    .requestMatchers("/api/seats").hasAuthority(Role.ROLE_USER.toString())
                    .requestMatchers("/api/vans").hasAuthority(Role.ROLE_ADMIN.toString())
                    .requestMatchers("/api/**").permitAll()
                    .anyRequest().permitAll()

            }
            .formLogin {
                it
                    .loginPage("/api/auth/login")
                    .permitAll()
            }
            .logout { it.permitAll() }
            .authenticationProvider(authenticationProvider(authorityGranter, webCallbackHandler))
        return http.build()
    }

    @Bean
    fun authenticationProvider(
        authorityGranter: AuthorityGranter,
        webCallbackHandler: WebCallbackHandler
    ): AuthenticationProvider {
        val authProvider = DefaultJaasAuthenticationProvider()
        val configurationEntry = AppConfigurationEntry(
            "com.itmo.blss.service.jaas.JaasLoginModule",
            AppConfigurationEntry.LoginModuleControlFlag.REQUIRED,
            mapOf(Pair("userService", userService), Pair("passwordEncoder", passwordEncoder()))
        )
        authProvider.setCallbackHandlers(arrayOf<JaasAuthenticationCallbackHandler>(webCallbackHandler))
        authProvider.setConfiguration(
            InMemoryConfiguration(
                mapOf(Pair("SPRINGSECURITY", arrayOf(configurationEntry)))
            )
        )
        authProvider.setAuthorityGranters(arrayOf(authorityGranter))
        return authProvider
    }

    @Bean
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager {
        return config.authenticationManager
    }
}