package com.example.Splitmate.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class PushRequests {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "Request" ,nullable=false)
    private String requestNames;

    @Column(name = "Last_Updated_Time" ,nullable=false)
    private String lastUpdatedTime;


    @ManyToOne
    @JsonIgnore
    @JoinColumn(name="username" ,referencedColumnName = "username")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private MainUser username;
 }
