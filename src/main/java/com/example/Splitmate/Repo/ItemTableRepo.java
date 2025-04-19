package com.example.Splitmate.Repo;

import com.example.Splitmate.Entity.ItemConsumer;
import com.example.Splitmate.Entity.ItemTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemTableRepo extends JpaRepository<ItemTable,Long> {
}
