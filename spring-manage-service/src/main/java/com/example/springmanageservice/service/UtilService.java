package com.example.springmanageservice.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UtilService {

    private final MessageSenderService messageSenderService;

    private final Logger logger = LoggerFactory.getLogger(UtilService.class);

    public UtilService(MessageSenderService messageSenderService) {
        this.messageSenderService = messageSenderService;
    }

    @Transactional
    public void processMessage(ConsumerRecord<String, String> record) {
        System.out.println("Received message: " + record.value());
        messageSenderService.processMessage(record.value());
    }
}
