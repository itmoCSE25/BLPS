package org.example;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;

import java.util.Properties;
import java.util.Collections;

public class KafkaTopicCreator {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        try (AdminClient adminClient = AdminClient.create(props)) {
            NewTopic billingTransactionsTopic = new NewTopic("billing-transactions", 1, (short) 1);
            adminClient.createTopics(Collections.singletonList(billingTransactionsTopic));

            NewTopic billingErrorsTopic = new NewTopic("billing-errors", 1, (short) 1);
            adminClient.createTopics(Collections.singletonList(billingErrorsTopic));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
