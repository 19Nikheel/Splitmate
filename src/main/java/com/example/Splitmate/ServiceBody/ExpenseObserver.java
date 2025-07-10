package com.example.Splitmate.ServiceBody;

public interface ExpenseObserver {
    void onExpenseAdded(Expense expense);


    void onExpenseUpdated(Expense expense);
}
