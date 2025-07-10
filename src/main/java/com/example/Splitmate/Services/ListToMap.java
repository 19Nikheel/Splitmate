package com.example.Splitmate.Services;

import com.example.Splitmate.Entity.Balance;
import com.example.Splitmate.Entity.Groups;
import com.example.Splitmate.ServiceBody.UserPair;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ListToMap {

    public Map<UserPair,Double> convertToMap(List<Balance> bl){
        Map<UserPair, Double> balanceMap = new HashMap<>();

        for (Balance b : bl) {
            UserPair user=new UserPair(b.getUser1Id(),b.getUser2Id());
            balanceMap.put(user, b.getBalance());
        }
        return balanceMap;
    }
}
