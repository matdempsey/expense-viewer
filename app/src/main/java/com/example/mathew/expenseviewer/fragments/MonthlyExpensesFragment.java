package com.example.mathew.expenseviewer.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mathew.expenseviewer.DateUtil;
import com.example.mathew.expenseviewer.MainActivity;
import com.example.mathew.expenseviewer.R;

import java.util.ArrayList;

public class MonthlyExpensesFragment extends Fragment {

    private Context context;
    private View view;
    private Bundle bundle;
    private String tag = "MonthlyExpensesFragment";
    private String month;
    private int i;

    private DailyExpensesAdapter dailyExpensesAdapter;
    private ArrayList<String> daysOfMonthList;

    private TextView tv_date_header;
    private ImageButton ib_prev_month, ib_next_month;
    private ImageButton ib_monthly_budget, ib_monthly_summary;
    private ListView lv_expenses;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_monthly_expenses, container, false);
        context = getActivity();
        bundle = new Bundle();

        tv_date_header = (TextView) view.findViewById(R.id.tv_date_header);
        ib_prev_month = (ImageButton) view.findViewById(R.id.ib_prev_month);
        ib_next_month = (ImageButton) view.findViewById(R.id.ib_next_month);
        ib_monthly_budget = (ImageButton) view.findViewById(R.id.ib_monthly_budget);
        ib_monthly_summary = (ImageButton) view.findViewById(R.id.ib_monthly_summary);
        lv_expenses = (ListView) view.findViewById(R.id.lv_expenses);

        daysOfMonthList = new ArrayList<>();

        // initial date setup
        i = DateUtil.getNum();   // retrieves the number of times num has been incremented/decremented from current device date.
        month = DateUtil.getCurrentMonthAndYear();
        tv_date_header.setText(month);
        daysOfMonthList = DateUtil.getDaysOfCurrentMonth();

        // initial adapter setup
        dailyExpensesAdapter = new DailyExpensesAdapter(context, R.layout.custom_listview_daily_expenses, daysOfMonthList);
        lv_expenses.setAdapter(dailyExpensesAdapter);

        // listeners
        ib_prev_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                i--;
                DateUtil.setNum(i);
                updateDates();

            }
        });

        ib_next_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                i++;
                DateUtil.setNum(i);
                updateDates();

            }
        });

        ib_monthly_budget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bundle.putString(MainActivity.CURRENT_DATE, month);

                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                MonthlyBudgetFragment monthlyBudgetFragment = new MonthlyBudgetFragment();
                monthlyBudgetFragment.setArguments(bundle);
                ft.replace(R.id.fl_fragment, monthlyBudgetFragment);
                ft.commit();


            }
        });

        ib_monthly_summary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bundle.putString(MainActivity.CURRENT_DATE, month);

                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                MonthlySummaryFragment monthlySummaryFragment = new MonthlySummaryFragment();
                monthlySummaryFragment.setArguments(bundle);
                ft.replace(R.id.fl_fragment, monthlySummaryFragment);
                ft.commit();

            }
        });

        return view;

    }

    private void updateDates() {

        month = DateUtil.changeCurrentMonth();
        daysOfMonthList = DateUtil.getDaysOfCurrentMonth();
        tv_date_header.setText(month);
        dailyExpensesAdapter = new DailyExpensesAdapter(context, R.layout.custom_listview_daily_expenses, daysOfMonthList);
        lv_expenses.setAdapter(dailyExpensesAdapter);

    }


    private class DailyExpensesAdapter extends ArrayAdapter {

        private Context context;
        private int resource;
        private String currentDate;
        private ArrayList<String> daysOfMonthList;
        private Bundle bundle;

        public DailyExpensesAdapter(Context context, int resource, ArrayList<String> list) {

            super(context, resource, list);

            this.context = context;
            this.resource = resource;
            this.daysOfMonthList = list;

            bundle = new Bundle();

        }

        @Override
        public View getView(final int pos, View convertView, ViewGroup parent) {

            View view = convertView;

            if (view == null) {

                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = layoutInflater.inflate(resource, null);

            }

            TextView tv_day_date = (TextView) view.findViewById(R.id.tv_day_date);
            Button btn_add_expense = (Button) view.findViewById(R.id.btn_add_expense);
            Button btn_view_expense = (Button) view.findViewById(R.id.btn_view_expense);

            tv_day_date.setText(daysOfMonthList.get(pos));

            //listeners
            btn_view_expense.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    currentDate = DateUtil.getSelectedDate(pos);
                    bundle.putString(MainActivity.CURRENT_DATE, currentDate);

                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ViewDailyExpensesFragment viewDailyExpensesFragment = new ViewDailyExpensesFragment();
                    viewDailyExpensesFragment.setArguments(bundle);
                    ft.replace(R.id.fl_fragment, viewDailyExpensesFragment);
                    ft.commit();


                }
            });

            btn_add_expense.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    currentDate = DateUtil.getSelectedDate(pos);
                    bundle.putString(MainActivity.CURRENT_DATE, currentDate);

                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    AddExpenseFragment addExpenseFragment = new AddExpenseFragment();
                    addExpenseFragment.setArguments(bundle);
                    ft.replace(R.id.fl_fragment, addExpenseFragment);
                    ft.commit();

                    notifyDataSetChanged();

                }
            });

            return view;

        }
    }


}
