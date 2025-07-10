package com.example.Splitmate.Services;

import com.example.Splitmate.Classbodies.ConsumerDTO;
import com.example.Splitmate.Classbodies.ItemDTO;
import com.example.Splitmate.Classbodies.ItemPayerDTO;
import com.example.Splitmate.Classbodies.ItemSubmissionRequest;
import com.example.Splitmate.Entity.*;
import com.example.Splitmate.Repo.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;
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
    private GroupRepo grp;
    
    @Autowired
    private ItemTableRepo itemTableRepo;

    @Autowired
    private LogRepo logRepo;

    @Autowired
    private ItemPriceLogRepo iplRepo;

    @Autowired
    private ConsumerRepo consumerRepo;

    @Autowired
    private TransactionRepo transactionRepo;

    @Autowired
    private BalanceSheetService bcSheet;


    @Transactional
    public void postData(ItemSubmissionRequest ISR){

        Log newLog=new Log();
        newLog.setTotalAmount(ISR.getTotalMoney());
        newLog.setDescription(ISR.getDescription());
        newLog.setTime(new Date().toString());
        String str=(ISR.getPayers().size()==1) ? "Full" : "Partial";
        newLog.setMode(str);
        Groups groupidId = grp.findByGroupId(ISR.getGroupId()).orElseThrow(() -> new RuntimeException("Groupid is wrong"));
        newLog.setGroupId(groupidId);
        Log logSave = logRepo.save(newLog);

        List<ItemDTO> items = ISR.getItems();
        List<ItemPayerDTO> payers = ISR.getPayers();


       for(ItemDTO i:items){
           ItemTable it=new ItemTable();
           it.setItem_name(i.getItemName());
           ItemTable itemSave = itemTableRepo.save(it);

           ItemPriceLog ipl=new ItemPriceLog();
           ipl.setItemId(itemSave);
           ipl.setLogId(logSave);
           ipl.setUnitPrice(i.getUnitPrice());
           ItemPriceLog saveipl = iplRepo.save(ipl);

           for(ConsumerDTO j: i.getConsumers()){
               Consumer con=new Consumer();
               AcceptRequests byGroupIdAndName = Arp.findByGroupIdAndName(groupidId, j.getName()).orElseThrow(()-> new RuntimeException("invalid user"));
               con.setUserId(byGroupIdAndName);
               con.setIsShared(j.getIsShared());
               con.setQuantity(j.getQuantity());
               con.setItemId(saveipl);
               consumerRepo.save(con);
           }
       }

       for(ItemPayerDTO i: ISR.getPayers()){
           Transaction transaction=new Transaction();
           transaction.setLogId(logSave);
           AcceptRequests byGroupIdAndName = Arp.findByGroupIdAndName(groupidId, i.getName()).orElseThrow(()-> new RuntimeException("invalid user"));
           transaction.setPayerId(byGroupIdAndName);
           transaction.setAmount(i.getAmount());
           transactionRepo.save(transaction);
       }


        bcSheet.updateBalancesFromExpense(ISR,groupidId);
    }


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






}
