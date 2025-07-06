package com.example.Splitmate.Controller;


import com.example.Splitmate.Classbodies.AllUserDto;
import com.example.Splitmate.Classbodies.ItemSubmissionRequest;
import com.example.Splitmate.Classbodies.WebResponce;
import com.example.Splitmate.Classbodies.userExpenses;
import com.example.Splitmate.Entity.*;
import com.example.Splitmate.Repo.*;
import com.example.Splitmate.Services.Addingservices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;


@org.springframework.web.bind.annotation.RestController
public class Controller {
    @Autowired
    private GroupRepo groupRepo;

    @Autowired
    private Admin_GRepo adgRepo;
    @Autowired
    private Addingservices  services;
    @Autowired
    private PushRequestRepo prr;

    @Autowired
    private MainuserRepo mur;

    @Autowired
    private AcceptRequestsRepo acceptRepo;

    @Autowired
    private GroupRepo gRepo;

//    @PostMapping("/post")
//    public ResponseEntity<String> addpost(@RequestBody ItemSubmissionRequest ISR){
//        try{
//
//            services.postData(ISR);
//            return ResponseEntity.ok("Data added successful");
//        }catch(RuntimeException e){
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Username not found");
//        }
//
//    }

//    @GetMapping("/findall")
//    public ResponseEntity<?> getAllDataInRaw(){
//
//        String username=SecurityContextHolder.getContext().getAuthentication().getName();
//
//        username=username.substring(username.indexOf("+")+1);
//
//        Optional<List<AcceptRequests>> all = acceptRepo.findByUsername(username);
//        if(all.isPresent() && all.get().size()==0){
//            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No data Available");
//        }
//        return  ResponseEntity.ok(all.get());
//    }

    @GetMapping("/findalluser/{gid}")
    public ResponseEntity<?> getAllUser(@PathVariable String gid){
        List<AllUserDto> usersByGroupId = acceptRepo.findUsersByGroupId(gid);
        System.out.println(usersByGroupId);

//
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//
//        Optional<List<String>> all = acceptRepo.findAllUser(username);
//        if(all.get().size()==0){
//            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No data Available");
//        }

        return  ResponseEntity.ok(usersByGroupId);
    }

//    @GetMapping("/findeach")
//    public ResponseEntity<?> getEachUserExpenses(@RequestParam("name") String name){
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        System.out.println("pop");
//        System.out.println(username+" "+name);
//        List<userExpenses> all = allreceiptRepo.findUserExpenses(username,name.toUpperCase());
//        if(all.size()==0){
//            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No data Available");
//        }
//
//        return  ResponseEntity.ok(all);
//    }

//    @GetMapping("/findsum")
//    public ResponseEntity<?> gettotalspend(){
//
//        double all = allreceiptRepo.findTotalSpend();
//        if(all==0.0){
//            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No data Available");
//        }
//
//        return  ResponseEntity.ok(all);
//    }

    @GetMapping("/findRequestList/{groupId}")
    public ResponseEntity<?> getRequestList(@PathVariable ("groupId") String groupId){
        Optional<List<PushRequests>> byUsername;
        Optional<Groups> byGroupId = groupRepo.findByGroupId(groupId);
        String name = SecurityContextHolder.getContext().getAuthentication().getName();

        if( byGroupId.isPresent() ){
            boolean check=adgRepo.existsByGidAndUsername(byGroupId.get().getId(),name);
            if(check){
                byUsername = prr.findByGroupId(byGroupId.get());
            }else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not Authorize to check");
            }

        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("groupId is  Wrong");
        }

        return ResponseEntity.ok(byUsername);

    }

    //@Transactional
    @GetMapping("/group")
    public ResponseEntity<?> createGroup(@RequestParam("groupName") String gname , Principal principal){

        String userid=principal.getName();
        Optional<MainUser> byUsername = mur.findByUsername(userid);
        if(byUsername.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Account is not eligible for this  action");
        }
        Groups gr=new Groups();
        gr.setName(gname);
        Groups save = gRepo.save(gr);
        gr.setGroupId(gname.substring(0,4)+save.getId());
        Groups save1 = gRepo.save(gr);

        Admin_Group admg=new Admin_Group();
        admg.setGid(save1.getId());
        admg.setUsername(byUsername.get());
        adgRepo.save(admg);

        AcceptRequests apt=new AcceptRequests();
        apt.setGroupId(save1);
        apt.setRole("ADMIN");
        apt.setCheck(true);
        apt.setName(byUsername.get().getName());
        apt.setUserId(byUsername.get().getUsername());

        acceptRepo.save(apt);


        return ResponseEntity.ok("successfully created"+ save1.getGroupId());
    }


}
