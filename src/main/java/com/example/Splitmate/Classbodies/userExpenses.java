package com.example.Splitmate.Classbodies;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder


public class userExpenses {
    private String name;
    private String item;
    private int amount;
}
