package com.example.Splitmate.ServiceBody;

import com.example.Splitmate.Entity.AcceptRequests;

import java.util.List;
import java.util.Map;

interface Split {
    /**
     - Calculates the split for the given amount among participants based on specific split details.
     -
     - @param amount        The total amount to split.
     - @param participants  The list of users participating in the split.
     - @param splitDetails  Additional details required for the specific split type.
     - @return A map where the key is the User and the value is the amount they owe.
     */
    Map<AcceptRequests, Double> calculateSplit(double amount, List<AcceptRequests> participants, Map<String, Object> splitDetails);
}