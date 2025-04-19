package com.example.Splitmate.Controller;

import com.example.Splitmate.Classbodies.*;
import com.example.Splitmate.Config.CustomUserDetailService;
import com.example.Splitmate.Entity.AcceptRequests;
import com.example.Splitmate.Entity.MainUser;

import com.example.Splitmate.Repo.AcceptRequestsRepo;
import com.example.Splitmate.Repo.MainuserRepo;

import com.example.Splitmate.Security.JwtHelper;
import com.example.Splitmate.Services.Addingservices;
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

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;


import java.time.Duration;

import java.util.Optional;
import java.util.UUID;

import static java.time.LocalTime.now;


@RestController
public class SecurityController {
    @Autowired
    private MainuserRepo userrepo;
    @Autowired
    private AcceptRequestsRepo arp;





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
        uload.setForTime(user.getForTime());
        uload.setRole("ADMIN");
        uload.setStatus("MEMBER");
        System.out.println(uload);
        MainUser save = userrepo.save(uload);
        return ResponseEntity.ok().body(save.getUsername());
    }

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
        uload.setForTime(user.getFortime());
        uload.setRole("ADMIN");
        uload.setStatus("GUEST");
        MainUser save = userrepo.save(uload);
        Request n=new Request(save.getUsername(),tempPass.toString());
        return new ResponseEntity<>(n,HttpStatus.OK);
    }


    private Logger logger= LoggerFactory.getLogger(SecurityController.class);

    @PostMapping("/login")
    private ResponseEntity<JwtResponce> login(@RequestBody Request jwtRequest){
        this.doAuthenticate(jwtRequest.getUserName(),jwtRequest.getPassword());
        UserDetails userDetails=userDetailsService.loadUserByUsername(jwtRequest.getUserName());

        String token=this.jwtHelper.generateToken(userDetails,userrepo.findByUsername(jwtRequest.getUserName()).get().getForTime());
        JwtResponce responce=JwtResponce.builder().Jwttoken(token).username(userDetails.getUsername()).build();
        MainUser mainUser = userrepo.findByUsername(userDetails.getUsername()).get();
        AcceptRequests ar=new AcceptRequests();
        ar.setNameId(mainUser.getName());
        ar.setRole("Main");
        ar.setCheck(false);
        ar.setUsername(mainUser);
        ar.setName(mainUser.getName()+"+"+mainUser.getUsername());
        arp.save(ar);
        return new ResponseEntity<>(responce,HttpStatus.OK);
    }

    @PostMapping("/guest-login")
    private ResponseEntity<JwtResponce> guestLogin(@RequestBody Request jwtRequest){
        this.doAuthenticate(jwtRequest.getUserName(),jwtRequest.getPassword());
        UserDetails userDetails=userDetailsService.loadUserByUsername(jwtRequest.getUserName());

        String token=this.jwtHelper.generateToken(userDetails,userrepo.findByUsername(jwtRequest.getUserName()).get().getForTime());
        JwtResponce responce=JwtResponce.builder().Jwttoken(token).username(userDetails.getUsername()).build();
        MainUser mainUser = userrepo.findByUsername(userDetails.getUsername()).get();
        AcceptRequests ar=new AcceptRequests();
        ar.setNameId(mainUser.getName());
        ar.setRole("Main");
        ar.setCheck(false);
        ar.setUsername(mainUser);
        ar.setName(mainUser.getName()+"+"+mainUser.getUsername());
        arp.save(ar);
        return new ResponseEntity<>(responce,HttpStatus.OK);
    }

    @PostMapping("/login-user")
    private ResponseEntity<?> loginUser(@RequestBody LoginUser jwtRequest){


        this.doAuthenticate("@#"+jwtRequest.getName()+"+"+jwtRequest.getUsername(),jwtRequest.getUsername());

        UserDetails userDetails=userDetailsService.loadUserByUsername("@#"+jwtRequest.getName()+"+"+jwtRequest.getUsername());



        Optional <AcceptRequests> art=arp.findByUsernameAndNameId(userrepo.findByUsername(jwtRequest.getUsername()).get(),jwtRequest.getName());

        if(art.isPresent() && art.get().isCheck()){

            art.get().setCheck(false);


            String token=this.jwtHelper.generateToken(userDetails, (int)Duration.between(now(),userrepo.findByUsername(jwtRequest.getUsername())
                    .get().getTimeOfDeletion()).toHours()+2);


            JwtResponce responce=JwtResponce.builder().Jwttoken(token).username(jwtRequest.getName()).build();
            arp.save(art.get());
            return new ResponseEntity<>(responce,HttpStatus.OK);
        }

        return ResponseEntity.status(HttpStatus.IM_USED).body("Token is issued");



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
    public String exceptionHandler(){
        return "Credentials Invalid !!";
    }





}
