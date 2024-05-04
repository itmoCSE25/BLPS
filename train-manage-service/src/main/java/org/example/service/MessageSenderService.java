package org.example.service;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.example.model.DbStation;

public class MessageSenderService {

    private final Producer<String, String> producer;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public MessageSenderService(Producer<String, String> producer) {
        this.producer = producer;
    }

    public void sendMessage() throws JsonProcessingException {
        producer.send(new ProducerRecord<>(
                "train_manage_system_result",
                "trains",
                objectMapper.writeValueAsString(List.of(
                        new DbStation(1, "Central Station"),
                        new DbStation(2, "East station"),
                        new DbStation(3, "East station"),
                        new DbStation(4, "South station"),
                        new DbStation(5, "West station"),
                        new DbStation(6, "Test station"),
                        new DbStation(10, "123123")

                ))
        ));
    }
}
