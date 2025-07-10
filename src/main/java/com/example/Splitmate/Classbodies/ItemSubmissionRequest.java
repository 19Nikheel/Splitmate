package com.example.Splitmate.Classbodies;

import lombok.Data;

import java.util.List;

@Data
public class ItemSubmissionRequest {


        private String description;
        private String groupId;
        private double totalMoney;
        private double tax;
        private List<ItemPayerDTO> payers;
        private List<ItemDTO> items;

        // Getters and setters


}