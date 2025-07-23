package com.example.Splitmate.Repo;

import com.example.Splitmate.Entity.Consumer;
import com.example.Splitmate.Entity.Groups;
import com.example.Splitmate.Entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LogRepo extends JpaRepository<Log,Long> {

    @Query(value = "select sum(totalAmount) from Log  where groupId= :gid and deleted=false")
    Double findSum(@Param("gid") Groups gid);

     List<Log> findByGroupId(Groups g);
}
