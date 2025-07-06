package com.example.Splitmate.Repo;

import com.example.Splitmate.Entity.Consumer;
import com.example.Splitmate.Entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepo extends JpaRepository<Log,Long> {
}
