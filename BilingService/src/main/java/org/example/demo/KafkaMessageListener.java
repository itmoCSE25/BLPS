package org.example.demo;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

@Service
public class KafkaMessageListener {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    private final BillingService billingService;

    @Autowired
    public KafkaMessageListener(BillingService billingService) {
        this.billingService = billingService;
    }

    @PostConstruct
    public void listen() {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        try (Consumer<String, String> consumer = new KafkaConsumer<>(properties)) {

            consumer.subscribe(Collections.singletonList("billing-transactions"));

            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println("Received message: " + record.value());
                    processMessage(record.value());
                }
            }
        }
    }

    private void processMessage(String message) {
        String[] parts = message.split(":");
        if (parts.length == 2) {
            int userId = Integer.parseInt(parts[0]);
            double amount = Double.parseDouble(parts[1]);
            if (amount < 0) {
                billingService.subtractMoney(userId, -amount);
            } else {
                billingService.addMoney(userId, amount);
            }
        } else {
            System.out.println("Invalid message format: " + message);
        }
    }
}
