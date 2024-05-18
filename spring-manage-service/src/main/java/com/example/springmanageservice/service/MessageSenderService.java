package com.example.springmanageservice.service;

import com.example.springmanageservice.service.db.StationDbService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.stereotype.Service;

@Service
public class MessageSenderService {

    private final Producer<String, String> kafkaProducer;

    private final StationDbService stationDbService;

    private final ObjectMapper objectMapper;

    private final ProducerFactory<String, String> producerFactory;

    public MessageSenderService(StationDbService stationDbService,
                                ObjectMapper objectMapper,
                                ProducerFactory<String, String> producerFactory) {
        this.kafkaProducer = producerFactory.createProducer("tx-");
        this.stationDbService = stationDbService;
        this.objectMapper = objectMapper;
        this.producerFactory = producerFactory;
    }

    public void processMessage(String value) {
        kafkaProducer.beginTransaction();
        try {
            String message = "";
            if (value.equals("stations")) {
                try {
                    message = objectMapper.writeValueAsString(stationDbService.getStations());
                } catch (Exception e) {
                    throw new RuntimeException();
                }
            }
            throw new RuntimeException();
//            sendMessage(message);
//            kafkaProducer.commitTransaction();
        } catch (Exception e) {
            sendErrorMessage(e);
            kafkaProducer.commitTransaction();
            throw new RuntimeException();
        }
    }

    public void sendMessage(String message) {
        System.out.println("Send normal message");
        kafkaProducer.send(new ProducerRecord<>(
                "train_manage_system_result",
                "trains",
                message
        ));
    }

    public void sendErrorMessage(Exception e) {
        System.out.println("Send error message");
        kafkaProducer.send(new ProducerRecord<>(
                "train_manage_system_result",
                "ERROR",
                e.toString()
        ));
    }
}
