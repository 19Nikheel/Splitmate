package com.example.Splitmate.Services;

import com.example.Splitmate.Classbodies.ItemDTO;
import com.example.Splitmate.Classbodies.ItemPayerDTO;
import com.example.Splitmate.Classbodies.ItemSubmissionRequest;
import com.example.Splitmate.Entity.*;
import com.example.Splitmate.Repo.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class Addingservices {

    @Autowired
    private MainuserRepo mainuserRepo;
    //private AllreceiptRepo allreceiptRepo;


    @Autowired
    private AcceptRequestsRepo Arp;
    
    @Autowired
    private ItemTableRepo itemTableRepo;


    
 

//    @Autowired
//    public Addingservices(MainuserRepo mainuserRepo, AllreceiptRepo allreceiptRepo,AcceptRequestsRepo Arp) {
//        this.mainuserRepo = mainuserRepo;
//        this.allreceiptRepo = allreceiptRepo;
//        this.Arp=Arp;
//    }


//    @Transactional
//    public void postData(ItemSubmissionRequest ISR){
//
//
//        List<ItemDTO> items = ISR.getItems();
//
//       for(ItemDTO i:items){
//           ItemTable t1=new ItemTable();
//           t1.setItem_name(i.getName());
//           t1.setPrice(i.getUnitPrice());
//
//           t1.setUserid(Arp.findById(i.getOwnerId()).get());
//           ItemTable save = itemTableRepo.save(t1);
//
//           List<Long> consumers = i.getConsumers();
//           for(Long p: consumers){
//               ItemConsumer ic=new ItemConsumer();
//               ic.setConsumer(Arp.findById(p).get());
//               ic.setItem(save);
//               itemConRepo.save(ic);
//           }
//
//           List<ItemPayerDTO> payers = i.getPayers();
//           for(ItemPayerDTO p:payers){
//               Itempayer y1=new Itempayer();
//               y1.setItem(save);
//               y1.setAmount(p.getAmount());
//               y1.setPayer(Arp.findById(p.getUserId()).get());
//               itemPayRepo.save(y1);
//           }
//
//
//       }
//
//    }


    int attempts = 0;
    int maxAttempts = 100;

    public  String generateUniqueId() {
          final Random random = new Random();
        return String.valueOf(1 + random.nextInt(9000));
    }

    public String createUserid() {
        String uniqueId;

        do {
            uniqueId = this.generateUniqueId();
            attempts++;
            if (attempts >= maxAttempts) {
                throw new RuntimeException("Failed to generate a unique ID after Server is full now ");
            }
        } while (mainuserRepo.existsByUsername(uniqueId));

        return uniqueId;
    }

//    public  String Calculate(){
//
//    }




}
