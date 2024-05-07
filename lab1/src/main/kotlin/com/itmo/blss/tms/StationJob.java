package com.itmo.blss.tms;

import java.time.Duration;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itmo.blss.model.db.Station;
import com.itmo.blss.service.StationDbService;
import jakarta.transaction.TransactionManager;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Service
public class StationJob {

    private final ConsumerFactory<String, String> consumerFactory;

    private final Producer<String, String> producer;

    private final Consumer<String, String> consumer;

    private final ObjectMapper objectMapper;

    private final StationDbService stationDbService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final TransactionTemplate transactionTemplate;

    private final JtaTransactionManager jtaTransactionManager;
    private final TransactionManager transactionManager;

    public StationJob(ConsumerFactory<String, String> consumerFactory, Producer<String, String> producer,
                      KafkaConsumer<String, String> consumer,
                      ObjectMapper objectMapper,
                      StationDbService stationDbService, TransactionTemplate transactionTemplate,
                      JtaTransactionManager transactionManager) {
        this.consumerFactory = consumerFactory;
        this.producer = producer;
        this.consumer = consumerFactory.createConsumer();
        this.objectMapper = objectMapper;
        this.stationDbService = stationDbService;
        this.transactionTemplate = transactionTemplate;
        this.jtaTransactionManager = transactionManager;
        this.transactionManager = jtaTransactionManager.getTransactionManager();
    }

    // every ten minutes
    @Scheduled(cron = "0 */1 * * * ?")
    void action() {
        producer.send(new ProducerRecord<>("train_manage_system", "key", "stations"));
        stationDbService.clearStations();
        try {
            transactionTemplate.execute(s -> {
                getMessages();
                return null;
            });
        }catch (Exception e) {
            logger.error("Transaction was rolled back: " + e);
        }
    }

    private void getMessages() {
        try {
            boolean poolingFlag = true;
            consumer.subscribe(List.of("train_manage_system_result"));
            while (poolingFlag) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    logger.info("Message was consumed");
                    Pair<String, String> message = Pair.of(record.key(), record.value());
                    List<Station> stations;
                    try {
                        stations = objectMapper.readValue(message.getValue(), new TypeReference<>() {
                        });
                    } catch (Exception e) {
                        logger.error("Some errors:" + e);
                        continue;
                    }
                    stationDbService.upsertStations(stations);
                    poolingFlag = false;
                }
            }
        } catch (Exception e) {
            System.out.println("Exception occured: " + e);
        }
        logger.info("Job success");
    }
}