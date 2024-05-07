package org.example.demo;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Properties;

@Component
public class KafkaTopicCreator {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @PostConstruct
    public void createTopics() {
        Properties properties = new Properties();
        properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        try (AdminClient adminClient = AdminClient.create(properties)) {
            NewTopic billingTransactionsTopic = new NewTopic("billing-transactions", 1, (short) 1);
            NewTopic billingErrorsTopic = new NewTopic("billing-errors", 1, (short) 1);

            adminClient.createTopics(Collections.singletonList(billingTransactionsTopic));
            adminClient.createTopics(Collections.singletonList(billingErrorsTopic));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
