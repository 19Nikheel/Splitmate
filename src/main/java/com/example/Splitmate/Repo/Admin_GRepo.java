package com.example.Splitmate.Repo;

import com.example.Splitmate.Entity.Admin_Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface Admin_GRepo extends JpaRepository<Admin_Group,Long> {

    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END FROM admin_group WHERE Group_id = :gid AND username = :username", nativeQuery = true)
    boolean existsByGidAndUsername(@Param("gid") long gid, @Param("username") String username);

}
