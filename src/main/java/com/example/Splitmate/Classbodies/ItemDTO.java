package com.example.Splitmate.Classbodies;

import lombok.Data;

import java.util.List;

@Data
public class ItemDTO {
    private String itemName;
    private double unitPrice;
    private int quantity;
    private List<ConsumerDTO> consumers;
}