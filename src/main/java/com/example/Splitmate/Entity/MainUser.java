package com.example.Splitmate.Entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Table(name="User_details")
public class MainUser {



    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long Idm;
    private String name;

    @Column(unique = true ,nullable = false)
    private String username;

    private String password;

    private String contactNo;

    @Column(name= "ForTime" ,nullable=false)
    private int forTime;

    private String role;

    private String status;

    private LocalDateTime timeOfCreation;

    private LocalDateTime timeOfDeletion;


    @PrePersist
    public void prePersist() {
        this.timeOfCreation = LocalDateTime.now(); // Set current timestamp
        this.timeOfDeletion = this.timeOfCreation.plusHours(forTime); // Calculate deletion time
    }
}
