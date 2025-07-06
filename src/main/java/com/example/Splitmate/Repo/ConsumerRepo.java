package com.example.Splitmate.Repo;

import com.example.Splitmate.Entity.Consumer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsumerRepo extends JpaRepository<Consumer,Long> {

}
