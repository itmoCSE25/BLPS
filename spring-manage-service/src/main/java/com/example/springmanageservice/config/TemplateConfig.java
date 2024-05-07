package com.example.springmanageservice.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
public class TemplateConfig {

    @Bean
    public NamedParameterJdbcTemplate myJdbcTemplate(
            DataSource dataSourceBean
    ) {
        return new NamedParameterJdbcTemplate(dataSourceBean);
    }
}
