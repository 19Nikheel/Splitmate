package com.example.Splitmate.Classbodies;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class SettleDto {



    private String to;
    private String from;
    private double amount;
    public SettleDto() {

    }

    public SettleDto( String to, String from, double amount) {
        this.to = to;
        this.from = from;
        this.amount = amount;
    }
}
