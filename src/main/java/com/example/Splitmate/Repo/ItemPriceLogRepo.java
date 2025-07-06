package com.example.Splitmate.Repo;

import com.example.Splitmate.Entity.Consumer;
import com.example.Splitmate.Entity.ItemPriceLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemPriceLogRepo extends JpaRepository<ItemPriceLog,Long> {
}
