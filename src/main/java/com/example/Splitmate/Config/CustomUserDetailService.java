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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        if(username.startsWith("@#")){

            username=username.substring(2);

            AcceptRequests usere = art.findByName(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            String encodedPassword = pse.encode(usere.getUsername().getUsername());

            return org.springframework.security.core.userdetails.User
                    .withUsername("@#"+usere.getName())
                    .password(encodedPassword)
                    .roles(usere.getRole())
                    .build();
        }else{
            Optional<MainUser> byUsername = userRepo.findByUsername(username);
            if(byUsername.isPresent()){
                MainUser user =byUsername.get();

                return org.springframework.security.core.userdetails.User
                        .withUsername(user.getUsername())
                        .password(user.getPassword())
                        .roles(user.getRole())
                        .build();
            }else{
                throw new UsernameNotFoundException("User not found");
            }


            
        }
        

        
    }

}

