package com.example.mathew.expenseviewer.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mathew.expenseviewer.ExpenseDatabase;
import com.example.mathew.expenseviewer.MainActivity;
import com.example.mathew.expenseviewer.R;

import java.util.ArrayList;


public class ViewDailyExpensesFragment extends Fragment {

    private ExpenseDatabase db;

    private View view;
    private String tag = "ViewDailyExpensesFragment";
    private String currentDate;
    private Context context;
    private Bundle bundle;

    private ArrayList<String> queryResults = new ArrayList<>();

    private TextView tv_daily_expense_date;
    private ListView lv_daily_expenses;
    private Button btn_daily_expenses_cancel;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_view_daily_expenses, container, false);
        context = getActivity();

        bundle = getArguments();
        currentDate = bundle.getString(MainActivity.CURRENT_DATE, "");

        tv_daily_expense_date = (TextView) view.findViewById(R.id.tv_daily_expense_date);
        lv_daily_expenses = (ListView) view.findViewById(R.id.lv_daily_expenses);
        btn_daily_expenses_cancel = (Button) view.findViewById(R.id.btn_daily_expenses_cancel);

        tv_daily_expense_date.setText(currentDate);

        //listeners
        btn_daily_expenses_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                MonthlyExpensesFragment monthlyExpensesFragment = new MonthlyExpensesFragment();
                monthlyExpensesFragment.setArguments(bundle);
                ft.replace(R.id.fl_fragment, monthlyExpensesFragment);
                ft.commit();

            }
        });

        ViewDailyExpensesAdapter viewDailyExpensesAdapter = new ViewDailyExpensesAdapter(context, R.layout.custom_listview_view_expenses,
                currentDate, queryResults);
        lv_daily_expenses.setAdapter(viewDailyExpensesAdapter);

        return view;

    }


    private class ViewDailyExpensesAdapter extends ArrayAdapter {

        private Context context;
        private int resource;

        private ArrayList<String> categoryList = new ArrayList<>();
        private ArrayList<String> expensesList = new ArrayList<>();
        private ArrayList<String> transactionTypeList = new ArrayList<>();

        public ViewDailyExpensesAdapter(Context context, int resource, String currentDate, ArrayList<String> queryResults) {

            super(context, resource, queryResults);

            this.context = context;
            this.resource = resource;

            db = new ExpenseDatabase(context);
            queryResults = db.retrieveDailyExpenses(currentDate);

            //if i is a even number then element is a category, if it's an odd number then its an expense.
            for (int i = 0; i < queryResults.size(); i++) {

                if (i % 2 != 1) {

                    categoryList.add(queryResults.get(i));

                } else {

                    expensesList.add(queryResults.get(i));

                }

            }

            Log.d(tag, "expense: " + queryResults);
            Log.d(tag, "expense: " + categoryList);
            Log.d(tag, "expense: " + expensesList);
            Log.d(tag, "expense: " + transactionTypeList);

        }

        @Override
        public int getCount() {
            return expensesList.size();
        }

        @Override
        public View getView(int pos, View convertView, ViewGroup parent) {

            View view = convertView;

            if (view == null) {

                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = layoutInflater.inflate(resource, null);

            }

            TextView tv_expense_category = (TextView) view.findViewById(R.id.tv_expense_category);
            TextView tv_expense_cost = (TextView) view.findViewById(R.id.tv_expense_cost);
            TextView tv_expense_trans_type = (TextView) view.findViewById(R.id.tv_expense_trans_type);

            tv_expense_category.setText(categoryList.get(pos));
            tv_expense_cost.setText("£" + expensesList.get(pos));
            //tv_expense_trans_type.setText(transactionTypeList.get(pos));
            notifyDataSetChanged();

            return view;

        }
    }


}




