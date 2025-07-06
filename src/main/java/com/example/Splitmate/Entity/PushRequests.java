package com.example.Splitmate.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class PushRequests {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "UserId" )
    private String userId;

    @Column(name = "Name" ,nullable=false)
    private String name;

    @Column(name = "Last_Updated_Time" ,nullable=false)
    private String lastUpdatedTime;


    @ManyToOne
    @JsonIgnore
    @JoinColumn(name="GroupId" ,referencedColumnName = "Id")
//    @OnDelete(action = OnDeleteAction.CASCADE)
    private Groups groupId;

 }
