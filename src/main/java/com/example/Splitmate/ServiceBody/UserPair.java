package com.example.Splitmate.ServiceBody;

import com.example.Splitmate.Entity.AcceptRequests;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class UserPair implements Serializable {

    private long user1Id;

    private long user2Id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPair userPair = (UserPair) o;
        return user1Id == userPair.user1Id && user2Id == userPair.user2Id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(user1Id, user2Id);
    }




}