package org.example.service;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public class ConsumerMessageService {

    private final KafkaConsumer<String, String> consumer;

    private final MessageSenderService messageSenderService;

    public ConsumerMessageService(KafkaConsumer<String, String> consumer, MessageSenderService messageSenderService) {
        this.consumer = consumer;
        this.messageSenderService = messageSenderService;
    }

    public void startConsumer() {
        acceptMessages();
    }

    private void acceptMessages() {
        try {
            consumer.subscribe(List.of("train_manage_system"));
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    Pair<String, String> message = Pair.of(record.key(), record.value());
                    System.out.println("Send message");
                    messageSenderService.sendMessage();
                }
            }

        } catch (Exception e) {
            System.out.println("Exception occurred: " + e);
        } finally {
            consumer.close(); // this will also commit the offsets if need be.
            System.out.println("The consumer is now gracefully closed.");
        }
    }
}
