package com.example.Splitmate.Services;

import com.example.Splitmate.Classbodies.ConsumerDTO;
import com.example.Splitmate.Classbodies.ItemDTO;
import com.example.Splitmate.Classbodies.ItemPayerDTO;
import com.example.Splitmate.Classbodies.ItemSubmissionRequest;
import com.example.Splitmate.Entity.AcceptRequests;
import com.example.Splitmate.Entity.Balance;
import com.example.Splitmate.Entity.Groups;
import com.example.Splitmate.Repo.AcceptRequestsRepo;
import com.example.Splitmate.Repo.BalanceRepository;
import com.example.Splitmate.Repo.GroupRepo;
import com.example.Splitmate.Repo.LogRepo;
import com.example.Splitmate.ServiceBody.Transaction;
import com.example.Splitmate.ServiceBody.UserPair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class BalanceSheetService {

    @Autowired
    private BalanceRepository balanceRepository;

    @Autowired
    private LogRepo lgp;

    @Autowired
    private GroupRepo grp;

    @Autowired
    private ListToMap lToM ;
    @Autowired
    private AcceptRequestsRepo ArtRepo;
    /**
     * Updates balances when an expense is added or updated.
     */
    @Transactional
    public void updateBalancesFromExpense(ItemSubmissionRequest expenseJson, Groups groupEntity) {
        // Step 1: Build consumer total share map
        Map<AcceptRequests, Double> consumerShare = new HashMap<>();

        for (ItemDTO item : expenseJson.getItems()) {
            double unitPrice = item.getUnitPrice();

            for (ConsumerDTO consumer : item.getConsumers()) {
                AcceptRequests consumerUser = ArtRepo.findByGroupIdAndName(groupEntity,consumer.getName()).get();
                int quantity = consumer.getQuantity();
                int isShared = consumer.getIsShared();

                double cost;
                if (isShared > 0) {
                    cost = (unitPrice * quantity) / isShared;
                } else {
                    cost = unitPrice * quantity;
                }

                consumerShare.put(consumerUser, consumerShare.getOrDefault(consumerUser, 0.0) + cost);
            }
        }

        // Step 2: Distribute tax evenly among consumers
        double tax = expenseJson.getTax();
        int totalConsumers = consumerShare.size();
        double taxPerConsumer = totalConsumers > 0 ? tax / totalConsumers : 0;

        for (Map.Entry<AcceptRequests, Double> entry : consumerShare.entrySet()) {
            consumerShare.put(entry.getKey(), entry.getValue() + taxPerConsumer);
        }

        // Step 3: Build payer paid map
        Map<AcceptRequests, Double> payerPaid = new HashMap<>();
        for (ItemPayerDTO payer : expenseJson.getPayers()) {

            AcceptRequests payerUser = ArtRepo.findByGroupIdAndName(groupEntity,payer.getName()).get();

            payerPaid.put(payerUser, payer.getAmount());
        }

        double totalMoney = expenseJson.getTotalMoney();

        // Step 4: Update or insert balances
        for (Map.Entry<AcceptRequests, Double> entry : consumerShare.entrySet()) {
            AcceptRequests consumer = entry.getKey();
            double shareAmount = entry.getValue();

            for (Map.Entry<AcceptRequests, Double> payerEntry : payerPaid.entrySet()) {
                AcceptRequests payer = payerEntry.getKey();
                double payerAmount = payerEntry.getValue();

                if (consumer.equals(payer)) continue;

                double payerProportion = payerAmount / totalMoney;
                double owedAmount = shareAmount * payerProportion;

                // Try to find existing Balance
                Optional<Balance> existingBalanceOpt = balanceRepository.findByUser1IdAndUser2IdAndGroupId(consumer.getId(), payer.getId(), groupEntity);

                Balance balance;
                if (existingBalanceOpt.isPresent()) {
                    // Update existing balance
                    balance = existingBalanceOpt.get();
                    balance.setBalance(balance.getBalance() + owedAmount);
                } else {

                    // Create new balance\

                    balance = new Balance();
                    balance.setUser1Id(consumer.getId());
                    balance.setUser2Id(payer.getId());
                    balance.setBalance(owedAmount);
                    balance.setGroupId(groupEntity);
                }

                // Save or update
                balanceRepository.save(balance);
            }
        }
    }


    public List<Transaction> getGroupSimplifiedSettlements(Groups groupId) {
        Optional<List<Balance>> byGroupId = balanceRepository.findByGroupId(groupId);

        if(byGroupId.isEmpty()){
            throw new RuntimeException("invalid Group id");
        }
        return  calculateSettlements(lToM.convertToMap(byGroupId.get()));
    }


    public double getGroupTotalBalance(AcceptRequests user, Groups groupId) {
        // Return 0 if the group doesn't exist in our records
        Optional<List<Balance>> byGroupId = balanceRepository.findByGroupId(groupId);

        if(byGroupId.isEmpty()){
            throw new RuntimeException("invalid Group id");
        }

        double total = 0.0;
        Map<UserPair, Double> groupBalanceMap =  lToM.convertToMap(byGroupId.get());

        for (Map.Entry<UserPair, Double> entry : groupBalanceMap.entrySet()) {
            UserPair pair = entry.getKey();
            double amount = entry.getValue();

            if (pair.getUser1Id()==(user.getId())) {
                total -= amount; // Money owed by the user
            } else if (pair.getUser2Id()==(user.getId())) {
                total += amount; // Money owed to the user
            }
        }

        return total;
    }


    public double getGroupBalance(AcceptRequests user1, AcceptRequests user2, Groups groupId) {
        // Return 0 if the group doesn't exist in our records
        Optional<List<Balance>> byGroupId = balanceRepository.findByGroupId(groupId);

        if(byGroupId.isEmpty()){
            throw new RuntimeException("invalid Group id");
        }

        Map<UserPair, Double> groupBalanceMap = lToM.convertToMap(byGroupId.get());
        UserPair pair1 = new UserPair(user1.getId(), user2.getId());
        UserPair pair2 = new UserPair(user2.getId(), user1.getId());

        double balance1 = groupBalanceMap.getOrDefault(pair1, 0.0);
        double balance2 = groupBalanceMap.getOrDefault(pair2, 0.0);

        return balance1 - balance2;
    }

    public int getOptimalMinimumSettlements(Map<UserPair, Double> balances) {
        // Step 1: Calculate net balances for each user
        Map<AcceptRequests, Double> netBalances = new HashMap<>();
        for (Map.Entry<UserPair, Double> entry : balances.entrySet()) {
            UserPair pair = entry.getKey();
            double amount = entry.getValue();
            AcceptRequests debtor = ArtRepo.findById(pair.getUser1Id()).orElseThrow(); // The user who owes money
            AcceptRequests creditor = ArtRepo.findById(pair.getUser2Id()).orElseThrow(); // The user who is owed money
            // Update the net balance of each user
            netBalances.put(debtor, netBalances.getOrDefault(debtor, 0.0) - amount);
            netBalances.put(creditor, netBalances.getOrDefault(creditor, 0.0) + amount);
        }
        List<Double> creditList = new ArrayList<>();


        for (Map.Entry<AcceptRequests, Double> entry : netBalances.entrySet()) {
            if (Math.abs(entry.getValue()) > 0.001) { // Ignore near-zero balances
                creditList.add(entry.getValue()); // Store the net balance
            }
        }
        // Step 3: Apply Dynamic Programming to find the minimum transactions required
        int n = creditList.size(); // Number of users with non-zero balance
        int[] dp = new int[1 << n]; // DP array for memoization
        Arrays.fill(dp, -1);
        dp[0] = 0; // Base case: No users left means zero transactions


        // Find the maximum number of fully settled subgroups using DFS + DP
        int maxSubGroups = dfs((1 << n) - 1, dp, creditList);

        // Minimum transactions needed = Total users - Maximum fully settled groups
        return n - maxSubGroups;
    }
    /**
     - Helper method to calculate the sum of balances in a subset, given by a bitmask.
     -
     - @param values The list of credit balances.
     - @param mask The bitmask representing a subset of users.
     - @return The sum of balances in the subset.
     */
    public double sumOfMask(List<Double> values, int mask) {
        double sum = 0;
        for (int i = 0; i < values.size(); i++) {
            if ((mask & (1 << i)) != 0) { // Check if the i-th bit is set in the mask
                sum += values.get(i); // Add the corresponding balance to the sum
            }
        }
        return sum;
    }
    /**
     - DFS with memoization to determine the maximum number of balanced subgroups.
     -
     - @param mask Bitmask representing the remaining users.
     - @param dp Memoization array for storing computed results.
     - @param creditList List of net balances for each user.
     - @return The maximum number of fully settled subgroups.
     */
    public int dfs(int mask, int[] dp, List<Double> creditList) {
        if (mask == 0) // Base case: No users left to process
            return 0;
        if (dp[mask] != -1) // Return cached result if already computed
            return dp[mask];
        int maxSubGroups = 0;
        int n = creditList.size();
        // Try all possible subsets (submasks) of the current mask
        for (int submask = 1; submask < (1 << n); submask++) {
            // Check if submask is a subset of mask and sums to zero (i.e., can be settled)
            if ((submask & mask) == submask && Math.abs(sumOfMask(creditList, submask)) < 0.001) {
                // If a subset can be settled, find the remaining subgroups recursively
                maxSubGroups = Math.max(maxSubGroups, 1 + dfs(mask ^ submask, dp, creditList));
            }
        }
        dp[mask] = maxSubGroups; // Store result in memoization table
        return maxSubGroups;
    }

    /**
     - Helper method to calculate settlements from a balance map
     -
     - @param balanceMap The map of balances between user pairs
     - @return List of transactions to settle all debts
     */
    public List<Transaction> calculateSettlements(Map<UserPair, Double> balanceMap) {
        // Step 1: Calculate net balances for each user
        Map<AcceptRequests, Double> netBalances = new HashMap<>();

        for (Map.Entry<UserPair, Double> entry : balanceMap.entrySet()) {
            UserPair pair = entry.getKey();
            double amount = entry.getValue();

            AcceptRequests debtor = ArtRepo.findById(pair.getUser1Id()).get();
            AcceptRequests creditor = ArtRepo.findById(pair.getUser2Id()).get();

            netBalances.put(debtor, netBalances.getOrDefault(debtor, 0.0) - amount);
            netBalances.put(creditor, netBalances.getOrDefault(creditor, 0.0) + amount);
        }

        // Step 2: Separate users into debtors and creditors
        List<Map.Entry<AcceptRequests, Double>> debtors = new ArrayList<>();
        List<Map.Entry<AcceptRequests, Double>> creditors = new ArrayList<>();

        for (Map.Entry<AcceptRequests, Double> entry : netBalances.entrySet()) {
            if (Math.abs(entry.getValue()) < 0.001) continue; // Skip users with zero balance

            if (entry.getValue() < 0) debtors.add(entry);
            else creditors.add(entry);
        }

        // Sort debtors and creditors by the absolute amount (largest first)
        /*
          Matching the largest debtor to the largest creditor first ensures that large balances are settled early.
          This minimizes the number of transactions because fewer people remain in debt.The approach is greedy,
          meaning at every step, the algorithm minimizes the remaining total debt in the most optimal way.
        */

        debtors.sort((a, b) -> Double.compare(Math.abs(b.getValue()), Math.abs(a.getValue())));
        creditors.sort((a, b) -> Double.compare(Math.abs(b.getValue()), Math.abs(a.getValue())));

        // Step 3: Match debtors and creditors to create transactions
        List<Transaction> transactions = new ArrayList<>();
        int debtorIndex = 0;
        int creditorIndex = 0;

        while (debtorIndex < debtors.size() && creditorIndex < creditors.size()) {
            Map.Entry<AcceptRequests, Double> debtor = debtors.get(debtorIndex);
            Map.Entry<AcceptRequests, Double> creditor = creditors.get(creditorIndex);

            double debtorBalance = debtor.getValue(); // Negative value
            double creditorBalance = creditor.getValue(); // Positive value

            // Determine the transfer amount as the smaller of the two balances
            double transferAmount = Math.min(Math.abs(debtorBalance), creditorBalance);

            if (transferAmount > 0.001) { // Only create transactions for significant amounts
                transactions.add(new Transaction(debtor.getKey(), creditor.getKey(), transferAmount));
            }

            // Update balances after the transaction
            debtor.setValue(debtorBalance + transferAmount);
            creditor.setValue(creditorBalance - transferAmount);

            // Move to the next debtor or creditor if their balance is settled
            if (Math.abs(debtor.getValue()) < 0.001) {
                debtorIndex++;
            }
            if (Math.abs(creditor.getValue()) < 0.001) {
                creditorIndex++;
            }
        }

        return transactions;
    }




}
