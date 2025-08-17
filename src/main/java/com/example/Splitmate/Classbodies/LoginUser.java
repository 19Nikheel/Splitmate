package com.example.Splitmate.Classbodies;


import lombok.*;
import org.springframework.stereotype.Component;


@NoArgsConstructor
@AllArgsConstructor
@Builder


public class LoginUser {
    private String name;
    private String userName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
