package com.itmo.blss.clients;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.itmo.blss.service.BillingService;
import kotlin.Pair;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class BillingClient implements BillingService {

    private final Producer<String, String> billingProducer;

    private final KafkaConsumer<String, String> billingConsumer;

    private final Logger logger = LoggerFactory.getLogger(BillingClient.class);

    private final ExecutorService executorService = Executors.newFixedThreadPool(2);

    public BillingClient(Producer<String, String> billingProducer, KafkaConsumer<String, String> billingConsumer) {
        this.billingProducer = billingProducer;
        this.billingConsumer = billingConsumer;
//        sendBillingInfo(123, 123.123);
        try {
//            executorService.execute(this::initKafkaConsumer);
        }catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    @Override
    public void sendBillingInfo(int userId, double amount) {
        billingProducer.send(new ProducerRecord<>(
                "billing-transactions",
                String.valueOf(userId), String.valueOf(amount)
        ));
    }

    @NotNull
    @Override
    public Pair<Integer, Double> getBillingInfo() {
        return null;
    }

    private void initKafkaConsumer() {
        billingConsumer.subscribe(List.of("billing-transactions"));
        while (true) {
            ConsumerRecords<String, String> records = billingConsumer.poll(Duration.ofMillis(100));

            for (ConsumerRecord<String, String> record : records){
                logger.info("1");
                logger.info("Key: " + record.key() + ", Value: " + record.value());
                logger.info("Partition: " + record.partition() + ", Offset:" + record.offset());
            }
        }
    }
}
