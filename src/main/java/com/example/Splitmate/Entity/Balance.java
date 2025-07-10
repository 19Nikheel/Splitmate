package com.example.Splitmate.Entity;

import com.example.Splitmate.ServiceBody.UserPair;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@IdClass(UserPair.class)
@Table(name = "balances")
public class Balance {

    @Id
    private long user1Id;

    @Id
    private long user2Id;

    private double balance;




    @ManyToOne
    @JsonIgnore
    @JoinColumn(name="GroupId" ,referencedColumnName = "Id")
    private Groups groupId;

}
