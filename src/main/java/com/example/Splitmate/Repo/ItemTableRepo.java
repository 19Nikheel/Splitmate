package com.example.Splitmate.Repo;


import com.example.Splitmate.Entity.ItemTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemTableRepo extends JpaRepository<ItemTable,Long> {
}
