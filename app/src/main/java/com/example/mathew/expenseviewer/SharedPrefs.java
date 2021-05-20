package com.example.mathew.expenseviewer;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.ArrayList;


public class SharedPrefs {

    private static final String TAG = "SharedPrefs";
    private static final String PREFS_FILE_EXPENSE_CATEGORIES = "expenseCategories";
    private static final String PREFS_FILE_BUDGET_CATEGORIES = "budgetCategories";

    public static void storeExpenseCategories(Context context, ArrayList<String> categoryList) {

        SharedPreferences prefs = context.getSharedPreferences(PREFS_FILE_EXPENSE_CATEGORIES, 0);
        SharedPreferences.Editor editor = prefs.edit();

        editor.clear().commit();  //clear old prefs file to make way for new prefs file (important when deleting categories)

        for (int i = 0; i < categoryList.size(); i++) {

            editor.putString(String.valueOf(i), categoryList.get(i));

        }

        Log.d(TAG, "set expense categories list: " + categoryList);

        editor.commit();

    }

    public static ArrayList<String> retrieveExpenseCategories(Context context) {

        ArrayList<String> categoryList = new ArrayList();
        SharedPreferences prefs = context.getSharedPreferences(PREFS_FILE_EXPENSE_CATEGORIES, 0);

        for (int i = 0; i < prefs.getAll().size(); i++) {

            categoryList.add(prefs.getString(String.valueOf(i), ""));

        }

        Log.d(TAG, "get expense categories list: " + categoryList);

        return categoryList;

    }

    public static void storeCategoryMonthlyBudget(Context context, String month, ArrayList<String> expenseBudgetList) {

        SharedPreferences prefs = context.getSharedPreferences(PREFS_FILE_BUDGET_CATEGORIES, 0);
        SharedPreferences.Editor editor = prefs.edit();

//        editor.clear().commit();  //clear old prefs file to make way for new prefs file (important when deleting categories)

        for (int i = 0; i < expenseBudgetList.size(); i++) {

            editor.putString(month + "_" + i, expenseBudgetList.get(i));

        }

        Log.d(TAG, "set  budget categories list: " + expenseBudgetList);

        editor.commit();

    }


    public static ArrayList<String> retrieveCategoryMonthlyBudget(Context context, String month) {

        ArrayList<String> expenseBudgetList = new ArrayList();
        SharedPreferences prefs = context.getSharedPreferences(PREFS_FILE_BUDGET_CATEGORIES, 0);

        for (int i = 0; i < prefs.getAll().size(); i++) {

            expenseBudgetList.add(prefs.getString(month + "_" + i, ""));

        }

        Log.d(TAG, "get budget categories list: " + expenseBudgetList);

        return expenseBudgetList;

    }


}
