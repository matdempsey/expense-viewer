package com.example.mathew.expenseviewer;

public class Expense {

    private String category, expense, transType, date;

    public Expense(String category, String expense, String transType, String date) {

        this.category = category;
        this.expense = expense;
        this.transType = transType;
        this.date = date;

    }


    public String getCategory() {
        return this.category;
    }


    public String getExpense() {
        return this.expense;
    }

    public String getTransType(){return this.transType;}


    public String getDate() {
        return this.date;
    }


}
