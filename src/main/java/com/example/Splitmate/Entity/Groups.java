package com.example.Splitmate.Entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
        this.createTime = LocalDateTime.now().toString(); // Set current timestamp
    }
}
