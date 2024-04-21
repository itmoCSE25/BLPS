package org.example;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class BillingService implements BillingInterface{
    private Producer<String, String> producer;
    private DatabaseService databaseService;

    public BillingService(Producer<String, String> producer, DatabaseService databaseService) {
        this.producer = producer;
        this.databaseService = databaseService;
    }

    public BillingService() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        this.producer = new KafkaProducer<>(props);
        this.databaseService = new DatabaseService();
    }

    @Override
    public void subtractMoney(Integer userId, double amount) {
        if (!validateTransaction(userId, -amount)) {
            return;
        }

        boolean databaseResult = executeDatabaseUpdate(userId, -amount, "subtract");

        if (databaseResult) {
            String transactionDetails = userId + ":" + -amount;
            ProducerRecord<String, String> record = new ProducerRecord<>("billing-transactions", "subtract", transactionDetails);
            producer.send(record);
        }
    }

    @Override
    public void addMoney(Integer userId, double amount) {
        if (!validateTransaction(userId, amount)) {
            return;
        }

        boolean databaseResult = executeDatabaseUpdate(userId, amount, "add");

        if (databaseResult) {
            String transactionDetails = userId + ":" + amount;
            ProducerRecord<String, String> record = new ProducerRecord<>("billing-transactions", "add", transactionDetails);
            producer.send(record);
        }
    }

    private boolean validateTransaction(Integer userId, double amount) {
        if (!databaseService.isUserExists(userId)) {
            String errorMessage = "Transaction failed for user " + userId + ": User does not exist";
            ProducerRecord<String, String> record = new ProducerRecord<>("billing-errors", errorMessage);
            producer.send(record);
            return false;
        }
        if (amount < 0 && databaseService.getUserBalance(userId) < Math.abs(amount)) {
            String errorMessage = "Transaction failed for user " + userId + ": Insufficient funds";
            ProducerRecord<String, String> record = new ProducerRecord<>("billing-errors", errorMessage);
            producer.send(record);
            return false;
        }

        return true;
    }

    private boolean executeDatabaseUpdate(Integer userId, double amount, String operation) {
        return databaseService.updateUserBalance(userId, amount);
    }
}
