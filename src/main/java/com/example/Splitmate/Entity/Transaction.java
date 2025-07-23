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
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Transaction_Id")
    private  long transaction_id;


    @ManyToOne
    @JsonIgnore
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name="log_id" ,referencedColumnName = "LogId")
    private Log logId;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name="payer_id" ,referencedColumnName = "Id")
    private AcceptRequests payerId;


    @Column(name = "Amount")
    private double amount;

}
