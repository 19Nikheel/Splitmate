package com.example.Splitmate.ServiceBody;

import com.example.Splitmate.Entity.AcceptRequests;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Expense {
    private long id;                     // id of the expens
    private double amount;                // Total amount of the expense
    private AcceptRequests payer;                   // User who paid the expense
    private List<AcceptRequests> participants;      // List of users sharing the expense
    private Map<AcceptRequests, Double> shares;     // Split amounts owed by each participant

    private long logId;
    private long groupId;
    // Constructor to initialize Expense attributes

}
