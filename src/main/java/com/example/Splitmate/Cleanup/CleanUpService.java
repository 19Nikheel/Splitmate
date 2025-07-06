package com.example.Splitmate.Cleanup;

import com.example.Splitmate.Repo.MainuserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@EnableScheduling
public class CleanUpService {


    @Autowired
    private MainuserRepo userrepo;


   // @Scheduled(fixedRate= 2 * 60 *60 *1000)
//    public void cleaner(){
//        LocalDateTime current=LocalDateTime.now();
//        userrepo.deleteByTimeOfDeletionLessThanEqual(current);
//    }

}
