package org.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BillingController {

    private final BillingService billingService;

    @Autowired
    public BillingController(BillingService billingService) {
        this.billingService = billingService;
    }

    @PostMapping("/addMoney")
    public String addMoney(@RequestBody Transaction transaction) {
        billingService.addMoney(transaction.getUserId(), transaction.getAmount());
        return "Money added successfully";
    }

    @PostMapping("/subtractMoney")
    public String subtractMoney(@RequestBody Transaction transaction) {
        billingService.subtractMoney(transaction.getUserId(), transaction.getAmount());
        return "Money subtracted successfully";
    }
}
