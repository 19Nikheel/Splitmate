package com.example.Splitmate.Repo;

import com.example.Splitmate.Classbodies.ResponceItemDTO;
import com.example.Splitmate.Entity.Consumer;
import com.example.Splitmate.Entity.ItemPriceLog;
import com.example.Splitmate.Entity.ItemTable;
import com.example.Splitmate.Entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemPriceLogRepo extends JpaRepository<ItemPriceLog,Long> {
    List<ItemPriceLog> findByLogId(Log log);


//    @Query("SELECT new com.example.Splitmate.Classbodies.ResponceItemDTO(it.itemId.item_name, it.unitPrice) " +
//            "FROM ItemPriceLog it " +
//            "WHERE it.logId = :ld")
//    List<ResponceItemDTO> findAllItems(@Param("ld") Log ld);


    @Query("SELECT it.itemId.item_name " +
            "FROM ItemPriceLog it " +
            "WHERE it.logId = :logId")
    List<String> findItemNamesByLogId(@Param("logId") Log logId);



}
