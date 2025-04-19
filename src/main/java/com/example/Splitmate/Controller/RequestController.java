package com.example.Splitmate.Controller;

import com.example.Splitmate.Entity.AcceptRequests;
import com.example.Splitmate.Entity.MainUser;
import com.example.Splitmate.Entity.PushRequests;
import com.example.Splitmate.Repo.AcceptRequestsRepo;
import com.example.Splitmate.Repo.MainuserRepo;
import com.example.Splitmate.Repo.PushRequestRepo;
import org.hibernate.JDBCException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.Optional;

@RestController
public class RequestController {

    @Autowired
    private PushRequestRepo requestRepo;
    @Autowired
    private AcceptRequestsRepo art;
    @Autowired
    private MainuserRepo mur;



    @GetMapping("/invite-request/{username}")
    public ResponseEntity<?> checklink(@PathVariable("username") String uname){
        String name;
        Optional<MainUser> byUsername = mur.findByUsername(uname);
        if(byUsername.isPresent() ){
            name=byUsername.get().getUsername();
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invite id is not correct");
        }
        return ResponseEntity.ok().body(name);
    }

    @PostMapping("/push-request")
    private ResponseEntity<String> sendRequest(@RequestParam("username") String username , @RequestParam ("name") String name){

        if(art.existsByUsernameAndName(mur.findByUsername(username).get(),name)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Name already exist!");  ///important
        }
        PushRequests rq=new PushRequests();
        rq.setRequestNames(name);
        Optional<MainUser> byUsername = mur.findByUsername(username);
        if(!byUsername.isPresent() ) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Username not found");
        }
        rq.setUsername(byUsername.get());
        rq.setLastUpdatedTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
        requestRepo.save(rq);
        return ResponseEntity.ok().body("Request sent");
    }



    @PostMapping("/accept-request")
    private ResponseEntity<String> cnf_request(@RequestParam ("username") String username ,@RequestParam ("name") String name){

        AcceptRequests ar=new AcceptRequests();
        Optional<MainUser> byUsername = mur.findByUsername(username);
        if(!byUsername.isPresent() ) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Username not found");
        }
        ar.setUsername(byUsername.get());
        ar.setName(name+"+"+username);
        ar.setNameId(name);
        ar.setRole("USER");
        ar.setCheck(true);
        try {
            art.save(ar);

            requestRepo.deleteByRequestNamesAndUsername(name,username);

            return ResponseEntity.ok("Successfully Request Accepted");
        }catch(DataIntegrityViolationException  e ){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Name is already available");
        }catch (Exception e){
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error in delete request");
        }

    }

    @PutMapping("/realive-auth")
    public ResponseEntity<String> realiveRequest(@RequestParam("username") String username,@RequestParam("name") String name){
        MainUser user= mur.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("user name not found"));
        AcceptRequests found = art.findByUsernameAndNameId(user,name).orElseThrow(() -> new UsernameNotFoundException("user name not found"));
        found.setCheck(true);
        art.save(found);
        return ResponseEntity.ok("Successful");
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> responseException(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("user name not found");
    }

}
