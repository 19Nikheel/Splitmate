package com.example.Splitmate.Repo;

import com.example.Splitmate.Entity.Consumer;
import com.example.Splitmate.Entity.Log;
import com.example.Splitmate.Entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TransactionRepo extends JpaRepository<Transaction,Long> {
    List<Transaction> findByLogId(Log log);

    @Query("select a.payerId.name from Transaction a where a.logId = :lg")
    List<String> findByLog(@Param("lg") Log lg);

}
