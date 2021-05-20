package com.example.mathew.expenseviewer.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mathew.expenseviewer.Calculations;
import com.example.mathew.expenseviewer.ExpenseDatabase;
import com.example.mathew.expenseviewer.MainActivity;
import com.example.mathew.expenseviewer.R;
import com.example.mathew.expenseviewer.SharedPrefs;

import java.util.ArrayList;


public class MonthlySummaryFragment extends Fragment {

    private View view;
    private Context context;
    private String tag = "MonthlySummaryFragment";

    private ArrayList<String> expenseCategoryList;

    private Button btn_cancel;
    private TextView tv_monthly_expense_summary_date;
    private ListView lv_monthly_expense_summary;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_monthly_summary, container, false);
        context = getActivity();

        btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        tv_monthly_expense_summary_date = (TextView) view.findViewById(R.id.tv_monthly_expense_summary_date);
        lv_monthly_expense_summary = (ListView) view.findViewById(R.id.lv_monthly_expense_summary);

        Bundle bundle = getArguments();

        tv_monthly_expense_summary_date.setText(bundle.getString(MainActivity.CURRENT_DATE, ""));

        expenseCategoryList = new ArrayList<>();
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

        int customCategoryPos = expenseCategoryList.size() - 1;
        expenseCategoryList.remove(customCategoryPos);  // remove add custom category, as not a category

        MonthlySummaryAdapter monthlySummaryAdapter = new MonthlySummaryAdapter(context, R.layout.custom_listview_monthly_summary, expenseCategoryList);
        lv_monthly_expense_summary.setAdapter(monthlySummaryAdapter);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                MonthlyExpensesFragment monthlyExpensesFragment = new MonthlyExpensesFragment();
                ft.replace(R.id.fl_fragment, monthlyExpensesFragment);
                ft.commit();

            }
        });


        return view;


    }


    private class MonthlySummaryAdapter extends ArrayAdapter {

        private ExpenseDatabase db;
        private int resource;

        private ArrayList<String> adapterExpenseCategoryList, adapterCategoryExpensesList, adapterSumExpensesList, adapterOverbudgetList;


        public MonthlySummaryAdapter(@NonNull Context context, int resource, ArrayList<String> expenseCategoryList) {
            super(context, resource, expenseCategoryList);

            this.resource = resource;
            this.adapterExpenseCategoryList = expenseCategoryList;

            adapterSumExpensesList = new ArrayList<>();
            adapterOverbudgetList = new ArrayList<>();

            db = new ExpenseDatabase(context);

            String category, expenseSum;

            for (int i = 0; i < adapterExpenseCategoryList.size(); i++) {

                category = adapterExpenseCategoryList.get(i);
                adapterCategoryExpensesList = db.retrieveCategoryExpenses(category, );

                expenseSum = Calculations.sumCategoryExpenses(adapterCategoryExpensesList);
                adapterSumExpensesList.add(expenseSum);

            }

        }


        @NonNull
        @Override
        public View getView(int pos, @Nullable View convertView, @NonNull ViewGroup parent) {

            View view = convertView;

            if (view == null) {

                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = layoutInflater.inflate(resource, null);

            }

            TextView tv_monthly_summary_category = (TextView) view.findViewById(R.id.tv_monthly_summary_category);
            TextView tv_monthly_summary_expense = (TextView) view.findViewById(R.id.tv_monthly_summary_expense);
            TextView tv_monthly_summary_overbudget = (TextView) view.findViewById(R.id.tv_monthly_summary_overbudget);

            tv_monthly_summary_category.setText(adapterExpenseCategoryList.get(pos));
            tv_monthly_summary_expense.setText(adapterSumExpensesList.get(pos));


            return view;
        }

    }


}
