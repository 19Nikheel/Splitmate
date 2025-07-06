package com.example.Splitmate.Classbodies;

import lombok.Data;

import java.util.List;

@Data
public class ItemSubmissionRequest {
    private Double paid;
    private List<ItemDTO> items;
}