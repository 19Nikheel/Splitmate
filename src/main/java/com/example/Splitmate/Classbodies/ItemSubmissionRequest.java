package com.example.Splitmate.Classbodies;

import lombok.Data;

import java.util.List;

@Data
public class ItemSubmissionRequest {
    private List<ItemDTO> items;
}