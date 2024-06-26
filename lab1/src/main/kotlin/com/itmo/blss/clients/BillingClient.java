package com.itmo.blss.clients;

import java.util.concurrent.ExecutorService;

import com.itmo.blss.service.BillingService;
import kotlin.Pair;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.stereotype.Service;

@Service
public class BillingClient implements BillingService {

    private final Producer<String, String> billingProducer;

    private final KafkaConsumer<String, String> billingConsumer;

    private final Logger logger = LoggerFactory.getLogger(BillingClient.class);

    private final ExecutorService executorService;

    private final ProducerFactory<String, String> producerFactory;

    public BillingClient(KafkaConsumer<String, String> billingConsumer,
                         ExecutorService executorService, ProducerFactory<String, String> producerFactory) {
        this.producerFactory = producerFactory;
//        this.billingProducer = producerFactory.createProducer("billing-tx-");
        this.billingProducer = null;
        this.billingConsumer = billingConsumer;
        this.executorService = executorService;
    }

    @Override
    public void sendBillingInfo(int userId, double amount) {
        billingProducer.send(new ProducerRecord<>(
                "billing-transactions",
                String.valueOf(userId), "%d:%f".formatted(userId, amount)
        ));
    }

    @NotNull
    @Override
    public Pair<Integer, Double> getBillingInfo() {
        return null;
    }
}
