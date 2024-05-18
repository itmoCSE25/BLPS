package com.itmo.blss.config;

import com.atomikos.icatch.jta.UserTransactionManager;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.atomikos.jms.AtomikosConnectionFactoryBean;
import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.SystemException;
import org.apache.activemq.spring.ActiveMQXAConnectionFactory;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.transaction.KafkaTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.jms.ConnectionFactory;

@Configuration
@Profile("!junit")
@EnableJpaRepositories
public class KafkaConfig {

    //https://www.baeldung.com/spring-kafka

    @Value(value = "${bootstrap.servers}")
    private String bootstrapAddress;

//    @Bean
//    public KafkaAdmin kafkaAdmin() {
//        Map<String, Object> configs = new HashMap<>();
//        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
//        return new KafkaAdmin(configs);
//    }

    @Bean(initMethod = "init", destroyMethod = "close")
    @Primary
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
    public AtomikosDataSourceBean masterDataSourceBean() {
        AtomikosDataSourceBean dataSource = new AtomikosDataSourceBean();
        dataSource.setLocalTransactionMode(false);
        dataSource.setUniqueResourceName("master_db");
        dataSource.setXaDataSourceClassName("org.postgresql.xa.PGXADataSource");
        Properties xaProperties = new Properties();
        xaProperties.setProperty("user", "blps");
        xaProperties.setProperty("password", "blps");
        xaProperties.setProperty("URL", "jdbc:postgresql://localhost:5434/master");
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

    @Bean
    @Primary
    public TransactionManager transactionManager() throws SystemException {
        return jtaTransactionManager();
    }

    @Primary
    @Bean
    public ConnectionFactory connectionFactory() {
        AtomikosConnectionFactoryBean atomikosConnectionFactoryBean = new AtomikosConnectionFactoryBean();
        atomikosConnectionFactoryBean.setXaConnectionFactory(new ActiveMQXAConnectionFactory());
        return atomikosConnectionFactoryBean;
    }

    @Bean
    public ProducerFactory<String, String> producerFactory(
            @Value("${bootstrap.servers}")
            String bootstrapServer
    ) {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "tr-id");
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
        DefaultKafkaProducerFactory<String, String> factory = new DefaultKafkaProducerFactory<>(props);
        factory.setTransactionIdPrefix("tx-");
        return factory;
    }

    @Bean
    public KafkaTransactionManager<String, String> kafkaTransactionManager(
            ProducerFactory<String, String> producerFactory
    ) {
        KafkaTransactionManager<String, String> manager = new KafkaTransactionManager<String, String>(producerFactory);
        return manager;
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(
            ProducerFactory<String, String> producerFactory
    ) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public TransactionTemplate transactionTemplate(
            JtaTransactionManager transactionManager
    ) {
        return new TransactionTemplate(transactionManager);
    }
}
