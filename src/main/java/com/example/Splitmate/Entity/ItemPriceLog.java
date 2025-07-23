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
public class ItemPriceLog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    private  long Id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name="item_id" ,referencedColumnName = "Item_id")
    private ItemTable itemId;

    @Column(name = "Unit_Price")
    private double unitPrice;

    @ManyToOne
    @JsonIgnore
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name="Log_id" ,referencedColumnName = "LogId")
    private Log logId;
}
