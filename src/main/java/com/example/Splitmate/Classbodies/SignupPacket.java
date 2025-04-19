package com.example.Splitmate.Classbodies;

import lombok.*;


@Getter
@Setter

@ToString
@AllArgsConstructor
@NoArgsConstructor

public class SignupPacket {
    private String name;
    private String password;
    private String phoneNo;
    private int forTime;
}
