package com.example.Splitmate.ServiceBody;

import com.example.Splitmate.Entity.AcceptRequests;

public class Transaction {
    private AcceptRequests from;        // User who owes the money
    private AcceptRequests to;          // User who is owed the money
    private double amount;    // Transaction amount

    // Constructor to initialize Transaction attributes
    public Transaction(AcceptRequests from, AcceptRequests to, double amount) {
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    // Getters for the transaction attributes
    public AcceptRequests getFrom() {
        return from;
    }
    public AcceptRequests getTo() {
        return to;
    }
    public double getAmount() {
        return amount;
    }
}
