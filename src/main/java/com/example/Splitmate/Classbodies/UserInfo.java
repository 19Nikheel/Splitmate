package com.example.Splitmate.Classbodies;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserInfo {
    private long id;
    private String name;
    private String image;
    private boolean isHost;
    public UserInfo(long id, String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }



}
