package com.example.Splitmate.Entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Table(name="Allreceipt")
public class Allreceipt {
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long Id;

    private String name;
    private String item;

    private double total;
    private double amount;

    @ManyToOne
    @JoinColumn(name="username" ,referencedColumnName = "username")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private MainUser username;
}
