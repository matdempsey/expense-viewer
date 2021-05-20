package com.example.mathew.expenseviewer.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mathew.expenseviewer.MainActivity;
import com.example.mathew.expenseviewer.R;
import com.example.mathew.expenseviewer.SharedPrefs;

import java.util.ArrayList;


public class MonthlyBudgetFragment extends Fragment {

    private Context context;
    private View view;
    private String tag = "MonthlyBudgetFragment";

    private ArrayList<String> expenseCategoryList, expenseBudgetList;

    private ListView lv_monthly_budget;
    private TextView tv_monthly_budget_date;
    private Button btn_monthly_budget_save, btn_monthly_budget_cancel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_monthly_budget, container, false);
        context = getActivity();

        tv_monthly_budget_date = (TextView) view.findViewById(R.id.tv_monthly_budget_date);
        lv_monthly_budget = (ListView) view.findViewById(R.id.lv_monthly_budget);
        btn_monthly_budget_save = (Button) view.findViewById(R.id.btn_monthly_budget_save);
        btn_monthly_budget_cancel = (Button) view.findViewById(R.id.btn_monthly_budget_cancel);

        Bundle bundle = getArguments();

        tv_monthly_budget_date.setText(bundle.getString(MainActivity.CURRENT_DATE, ""));
        final String currentMonth = tv_monthly_budget_date.getText().toString();

        expenseCategoryList = new ArrayList<>();
        expenseBudgetList = new ArrayList<>();

        int defaultSize = SharedPrefs.retrieveExpenseCategories(context).size();

        if (defaultSize == 0) {  // initial setup

            expenseCategoryList.add("Food");
            expenseCategoryList.add("Fuel");
            expenseCategoryList.add("Bills");
            expenseCategoryList.add("Rent");
            expenseCategoryList.add("Social");
            expenseCategoryList.add("-- Add custom category --");

            SharedPrefs.storeExpenseCategories(context, expenseCategoryList);

        }

        expenseCategoryList = SharedPrefs.retrieveExpenseCategories(context);
        expenseBudgetList = SharedPrefs.retrieveCategoryMonthlyBudget(context, currentMonth);

        int customCategoryPos = expenseCategoryList.size() - 1;
        expenseCategoryList.remove(customCategoryPos);  // remove add custom category, as not a category

        final MonthlyBudgetAdapter monthlyBudgetAdapter = new MonthlyBudgetAdapter(context, R.layout.custom_listview_monthly_budget, expenseCategoryList, expenseBudgetList);
        lv_monthly_budget.setAdapter(monthlyBudgetAdapter);

        //Listeners
        btn_monthly_budget_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                expenseBudgetList = monthlyBudgetAdapter.getBudgetEtListValues();
                SharedPrefs.storeCategoryMonthlyBudget(context, currentMonth, expenseBudgetList);

                loadFragment(new MonthlyExpensesFragment());

            }
        });

        btn_monthly_budget_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loadFragment(new MonthlyExpensesFragment());

            }
        });

        return view;

    }

    private void loadFragment(Fragment fragment) {

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fl_fragment, fragment);
        ft.commit();

    }


    private class MonthlyBudgetAdapter extends ArrayAdapter {

        private Context context;
        private int resource;

        private ArrayList<String> adapterExpenseCategoryList, adapterExpenseBudgetList;
        private ArrayList<EditText> budgetEtList;

        public MonthlyBudgetAdapter(Context context, int resource, ArrayList<String> expenseCategoryList, ArrayList<String> expenseBudgetList) {
            super(context, resource, expenseCategoryList);

            this.context = context;
            this.resource = resource;
            this.adapterExpenseCategoryList = expenseCategoryList;
            this.adapterExpenseBudgetList = expenseBudgetList;

            budgetEtList = new ArrayList<>();

        }

        @Override
        public int getCount() {
            return super.getCount();
        }

        @NonNull
        @Override
        public View getView(int pos, View convertView, ViewGroup parent) {

            View view = convertView;

            if (view == null) {

                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = layoutInflater.inflate(resource, null);

            }

            TextView tv_monthly_budget_category = (TextView) view.findViewById(R.id.tv_monthly_budget_category);
            TextView tv_monthly_budget_currency = (TextView) view.findViewById(R.id.tv_monthly_budget_currency);
            EditText et_monthly_budget_amount = (EditText) view.findViewById(R.id.et_monthly_budget_amount);

            tv_monthly_budget_category.setText(adapterExpenseCategoryList.get(pos));

            budgetEtList.add(et_monthly_budget_amount);
            Log.d(tag, "budgetEtList size =  " + budgetEtList.size());

            if (adapterExpenseBudgetList.size() > 0 && pos < adapterExpenseBudgetList.size()) {

                et_monthly_budget_amount.setText(adapterExpenseBudgetList.get(pos));
                Log.d(tag, "adapterExpenseBudgetList values: " + adapterExpenseBudgetList);

            }

            return view;

        }

        public ArrayList<String> getBudgetEtListValues() {

            int listViewChildCount = lv_monthly_budget.getChildCount();

            ArrayList<String> budgetEtListValues = new ArrayList<>();

            for (int i = 0; i < listViewChildCount; i++) {

                budgetEtListValues.add(budgetEtList.get(i).getText().toString());

            }

            return budgetEtListValues;

        }

    }


}
