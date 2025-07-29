package com.example.Splitmate.Entity;

import com.example.Splitmate.Entity.AcceptRequests;
import com.example.Splitmate.ServiceBody.SettlementId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@IdClass(SettlementId.class)
public class Settlement {

    @Id
    @ManyToOne
    @JoinColumn(name="from_user_id" ,referencedColumnName = "Id")
    private AcceptRequests from;

    @Id
    @ManyToOne
    @JoinColumn(name="to_user_id" ,referencedColumnName = "Id")// User who owes the money
    private AcceptRequests to;


    // User who is owed the money

    @Id
    @ManyToOne
    @JoinColumn(name="GroupId" ,referencedColumnName = "Id")
    private Groups groups;


    private double amount;    // Transaction amount

    // Constructor to initialize Transaction attributes
    public Settlement(AcceptRequests from, AcceptRequests to,Groups g, double amount) {
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.groups=g;
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
