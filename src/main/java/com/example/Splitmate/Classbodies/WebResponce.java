package com.example.Splitmate.Classbodies;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor


@Component
public class WebResponce {
    private String name;
    private  String item;
    private double amount;
    private  String username;
}
