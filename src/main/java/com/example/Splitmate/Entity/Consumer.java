package com.example.Splitmate.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Data
@Entity
@NoArgsConstructor
public class Consumer {

    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    private  long Id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name="user_id" ,referencedColumnName = "Id")
    private AcceptRequests userId;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name="item_id" ,referencedColumnName = "Id")
    private ItemPriceLog itemId;

    @Column(name = "Quantity")
    private int quantity;

    @Column(name="Shared")
    private boolean isShared;

}
