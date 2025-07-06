package com.example.Splitmate.Classbodies;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AllUserDto {
    String name;
    String image;
    boolean isHost;
}
