package com.example.springmanageservice.service;

import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.ExecutorService;

import jakarta.annotation.PostConstruct;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

@Service
public class MessageListenerService {

    private final KafkaConsumer<String, String> kafkaConsumer;

    private final MessageSenderService messageSenderService;

    private final ExecutorService executorService;

    private final TransactionTemplate transactionTemplate;

    private final Logger logger = LoggerFactory.getLogger(MessageListenerService.class);

    public MessageListenerService(KafkaConsumer<String, String> kafkaConsumer,
                                  MessageSenderService messageSenderService,
                                  ExecutorService executorService, TransactionTemplate transactionTemplate) {
        this.kafkaConsumer = kafkaConsumer;
        this.messageSenderService = messageSenderService;
        this.executorService = executorService;
        this.transactionTemplate = transactionTemplate;
    }

    @PostConstruct
    public void postConstruct() {
        executorService.submit(this::waitForMessages);
    }

    public void waitForMessages() {
        kafkaConsumer.subscribe(Collections.singletonList("train_manage_system"));
        try {
            while (true) {
                ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    transactionTemplate.execute(s -> {
                        System.out.println("Received message: " + record.value());
                        messageSenderService.processMessage(record.value());
                        throw new RuntimeException();
//                        return null;
                    });
                }
            }
        } catch (Exception e) {
            logger.error("Message listening stop due to error: " + e);
        }
    }
}
