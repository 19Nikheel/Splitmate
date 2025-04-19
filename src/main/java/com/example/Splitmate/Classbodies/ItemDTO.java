package com.example.Splitmate.Classbodies;

import lombok.Data;

import java.util.List;

@Data
public class ItemDTO {
    private String name;
    private Double unitPrice;
    private Long ownerId;
    private List<Long> consumers;
    private List<ItemPayerDTO> payers;
}