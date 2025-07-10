package com.example.Splitmate.Entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "LogId")
    private  long logId;

    @Column(name = "Amount")
    private double totalAmount;

    @Column(name = "Description")
    private String Description;

    @Column(name = "Time")
    private String time;

    @Column(name = "Mode")
    private String mode;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name="GroupId" ,referencedColumnName = "Id")
    private Groups GroupId;
}
