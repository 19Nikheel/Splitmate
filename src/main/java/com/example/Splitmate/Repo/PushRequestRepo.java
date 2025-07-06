package com.example.Splitmate.Repo;


import com.example.Splitmate.Entity.Groups;
import com.example.Splitmate.Entity.MainUser;
import com.example.Splitmate.Entity.PushRequests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface PushRequestRepo extends JpaRepository<PushRequests,Long> {
    //boolean existsByUsername(String username);
    //Optional<List<PushRequests>> findByUsername (MainUser user);
    Optional<List<PushRequests>> findByGroupId (Groups user);


    @Modifying
    @Transactional
    @Query(value=" DELETE FROM push_requests pr" +
            "    USING groups gr" +
            "    WHERE pr.group_id = gr.id" +
            "    AND pr.name= :name" +
            "    AND gr.group_id= :username", nativeQuery = true)
    void deleteByNameAndGroupId(@Param("name") String name, @Param("username") String username);



}
