package com.example.Splitmate.Repo;

import com.example.Splitmate.Entity.RawData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RawRepo extends JpaRepository<RawData,Long> {

}
