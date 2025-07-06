package com.example.Splitmate.Classbodies;


import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponce {
    private String Jwttoken;
    private String name;
    private String role;
    private String username;
}
