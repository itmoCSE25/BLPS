package com.example.springmanageservice.service;

import com.example.springmanageservice.service.db.StationDbService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;

@Service
public class MessageSenderService {

    private final Producer<String, String> kafkaProducer;

    private final StationDbService stationDbService;

    private final ObjectMapper objectMapper;

    public MessageSenderService(Producer<String, String> kafkaProducer, StationDbService stationDbService,
                                ObjectMapper objectMapper) {
        this.kafkaProducer = kafkaProducer;
        this.stationDbService = stationDbService;
        this.objectMapper = objectMapper;
    }

    public void processMessage(String value) {
        String message = "";
        if (value.equals("stations")) {
            try {
                message = objectMapper.writeValueAsString(stationDbService.getStations());
            }catch (Exception e) {
                throw new RuntimeException();
            }
        }
        sendMessage(message);
    }

    public void sendMessage(String message) {
        kafkaProducer.send(new ProducerRecord<>(
                "train_manage_system_result",
                "trains",
                message
        ));
    }
}
