package com.example.Splitmate.Classbodies;

import lombok.Data;

import java.util.List;

@Data
public class ItemSubmissionRequest2 {


        private String description;
        private long id;
        private long groupId;
        private double totalMoney;
        private double tax;
        private List<ItemPayerDTO> payers;
        private List<ItemDTO> items;

        // Getters and setters


}