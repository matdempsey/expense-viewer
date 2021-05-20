package com.example.mathew.expenseviewer;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.mathew.expenseviewer.fragments.MonthlyExpensesFragment;


public class MainActivity extends AppCompatActivity {

    private String tag = "MainActivity";

    //constants
    public static final String CURRENT_DATE = "currentDate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //hide blue action bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        MonthlyExpensesFragment monthlyExpensesFragment = new MonthlyExpensesFragment();
        ft.replace(R.id.fl_fragment, monthlyExpensesFragment);
        ft.commit();

    }


}