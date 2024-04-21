package org.example;

public interface BillingInterface {
    void subtractMoney(Integer userId, double amount);
    void addMoney(Integer userId, double amount);
}
