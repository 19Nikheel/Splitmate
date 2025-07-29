package com.example.Splitmate.ServiceBody;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SettlementId implements Serializable {
    private Long from;
    private Long to;
    private Long groups;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SettlementId that = (SettlementId) o;
        return Objects.equals(from, that.from) && Objects.equals(to, that.to) && Objects.equals(groups, that.groups);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, groups);
    }
}

