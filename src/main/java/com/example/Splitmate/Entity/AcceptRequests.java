package com.example.Splitmate.Entity;


import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AcceptRequests {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private long Id;

    @Column(name = "name")
    private String name;

    @Column(name ="User_Id")
    private String userId;
    private boolean isCheck;
    private String role;
    private String avatar;
    private int tokenID;

    @ManyToOne
    @JoinColumn(name="groupId" ,referencedColumnName = "id")
    private Groups groupId;

}
