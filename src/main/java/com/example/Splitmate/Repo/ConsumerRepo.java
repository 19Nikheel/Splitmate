package com.example.Splitmate.Repo;

import com.example.Splitmate.Classbodies.ParticipantDTO;
import com.example.Splitmate.Entity.Consumer;
import com.example.Splitmate.Entity.ItemPriceLog;
import com.example.Splitmate.Entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsumerRepo extends JpaRepository<Consumer,Long> {

    List<Consumer> findByItemId(ItemPriceLog it);

//    @Query("SELECT new com.example.Splitmate.Classbodies.ParticipantDTO(c.userId.name, c.amount) " +
//            "FROM Consumer c " +
//            "WHERE c.itemPriceLog.logId = :ld")
//    List<ParticipantDTO> findAllParticipants(@Param("ld") Log ld);

    @Query("SELECT ar.name " +
            "FROM AcceptRequests ar " +
            "WHERE ar.id IN ( " +
            "  SELECT DISTINCT c.userId.id " +
            "  FROM Consumer c " +
            "  JOIN c.itemId ipl " +
            "  WHERE ipl.logId = :logId " +
            ")")
    List<String> findNamesByLogId(@Param("logId") Log logId);



}
