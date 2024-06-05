package com.itmo.blss.tms;

import java.time.Duration;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itmo.blss.model.db.Station;
import com.itmo.blss.service.StationDbService;
import com.itmo.blss.service.impl.MasterDbService;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

@Service
public class StationJob {

    private final Producer<String, String> producer;

    private final Consumer<String, String> consumer;

    private final ObjectMapper objectMapper;

    private final StationDbService stationDbService;

    private final TransactionTemplate transactionTemplate;

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final MasterDbService masterDbService;

    private final ProducerFactory<String, String> producerFactory;

    public StationJob(ConsumerFactory<String, String> consumerFactory,
                      ObjectMapper objectMapper,
                      StationDbService stationDbService, TransactionTemplate transactionTemplate,
                      KafkaTemplate<String, String> kafkaTemplate, MasterDbService masterDbService,
                      ProducerFactory<String, String> producerFactory) {
//        this.producer = producerFactory.createProducer("pr-tx-1-");
        this.producer = null;
        this.consumer = consumerFactory.createConsumer();
        this.objectMapper = objectMapper;
        this.stationDbService = stationDbService;
        this.transactionTemplate = transactionTemplate;
        this.kafkaTemplate = kafkaTemplate;
        this.masterDbService = masterDbService;
        this.producerFactory = producerFactory;
        logger.info("DONE");
    }

    // every ten minutes
//    @Scheduled(cron = "0 */1 * * * ?")
    @Transactional
    void action() {
        try {
            producer.send(new ProducerRecord<>("train_manage_system", "key", "stations"));
            stationDbService.clearStations();
            getMessages();
        } catch (Exception e) {
            logger.error("Transaction was rolled back: " + e);
            producer.abortTransaction();
            throw new RuntimeException();
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
                        if (message.getKey().equals("ERROR")) {
                            throw new Exception();
                        }
                        stations = objectMapper.readValue(
                                message.getValue(),
                                new TypeReference<>() {
                                }
                        );
                        stationDbService.upsertStations(stations);
                    } catch (Exception e) {
                        logger.error("Some errors:" + e);
                    }
                    poolingFlag = false;
                }
            }
        } catch (Exception e) {
            System.out.println("Exception occured: " + e);
        }
        logger.info("Job success");
    }
}
