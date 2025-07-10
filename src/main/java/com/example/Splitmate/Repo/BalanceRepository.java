package com.example.Splitmate.Repo;

import com.example.Splitmate.Entity.Balance;
import com.example.Splitmate.Entity.Groups;
import com.example.Splitmate.ServiceBody.UserPair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface BalanceRepository  extends JpaRepository<Balance, UserPair> {
    Optional<Balance> findByUser1IdAndUser2Id(Long user1Id, Long user2Id);

    Optional<Balance> findByUser1IdAndUser2IdAndGroupId(Long user1Id, Long user2Id, Groups groupId);

    Optional<List<Balance>> findByGroupId(Groups g);
}
