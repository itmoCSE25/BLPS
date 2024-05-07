package org.example.demo;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class BillingService implements BillingInterface {
    private final Producer<String, String> producer;
    private final DatabaseService databaseService;

    public BillingService(Producer<String, String> producer, DatabaseService databaseService) {
        this.producer = producer;
        this.databaseService = databaseService;
    }

    public BillingService() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:29092");
        props.put("key.serializer", StringSerializer.class.getName());
        props.put("value.serializer", StringSerializer.class.getName());
        this.producer = new org.apache.kafka.clients.producer.KafkaProducer<>(props);
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
            ProducerRecord<String, String> record = new ProducerRecord<>("billing-transactions-result", "subtract", transactionDetails);
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
            ProducerRecord<String, String> record = new ProducerRecord<>("billing-transactions-result", "add", transactionDetails);
            producer.send(record);
        }
    }

    private boolean validateTransaction(Integer userId, double amount) {
        if (!databaseService.isUserExists(userId)) {
            String errorMessage = "Транзакция для пользователя " + userId + " не выполнена: Пользователь не существует";
            ProducerRecord<String, String> record = new ProducerRecord<>("billing-errors", errorMessage);
            producer.send(record);
            return false;
        }
        if (amount < 0 && databaseService.getUserBalance(userId) < Math.abs(amount)) {
            String errorMessage = "Транзакция для пользователя " + userId + " не выполнена: Недостаточно средств";
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
