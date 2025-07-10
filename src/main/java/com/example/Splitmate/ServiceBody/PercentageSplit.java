package com.example.Splitmate.ServiceBody;

import com.example.Splitmate.Entity.AcceptRequests;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PercentageSplit implements Split{
    @Override
    public Map<AcceptRequests, Double> calculateSplit(double amount, List<AcceptRequests> participants, Map<String, Object> splitDetails) {
        Map<AcceptRequests, Double> percentages = (Map<AcceptRequests, Double>) splitDetails.get("percentages");
        Map<AcceptRequests, Double> splits = new HashMap<>(); // Map to hold the calculated split


        for (AcceptRequests user : participants) {
            double percentage = percentages.getOrDefault(user, 0.0); // Get the percentage for the user
            splits.put(user, amount * percentage / 100.0); // Calculate the share based on the percentage
        }
        return splits;
    }
}
