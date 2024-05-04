package com.itmo.blss.tms;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itmo.blss.model.db.Station;
import com.itmo.blss.service.StationDbService;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class StationJob {

    private final Producer<String, String> producer;

    private final KafkaConsumer<String, String> consumer;

    private final ObjectMapper objectMapper;

    private final StationDbService stationDbService;

    private final ExecutorService executorService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public StationJob(Producer<String, String> producer,
                      KafkaConsumer<String, String> consumer,
                      ObjectMapper objectMapper,
                      StationDbService stationDbService) {
        this.producer = producer;
        this.consumer = consumer;
        this.objectMapper = objectMapper;
        this.stationDbService = stationDbService;
        this.executorService = Executors.newFixedThreadPool(10);
        executorService.execute(this::getMessages);
        System.out.println(1);
    }

    // every ten minutes
    @Scheduled(cron = "0 */1 * * * ?")
    void action() {
        producer.send(new ProducerRecord<>("train_manage_system", "123", "123"));
        logger.info("Message was sent");
    }

    private void getMessages() {
        try {
            consumer.subscribe(List.of("train_manage_system_result"));
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    logger.info("Message was consumed");
                    Pair<String, String> message = Pair.of(record.key(),
                            record.value());
                    List<Station> stations;
                    try {
                        stations = objectMapper.readValue(message.getValue(), new TypeReference<>() {
                        });
                    } catch (Exception e) {
                        logger.error("Some errors:" + e);
                        continue;
                    }
                    stationDbService.upsertStations(stations);
                }
            }

        } catch (Exception e) {
            System.out.println("Exception occured: " + e);
        } finally {
            consumer.close(); // this will also commit the offsets if need be.
            System.out.println("The consumer is now gracefully closed.");
        }
        logger.info("Unexpected 123");
    }
}
