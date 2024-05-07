package com.itmo.blss.config;

import com.atomikos.icatch.jta.UserTransactionManager;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.atomikos.jdbc.internal.AtomikosSQLException;
import jakarta.transaction.SystemException;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.transaction.jta.JtaTransactionManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
@Profile("!junit")
public class KafkaConfig {

    //https://www.baeldung.com/spring-kafka

    @Value(value = "${bootstrap.servers}")
    private String bootstrapAddress;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic topic1() {
        return new NewTopic("baeldung", 1, (short) 1);
    }

    @Bean
    public NewTopic billingTopic() {
        return new NewTopic("billing-service", 1, (short) 1);
    }

    @Bean(initMethod = "init", destroyMethod = "close")
    public AtomikosDataSourceBean dataSourceBean() {
        AtomikosDataSourceBean dataSource = new AtomikosDataSourceBean();
        dataSource.setLocalTransactionMode(true);
        dataSource.setUniqueResourceName("db1");
        dataSource.setXaDataSourceClassName("org.postgresql.xa.PGXADataSource");
        Properties xaProperties = new Properties();
        xaProperties.setProperty("user", "blps");
        xaProperties.setProperty("password", "blps");
        xaProperties.setProperty("URL", "jdbc:postgresql://localhost:5432/blps_db");
        dataSource.setPoolSize(10);
        dataSource.setXaProperties(xaProperties);
        return dataSource;
    }

    @Bean(initMethod = "init", destroyMethod = "close")
    public UserTransactionManager userTransactionManager() throws jakarta.transaction.SystemException {
        UserTransactionManager userTransactionManager = new UserTransactionManager();
        userTransactionManager.setTransactionTimeout(300);
        userTransactionManager.setForceShutdown(true);
        return userTransactionManager;
    }

    @Bean
    public JtaTransactionManager jtaTransactionManager() throws SystemException {
        JtaTransactionManager jtaTransactionManager = new JtaTransactionManager();
        jtaTransactionManager.setTransactionManager(userTransactionManager());
        jtaTransactionManager.setUserTransaction(userTransactionManager());
        return jtaTransactionManager;
    }

}
