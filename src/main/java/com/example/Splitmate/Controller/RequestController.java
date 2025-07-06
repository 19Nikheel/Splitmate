package com.example.Splitmate.Controller;

import com.example.Splitmate.Entity.AcceptRequests;
import com.example.Splitmate.Entity.Groups;
import com.example.Splitmate.Entity.MainUser;
import com.example.Splitmate.Entity.PushRequests;
import com.example.Splitmate.Repo.AcceptRequestsRepo;
import com.example.Splitmate.Repo.GroupRepo;
import com.example.Splitmate.Repo.MainuserRepo;
import com.example.Splitmate.Repo.PushRequestRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    @Autowired
    private GroupRepo groupRepo;




//    //Wrong
//    @GetMapping("/invite-request/{groupId}")
//    public ResponseEntity<?> checklink(@PathVariable("groupId") String groupId){
//        String Id;
//        Optional<Group> byUsername = groupRepo.findByUsername(groupId);
//        if(byUsername.isPresent() ){
//            Id=byUsername.get().getGroupId();
//        }else{
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invite id is not correct");
//        }
//        return ResponseEntity.ok().body(Id);
//    }


    @PostMapping("/push-request")
    public ResponseEntity<String> sendRequest(@RequestParam("groupId") String groupId , @RequestParam ("name") String name){
        Optional<Groups> byGroupId = groupRepo.findByGroupId(groupId);
        if(byGroupId.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group not exist!");  ///important
        }

        if(art.existsByGroupIdAndName(byGroupId.get(),name)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Name already exist!");  ///important
        }

        PushRequests rq=new PushRequests();
                                                                                //Think again  conflict
        rq.setName(name);
        rq.setGroupId(byGroupId.get());
        rq.setLastUpdatedTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
        requestRepo.save(rq);
        return ResponseEntity.ok().body("Request sent");
    }




    @PostMapping("/accept-request")
    @Transactional
    public ResponseEntity<String> cnf_request(@RequestParam ("groupId") String gid ,@RequestParam ("name") String name){

        AcceptRequests ar=new AcceptRequests();
        Optional<MainUser> byUsername = mur.findByUsername(name);
        Optional<Groups> byGroupId = groupRepo.findByGroupId(gid);

        if(byGroupId.isEmpty() ) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("group not found");
        }
        ar.setGroupId(byGroupId.get());
        ar.setUserId(name+"+"+gid);

        if(byUsername.isPresent()){
            ar.setName(byUsername.get().getName());    ///  Conflict point when member vs guest name clash
            ar.setRole("MEMBER");
            ar.setTokenID(-1);
        }else{
            ar.setName(name);
            ar.setRole("GUEST");
            ar.setTokenID(1);
        }

        ar.setCheck(true);
        try {
            art.save(ar);

            requestRepo.deleteByNameAndGroupId(name,gid);

            return ResponseEntity.ok("Successfully Request Accepted");
        }catch(DataIntegrityViolationException  e ){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Name is already available");
        }catch (Exception e){
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error in delete request");
        }

    }

    @PutMapping("/realive-auth")
    public ResponseEntity<String> realiveRequest(@RequestParam("groupId") String groupId,@RequestParam("name") String name){
        Groups gid = groupRepo.findByGroupId(groupId).orElseThrow(() -> new UsernameNotFoundException("group name not found"));
        AcceptRequests found = art.findByGroupIdAndName(gid,name).orElseThrow(() -> new UsernameNotFoundException("user name not found"));
        found.setCheck(true);
        found.setTokenID((found.getTokenID()+17)%Integer.MAX_VALUE);
        art.save(found);
        return ResponseEntity.ok("Successful");
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> responseException(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("user name not found");
    }

}
