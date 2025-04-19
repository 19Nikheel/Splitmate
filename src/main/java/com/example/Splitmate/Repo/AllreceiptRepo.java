package com.example.Splitmate.Repo;

import com.example.Splitmate.Classbodies.WebResponce;
import com.example.Splitmate.Classbodies.userExpenses;
import com.example.Splitmate.Entity.Allreceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AllreceiptRepo extends JpaRepository<Allreceipt,Long> {
    @Query(value="SELECT DISTINCT a.name FROM Allreceipt a WHERE a.username = username",
  nativeQuery = true)
    List<String> findDistinctUsernamesExcluding(@Param("username") String username);

    @Query(
            value="SELECT  a.name, a.item , a.amount FROM Allreceipt a WHERE a.name = :name AND a.username = :username",
            nativeQuery = true)
    List<userExpenses> findUserExpenses(@Param("username") String username, @Param("name") String name);


    @Query(value="SELECT  a.name, a.item , a.amount ,u.username FROM Allreceipt a JOIN user_details u ON a.username = u.username",
            nativeQuery = true)
    List<WebResponce> findall();


    @Query(value="SELECT  SUM(a.amount) FROM Allreceipt a ",
            nativeQuery = true)
    double findTotalSpend();

   // boolean existsByUsernameAndName(String uname,String name);
}
