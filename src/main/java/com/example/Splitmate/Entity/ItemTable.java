package com.example.Splitmate.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Table(name="Item")
public class ItemTable {
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long Id;

    @Column(name = "Item_name")
    private String item_name;

    @Column(name = "Price")
    private double price;

    @ManyToOne
    @JoinColumn(name="userid" ,referencedColumnName = "Id")
    private AcceptRequests userid;
}

