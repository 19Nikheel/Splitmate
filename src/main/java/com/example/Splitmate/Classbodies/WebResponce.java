package com.example.Splitmate.Classbodies;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor


@Component
public class WebResponce {
    private Long id;
    private List<String> paidBy;
    private List<String> items;
    private List<String> participants;
    private String time;
    private double amount;
}
