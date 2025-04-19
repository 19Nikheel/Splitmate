package com.example.Splitmate.Entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AcceptRequests {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;

    @Column(name = "nam")
    private String nameId;

    @Column(unique = true)
    private String name;
    private boolean isCheck;
    private String role;

    @ManyToOne
    @JoinColumn(name="username" ,referencedColumnName = "username")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private MainUser username;

}
