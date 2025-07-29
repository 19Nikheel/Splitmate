package com.example.Splitmate.Repo;

import com.example.Splitmate.Classbodies.SettleDto;
import com.example.Splitmate.Entity.Groups;
import com.example.Splitmate.Entity.Settlement;
import com.example.Splitmate.Entity.Transaction;
import com.example.Splitmate.ServiceBody.SettlementId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SettlementRepository extends JpaRepository<Settlement, SettlementId> {
    @Transactional
    @Modifying
    @Query("UPDATE Settlement s SET s.amount = :amount WHERE s.from.id = :fromUserId AND s.to.id = :toUserId AND s.groups.id = :groupId")
    void updateAmount(@Param("fromUserId") Long fromUserId,
                      @Param("toUserId") Long toUserId,
                      @Param("groupId") Long groupId,
                      @Param("amount") Double amount);


    @Query("SELECT new com.example.Splitmate.Classbodies.SettleDto(t.to.name, t.from.name, t.amount) " +
            "FROM Settlement t WHERE t.groups = :group")
    List<SettleDto> findByGroups(@Param("group") Groups group);
}
