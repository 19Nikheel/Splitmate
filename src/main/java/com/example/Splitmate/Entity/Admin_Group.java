package com.example.Splitmate.Entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Data
@NoArgsConstructor
public class Admin_Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="Group_id")
    private long gid;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name="username" ,referencedColumnName = "username")
    private MainUser username;
}
