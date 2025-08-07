package com.example.Splitmate.Controller;

import com.example.Splitmate.Classbodies.*;
import com.example.Splitmate.Config.CustomUserDetailService;
import com.example.Splitmate.Config.CustomUserDetails;
import com.example.Splitmate.Entity.AcceptRequests;
import com.example.Splitmate.Entity.Groups;
import com.example.Splitmate.Entity.MainUser;

import com.example.Splitmate.Repo.AcceptRequestsRepo;
import com.example.Splitmate.Repo.GroupRepo;
import com.example.Splitmate.Repo.MainuserRepo;

import com.example.Splitmate.Security.JwtHelper;
import com.example.Splitmate.Services.Addingservices;
import com.sun.net.httpserver.Authenticator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;


import java.util.Optional;
import java.util.UUID;



@RestController
public class SecurityController {
    @Autowired
    private MainuserRepo userrepo;
    @Autowired
    private AcceptRequestsRepo arp;


    @Autowired
    private GroupRepo groupRepo;


    @Autowired
    private CustomUserDetailService userDetailsService;
    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private Addingservices ads;
    @Autowired
    private PasswordEncoder pse;


    @Transactional
    @PostMapping("/signup")
    public ResponseEntity<?> loadData(@RequestBody SignupPacket user){

        if(user==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("wrong sign up credentials");
        }
        if(user.getName().startsWith("@#")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("wrong name format");        }

        MainUser uload=new MainUser();
        uload.setUsername(user.getName().substring(0,4)+ads.createUserid());   // important not done
        uload.setName(user.getName());
        uload.setPassword(pse.encode(user.getPassword()));
        uload.setContactNo(user.getPhoneNo());
        uload.setRole("MEMBER");

        MainUser save = userrepo.save(uload);
        return ResponseEntity.ok().body(save.getUsername());
    }

    @Transactional
    @PostMapping("/signup-guest")
    public ResponseEntity<?> saveData(@RequestBody Reportbody user){

        if(user==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("wrong sign up credentials");
        }

        if(user.getName().startsWith("@#")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("wrong name format");        }

        UUID tempPass=UUID.randomUUID();
        MainUser uload=new MainUser();
        uload.setUsername(user.getName().substring(0,4)+ads.createUserid());   // important not done
        uload.setName(user.getName());
        uload.setPassword(pse.encode(tempPass.toString()));
        uload.setRole("GUEST_MEMBER");
        MainUser save = userrepo.save(uload);

        //this.doAuthenticate(save.getUsername(),tempPass.toString());
        CustomUserDetails userDetails=userDetailsService.loadUserByUsername(save.getUsername());

        String token=this.jwtHelper.generateToken(userDetails);
        JwtResponce responce=JwtResponce.builder().Jwttoken(token).username(userDetails.getUsername()).name(user.getName()).role("GUEST_MEMBER").build();
        return new ResponseEntity<>(responce,HttpStatus.OK);

    }


    private Logger logger= LoggerFactory.getLogger(SecurityController.class);

    @PostMapping("/login")
    public ResponseEntity<JwtResponce> login(@RequestBody Request jwtRequest){
        this.doAuthenticate(jwtRequest.getUserName(),jwtRequest.getPassword());
        CustomUserDetails userDetails=userDetailsService.loadUserByUsername(jwtRequest.getUserName());
        String token=this.jwtHelper.generateToken(userDetails);
        Optional<MainUser> byUsername = userrepo.findByUsername(userDetails.getUsername());

        JwtResponce responce=JwtResponce.builder().Jwttoken(token).username(userDetails.getUsername()).name(byUsername.get().getName()).role(byUsername.get().getRole()).build();
        return new ResponseEntity<>(responce,HttpStatus.OK);
    }


    @PostMapping("/login-user")
    public ResponseEntity<?> loginUser(@RequestBody LoginUser jwtRequest){


        this.doAuthenticate("@#"+jwtRequest.getName()+"+"+jwtRequest.getUsername(),jwtRequest.getUsername());

        CustomUserDetails userDetails=userDetailsService.loadUserByUsername("@#"+jwtRequest.getName()+"+"+jwtRequest.getUsername());
        Groups gid = groupRepo.findByGroupId(jwtRequest.getUsername()).orElseThrow(() -> new UsernameNotFoundException("group name not found"));
        Optional <AcceptRequests> art=arp.findByGroupIdAndName(gid,jwtRequest.getName());

        //Optional <AcceptRequests> art=arp.findByUsernameAndNameId(userrepo.findByUsername(jwtRequest.getUsername()).get(),jwtRequest.getName());

        if(art.isPresent() && art.get().isCheck()){

            art.get().setCheck(false);


            String token=this.jwtHelper.generateToken(userDetails);



            JwtResponce responce=JwtResponce.builder().Jwttoken(token).username(jwtRequest.getName()).name(jwtRequest.getName()).role("GUEST").build();
            arp.save(art.get());
            return new ResponseEntity<>(responce,HttpStatus.OK);
        }

        return ResponseEntity.status(HttpStatus.IM_USED).body("Token is issued");



    }

    @GetMapping("/check")
    public ResponseEntity<JwtResponce> check(){
        Object details = SecurityContextHolder.getContext().getAuthentication().getDetails();
        System.out.println(details);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/becomemember")
    public ResponseEntity<?> becomeMember(@RequestBody BecomeMemberDTO user){

        if(user==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("wrong sign up credentials");
        }
        try {
            String name = SecurityContextHolder.getContext().getAuthentication().getName();
            Optional<AcceptRequests> byUserId = arp.findByUserId(name);

            Optional<MainUser> byUsername = userrepo.findByUsername(byUserId.get().getUserId());
            byUsername.get().setContactNo(user.getPhoneNo());
            byUsername.get().setPassword(pse.encode(user.getPassword()));
            userrepo.save(byUsername.get());
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("wrong sign up credentials");
        }
        return ResponseEntity.ok().body("success");
    }

    @PostMapping("/forget")
    public ResponseEntity<?> forgetpassword(@RequestBody BecomeMemberDTO user){

        if(user==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("wrong phoneNo");
        }

        this.doAuthenticate("R#%0)"+user.getPassword(),user.getPhoneNo());
        CustomUserDetails userDetails=userDetailsService.loadUserByUsername(user.getPassword());
        String token=this.jwtHelper.generateTokenlimit(userDetails);
        Optional<MainUser> byUsername = userrepo.findByUsername(userDetails.getUsername());

        JwtResponce responce=JwtResponce.builder().Jwttoken(token).username(userDetails.getUsername()).name(byUsername.get().getName()).role(byUsername.get().getRole()).build();
        return new ResponseEntity<>(responce,HttpStatus.OK);
    }

    @PostMapping("/newpass")
    public ResponseEntity<?>  newPassword(@RequestBody Reportbody user){

        if(user==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("wrong phoneNo");
        }
        String id=SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<MainUser> byUsername = userrepo.findByUsername(id);
        byUsername.get().setPassword(pse.encode(user.getName()));
        userrepo.save(byUsername.get());
        return ResponseEntity.ok().body("Successfully changed");
    }



    private void doAuthenticate(String username,String password){
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(username,password);
        try {
            authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        }catch(BadCredentialsException e){
            throw new BadCredentialsException("Invalid username and password");
        }

    }


    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> exceptionHandler(BadCredentialsException ex){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credentials Invalid !!");
    }





}
