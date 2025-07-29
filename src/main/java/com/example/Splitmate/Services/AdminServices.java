package com.example.Splitmate.Services;

import com.example.Splitmate.Classbodies.GroupDetailsDTO;
import com.example.Splitmate.Entity.AcceptRequests;
import com.example.Splitmate.Entity.Admin_Group;
import com.example.Splitmate.Entity.Groups;
import com.example.Splitmate.Entity.MainUser;
import com.example.Splitmate.Repo.AcceptRequestsRepo;
import com.example.Splitmate.Repo.Admin_GRepo;
import com.example.Splitmate.Repo.GroupRepo;
import com.example.Splitmate.Repo.MainuserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class AdminServices {


    @Autowired
    private AcceptRequestsRepo acceptRepo;
    @Autowired
    private Admin_GRepo AdminRepo;

    @Autowired
    private MainuserRepo mainuserRepo;

    @Autowired
    private PasswordEncoder pse;
    @Autowired
    private GroupRepo grp;
    public List<GroupDetailsDTO> myGroup(String name){
        MainUser user = mainuserRepo.findByUsername(name).orElseThrow(() -> new RuntimeException("user not found"));
        List<Admin_Group> byUsername = AdminRepo.findByUsername(user);
        List<GroupDetailsDTO> gdl=new ArrayList<>();
        for(Admin_Group i: byUsername){
            Groups gr=grp.findById(i.getGid()).get();
            GroupDetailsDTO gd=new GroupDetailsDTO();
            gd.setGroupId(i.getGid());
            gd.setGroupName(gr.getName());
            gd.setDateOfCreation(gr.getCreateTime());
            gd.setAdminList(AdminRepo.findNamesByGroupId(i.getGid()));
            gdl.add(gd);
        }

        return gdl;
    }

    public List<GroupDetailsDTO> otherGroup(String name){
        List<Groups> groupId = acceptRepo.findGroupsByUserId(name);

        List<GroupDetailsDTO> gdl=new ArrayList<>();
        for(Groups i: groupId){

            GroupDetailsDTO gd=new GroupDetailsDTO();
            gd.setGroupId(i.getId());
            gd.setGroupName(i.getName());
            gd.setDateOfCreation(i.getCreateTime());
            gd.setAdminList(AdminRepo.findNamesByGroupId(i.getId()));
            gdl.add(gd);
        }

        return gdl;
    }

    public void change(String user,String pass){
        Optional<MainUser> byUsername = mainuserRepo.findByUsername(user);
        byUsername.get().setPassword(pse.encode(pass));
        mainuserRepo.save(byUsername.get());
    }


}
