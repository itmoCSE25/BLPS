package com.itmo.blss.config;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
public class TemplateConfig {

    @Bean
    @Primary
    public NamedParameterJdbcTemplate myJdbcTemplate(
        AtomikosDataSourceBean dataSourceBean
    ) {
        return new NamedParameterJdbcTemplate(dataSourceBean);
    }
}
