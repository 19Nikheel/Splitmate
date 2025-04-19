package com.example.Splitmate.Classbodies;


import lombok.*;
import org.springframework.stereotype.Component;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder


public class LoginUser {
    private String name;
    private String username;
}
