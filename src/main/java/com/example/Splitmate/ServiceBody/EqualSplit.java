package com.example.Splitmate.ServiceBody;

import com.example.Splitmate.Entity.AcceptRequests;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EqualSplit implements Split{
    @Override
    public Map<AcceptRequests, Double> calculateSplit(double amount, List<AcceptRequests> participants, Map<String, Object> splitDetails) {
        double amountPerPerson = amount / participants.size(); // Divide the amount equally among all participants
        Map<AcceptRequests, Double> splits = new HashMap<>(); // Map to hold the calculated split
        for (AcceptRequests user : participants) {
            splits.put(user, amountPerPerson); // Assign each participant the equal amount
        }
        return splits;
    }
}
