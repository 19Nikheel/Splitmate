package com.example.Splitmate.Repo;


import com.example.Splitmate.Entity.MainUser;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface MainuserRepo extends JpaRepository<MainUser,Long> {



    Optional<MainUser> findByUsername(String u);

    boolean existsByUsername(String username);

//    @Transactional
//    void deleteByTimeOfDeletionLessThanEqual(LocalDateTime time);



}

