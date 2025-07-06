package com.example.Splitmate.Repo;


import com.example.Splitmate.Entity.Groups;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface GroupRepo  extends JpaRepository<Groups,Long> {
    Optional<Groups> findByGroupId(String u);
}
