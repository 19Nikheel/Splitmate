package com.example.Splitmate.Controller;


import com.example.Splitmate.Classbodies.*;
import com.example.Splitmate.Entity.*;
import com.example.Splitmate.Repo.*;
import com.example.Splitmate.ServiceBody.Transaction;
import com.example.Splitmate.Services.Addingservices;
import com.example.Splitmate.Services.AdminServices;
import com.example.Splitmate.Services.BalanceSheetService;
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
    private LogRepo logRepo;

    @Autowired
    private AcceptRequestsRepo acceptRepo;

    @Autowired
    private GroupRepo gRepo;

    @Autowired
    private BalanceSheetService bss;

    @Autowired
    private AdminServices adminServices;



    @PostMapping("/post")
    public ResponseEntity<String> addpost(@RequestBody ItemSubmissionRequest ISR){
        try{
            services.postData(ISR);

            return ResponseEntity.ok("Data added successful");
        }catch(RuntimeException e){
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Username not found");
        }

    }

    @GetMapping("/findalluser/{gid}")
    public ResponseEntity<?> getAllDataInRaw(@PathVariable ("gid") long gid){

        Groups g1 = gRepo.findById(gid).orElseThrow(() -> new RuntimeException("Group not exist"));

        Optional<List<UserInfo>> allById = acceptRepo.findAllById(g1);

        if(allById.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No data Available");
        }
        return  ResponseEntity.ok(allById.get());
    }

    @GetMapping("/findsettlement/{gid}")
    public ResponseEntity<?> getSettlement(@PathVariable ("gid") long gid){

        Groups g1 = gRepo.findById(gid).orElseThrow(() -> new RuntimeException("Group not exist"));

        List<Transaction> groupSimplifiedSettlements = bss.getGroupSimplifiedSettlements(g1);

//        Optional<List<String>> allById = acceptRepo.findAllById(g1.getId());
//
//        if(allById.isEmpty()){
//            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No data Available");
//        }
        return  ResponseEntity.ok(groupSimplifiedSettlements);
    }



    @GetMapping("/findeach")
    public ResponseEntity<?> getEachUserExpenses(@RequestParam("groupId") long groupId,@RequestParam("name") String name){
        Groups g1 = gRepo.findById(groupId).orElseThrow(() -> new RuntimeException("Group not exist"));
        AcceptRequests found = acceptRepo.findByGroupIdAndName(g1, name).orElseThrow(() -> new RuntimeException("User not found"));


        return  ResponseEntity.ok(bss.getGroupTotalBalance(found,g1));
    }



    @GetMapping("/findRequestList/{groupId}")
    public ResponseEntity<?> getRequestList(@PathVariable ("groupId") long groupId){
        Optional<List<PushRequests>> byUsername;
        Optional<Groups> byGroupId = groupRepo.findById(groupId);
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
    @PostMapping("/group")
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


        return ResponseEntity.ok("successfully created "+ save1.getGroupId());
    }

    @GetMapping("/findgroupname/{groupId}")
    public ResponseEntity<?> getGroupName(@PathVariable ("groupId") long groupId){
        Optional<Groups> byGroupId = groupRepo.findById(groupId);

        if( byGroupId.isPresent() ){
            return ResponseEntity.ok().body(byGroupId.get().getName());
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group Not Found");
        }
    }

    @DeleteMapping("/delete/{logid}")
    public ResponseEntity<?> deleteLogId(@PathVariable ("logid") long logid){
        Optional<Log> byId = logRepo.findById(logid);
        byId.get().setDeleted(true);

        if(byId.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("log id not found");

        }
        try{
            services.deleteLog(byId.get());
            logRepo.save(byId.get());
        }catch(Exception e){
            System.out.println(e);      // remind me change
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Deletion Unsuccessful");
         }
        return ResponseEntity.ok().body("Deleted Successfully ");
    }

    @GetMapping("/findtotal/{gid}")
    public ResponseEntity<?> getTotal(@PathVariable ("gid") long gid){
        Optional<Groups> byGroupId = groupRepo.findById(gid);

        if( byGroupId.isPresent() ){
            Double sum = logRepo.findSum(byGroupId.get());
            sum = (sum==null)?0.0 : sum;
            return ResponseEntity.ok().body(sum);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group Not Found");
        }
    }

    @GetMapping("/findmygroups")
    public ResponseEntity<?> findMyGroups(){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();

        try{
            List<GroupDetailsDTO> groupDetailsDTOS = adminServices.myGroup(name);
            return ResponseEntity.ok(groupDetailsDTOS);
        }catch (Exception e){
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("group not found");
        }


    }

    @GetMapping("/findothergroups")
    public ResponseEntity<?> findotherGroups(){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();

        try{
            List<GroupDetailsDTO> groupDetailsDTOS = adminServices.otherGroup(name);
            return ResponseEntity.ok(groupDetailsDTOS);
        }catch (Exception e){
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("group not found");
        }


    }

    @GetMapping("/findlog/{gid}")
    public ResponseEntity<?> getLog(@PathVariable ("gid") long gid){
        Optional<Groups> byGroupId = groupRepo.findById(gid);

        if( byGroupId.isPresent() ){
            List<WebResponce> gLog = services.getGLog(byGroupId.get());
            return ResponseEntity.ok().body(gLog);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("log Not Found");
        }
    }

}
