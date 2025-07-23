package com.example.Splitmate.Entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Data
@NoArgsConstructor
public class Groups {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="Id")
    private long id;

    @Column(name="GroupId")
    private String groupId;

    @Column(name="Group_Name" , nullable = false)
    private String name;

    @Column(name="Create_time")
    private String createTime;


    @PrePersist
    public void prePersist() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.createTime = LocalDateTime.now().format(formatter); // Set current timestamp
    }
}
