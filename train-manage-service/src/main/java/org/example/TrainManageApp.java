package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import bitronix.tm.TransactionManagerServices;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.Producer;
import org.example.config.KafkaConfig;
import org.example.service.ConsumerMessageService;
import org.example.service.MessageSenderService;

public class TrainManageApp {
    public static void main(String[] args) {
        TransactionManagerServices.getConfiguration().setLogPart1Filename("btm2.tlog");
        TransactionManagerServices.getConfiguration().setLogPart2Filename("btm1.tlog");

        Properties projectProperties = loadProjectProperties();
        String bootstrapServers = projectProperties.getProperty("bootstrap.servers");
        KafkaConsumer<String, String> consumer = KafkaConfig.billingConsumer(bootstrapServers);
        Producer<String, String> producer = KafkaConfig.billingProducer(bootstrapServers);
        MessageSenderService messageSenderService = new MessageSenderService(producer);
        ConsumerMessageService consumerMessageService = new ConsumerMessageService(consumer, messageSenderService);
        consumerMessageService.startConsumer();
    }

    private static Properties loadProjectProperties() {
        Properties properties = new Properties();
        try (InputStream is = TrainManageApp.class.getResourceAsStream("/application.properties")) {
            properties.load(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties;
    }
}
