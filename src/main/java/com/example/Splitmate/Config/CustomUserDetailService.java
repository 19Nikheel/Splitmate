package com.example.Splitmate.Config;

import com.example.Splitmate.Entity.AcceptRequests;
import com.example.Splitmate.Entity.MainUser;
import com.example.Splitmate.Repo.AcceptRequestsRepo;
import com.example.Splitmate.Repo.MainuserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomUserDetailService implements UserDetailsService {
    @Autowired @Lazy
    private PasswordEncoder pse;
    @Autowired
    private MainuserRepo userRepo;
    @Autowired
    private AcceptRequestsRepo art;


    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        if(username.startsWith("@#")){

            username=username.substring(2);

            AcceptRequests usere = art.findByUserId(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            String encodedPassword = pse.encode(usere.getGroupId().getGroupId());

//            return org.springframework.security.core.userdetails.User
//                    .withUsername("@#"+usere.getUserId())
//                    .password(encodedPassword)
//                    .roles(usere.getRole())
//                    .
//                    .build();
            return CustomUserDetails.withUsername("@#"+usere.getUserId())
                    .password(encodedPassword)
                    .roles(usere.getRole())
                    .tokenId(usere.getTokenID())
                    .build();
        }else{
            Optional<MainUser> byUsername = userRepo.findByUsername(username);
            if(byUsername.isPresent()){
                MainUser user =byUsername.get();

                return CustomUserDetails
                        .withUsername(user.getUsername())
                        .password(user.getPassword())
                        .roles(user.getRole())
                        .tokenId(-1)
                        .build();
            }else{
                throw new UsernameNotFoundException("User not found");
            }


            
        }
        

        
    }

}

