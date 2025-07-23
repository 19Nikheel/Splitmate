package com.example.Splitmate.Repo;

import com.example.Splitmate.Classbodies.AllUserDto;
import com.example.Splitmate.Classbodies.UserInfo;
import com.example.Splitmate.Entity.AcceptRequests;
import com.example.Splitmate.Entity.Groups;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AcceptRequestsRepo extends JpaRepository<AcceptRequests,Long> {
    boolean existsByGroupIdAndName(Groups user, String name);



//    @Query(value = "select a.name from accept_requests a where a.Id= :id",nativeQuery = true)
//    String findName(long id);

    @Query(value="Select * from accept_requests a where a.username= :name",nativeQuery = true)
    Optional<List<AcceptRequests>> findByUsername(@Param("name") String name);

    @Query(value="Select a.nam from accept_requests a where a.username= :name",nativeQuery = true)
    Optional<List<String>> findAllUser(@Param("name") String name);


   // Optional<AcceptRequests> findByUsername(MainUser i);

    //@Query(name="Select * from accept_requests a where a.name= :name",nativeQuery = true)
    //Optional<AcceptRequests> findByName(@Param("name") String name);
    Optional<AcceptRequests> findByUserId(String name);
   // Optional<AcceptRequests> findByUsernameAndNameId(MainUser user,String name);
    Optional<AcceptRequests> findByGroupIdAndName(Groups g, String name);

    @Query("SELECT new com.example.Splitmate.Classbodies.AllUserDto(u.name, " +
            "CONCAT('http://localhost:8080/images/', u.avatar), " +
            "(CASE WHEN ag.id IS NOT NULL THEN true ELSE false END)) " +
            "FROM AcceptRequests u " +
            "LEFT JOIN Admin_Group ag ON u.userId = ag.username.username AND ag.gid = :groupId " +
            "WHERE u.groupId.id = :groupId")
    List<AllUserDto> findUsersByGroupId(@Param("groupId") String groupId);

    @Query("select new com.example.Splitmate.Classbodies.UserInfo(a.Id,a.name,a.avatar) from AcceptRequests a where a.groupId= :id")
    Optional<List<UserInfo>> findAllById(@Param("id") Groups id);

    @Query(value = "select a.name from accept_requests a where a.group_id= :id",nativeQuery = true)
    Optional<List<String>> findAllUserById(@Param("id") long id);

//    @Query(value="select a.groupId from accept_requests a where a.userId= :userId")
//    List<Groups> findGroupId (@Param("userId") String userId);

    @Query("SELECT a.groupId FROM AcceptRequests a WHERE a.userId = :userId")
    List<Groups> findGroupsByUserId(@Param("userId") String userId);


}
