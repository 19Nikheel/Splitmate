package com.example.Splitmate.Controller;


import com.example.Splitmate.Classbodies.ItemSubmissionRequest;
import com.example.Splitmate.Classbodies.WebResponce;
import com.example.Splitmate.Classbodies.userExpenses;
import com.example.Splitmate.Entity.AcceptRequests;
import com.example.Splitmate.Entity.MainUser;
import com.example.Splitmate.Entity.PushRequests;
import com.example.Splitmate.Repo.AcceptRequestsRepo;
import com.example.Splitmate.Repo.AllreceiptRepo;
import com.example.Splitmate.Repo.MainuserRepo;
import com.example.Splitmate.Repo.PushRequestRepo;
import com.example.Splitmate.Services.Addingservices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@org.springframework.web.bind.annotation.RestController
public class Controller {


    @Autowired
    private Addingservices  services;
    @Autowired
    private PushRequestRepo prr;

    @Autowired
    private MainuserRepo mur;

    @Autowired
    private AllreceiptRepo allreceiptRepo;

    @Autowired
    private AcceptRequestsRepo acceptRepo;

    @PostMapping("/post")
    public ResponseEntity<String> addpost(@RequestBody ItemSubmissionRequest ISR){
        try{

            services.postData(ISR);
            return ResponseEntity.ok("Data added successful");
        }catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Username not found");
        }

    }

    @GetMapping("/findall")
    public ResponseEntity<?> getAllDataInRaw(){

        String username=SecurityContextHolder.getContext().getAuthentication().getName();

        username=username.substring(username.indexOf("+")+1);

        Optional<List<AcceptRequests>> all = acceptRepo.findByUsername(username);
        if(all.isPresent() && all.get().size()==0){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No data Available");
        }
        return  ResponseEntity.ok(all.get());
    }

    @GetMapping("/findalluser")
    public ResponseEntity<?> getAllUser(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<List<String>> all = acceptRepo.findAllUser(username);
        if(all.get().size()==0){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No data Available");
        }

        return  ResponseEntity.ok(all);
    }

    @GetMapping("/findeach")
    public ResponseEntity<?> getEachUserExpenses(@RequestParam("name") String name){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("pop");
        System.out.println(username+" "+name);
        List<userExpenses> all = allreceiptRepo.findUserExpenses(username,name.toUpperCase());
        if(all.size()==0){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No data Available");
        }

        return  ResponseEntity.ok(all);
    }

    @GetMapping("/findsum")
    public ResponseEntity<?> gettotalspend(){

        double all = allreceiptRepo.findTotalSpend();
        if(all==0.0){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No data Available");
        }

        return  ResponseEntity.ok(all);
    }

    @GetMapping("/findRequestList/{Username}")
    public ResponseEntity<?> getRequestList(@PathVariable ("Username") String username){
        Optional<List<PushRequests>> byUsername;
        Optional<MainUser> byU1 = mur.findByUsername(username);
        if(byU1.isPresent()){
            byUsername = prr.findByUsername(byU1.get());
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username is  Wrong");
        }

        return ResponseEntity.ok(byUsername);

    }


}
