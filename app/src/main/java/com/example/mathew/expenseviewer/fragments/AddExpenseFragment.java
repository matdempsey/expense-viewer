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
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mathew.expenseviewer.Expense;
import com.example.mathew.expenseviewer.ExpenseDatabase;
import com.example.mathew.expenseviewer.MainActivity;
import com.example.mathew.expenseviewer.R;
import com.example.mathew.expenseviewer.SharedPrefs;
import com.example.mathew.expenseviewer.dialogs.CustomCategoryDialogFragment;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class AddExpenseFragment extends Fragment {

    private Context context;
    private View view;
    private String tag = "AddExpenseFragment";
    private ExpenseDatabase db;
    private ArrayList<String> expenseCategoryList, transactionTypeList;
    private Bundle bundle;

    private Button btn_add_expense, btn_cancel;
    private EditText et_expense;
    private Spinner sp_category, sp_transaction_type;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_add_expense, container, false);
        context = getActivity();

        int defaultSize = SharedPrefs.retrieveExpenseCategories(context).size();

        btn_add_expense = (Button) view.findViewById(R.id.btn_add_expense);
        btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        et_expense = (EditText) view.findViewById(R.id.et_expense);
        sp_category = (Spinner) view.findViewById(R.id.sp_category);
        sp_transaction_type = (Spinner) view.findViewById(R.id.sp_transaction_type);


        //list of categories
        expenseCategoryList = new ArrayList<>();
        expenseCategoryList = SharedPrefs.retrieveExpenseCategories(context);

        if (defaultSize == 0) {  // initial setup

            expenseCategoryList.add("Food");
            expenseCategoryList.add("Fuel");
            expenseCategoryList.add("Bills");
            expenseCategoryList.add("Rent");
            expenseCategoryList.add("Social");
            expenseCategoryList.add("-- Add custom category --");

            SharedPrefs.storeExpenseCategories(context, expenseCategoryList);

        }

        // list of transaction types
        transactionTypeList = new ArrayList<>();
        transactionTypeList.add("Cash");
        transactionTypeList.add("Credit/Debit Card");
        transactionTypeList.add("SO/DD");

        CustomCategorySpinner customCategorySpinner = new CustomCategorySpinner(context, R.layout.custom_spinner_add_category, R.id.tv_spinner_categories, expenseCategoryList);
        sp_category.setAdapter(customCategorySpinner);

        ArrayAdapter<String> arrayAdapterTransactionTypeCategory = new ArrayAdapter<>(context, R.layout.support_simple_spinner_dropdown_item, transactionTypeList);
        sp_transaction_type.setAdapter(arrayAdapterTransactionTypeCategory);

        btn_add_expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bundle = getArguments();
                String currentDate = bundle.getString(MainActivity.CURRENT_DATE, "");

                addExpense(context, currentDate);
                Toast.makeText(context, "Expense Added Successfully.", Toast.LENGTH_SHORT).show();

                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fl_fragment, new MonthlyExpensesFragment());
                ft.commit();

            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fl_fragment, new MonthlyExpensesFragment());
                ft.commit();

            }
        });

        return view;

    }

    private void addExpense(Context context, String currentDate) {

        String expense = et_expense.getText().toString();
        String category = sp_category.getSelectedItem().toString();
        String transType = sp_transaction_type.getSelectedItem().toString();

        Expense newExpense = new Expense(category, expense, transType, currentDate);

        db = new ExpenseDatabase(context);
        db.insertExpense(newExpense);

    }

    private class CustomCategorySpinner extends ArrayAdapter {

        private Context context;
        private int resource, textViewID, customCategoryPos;

        private ArrayList<String> expenseCategoryList;

        private TextView tv_spinner_categories;
        ImageButton ib_spinner_delete_category;


        public CustomCategorySpinner(Context context, int resource, int textViewID, ArrayList<String> expenseCategoryList) {

            super(context, resource, textViewID, expenseCategoryList);

            this.context = context;
            this.resource = resource;
            this.textViewID = textViewID;
            this.expenseCategoryList = expenseCategoryList;

            customCategoryPos = expenseCategoryList.size() - 1;

        }

        @NonNull
        @Override
        public View getView(int pos, @Nullable View convertView, @NonNull ViewGroup parent) {

            View view = convertView;

            if (view == null) {

                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = layoutInflater.inflate(resource, null);

            }

            tv_spinner_categories = (TextView) view.findViewById(R.id.tv_spinner_categories);
            ib_spinner_delete_category = (ImageButton) view.findViewById(R.id.ib_spinner_delete_category);

            tv_spinner_categories.setText(expenseCategoryList.get(pos));
            ib_spinner_delete_category.setVisibility(View.INVISIBLE);

            return view;

        }

        // handles functionality of views inside the spinner's dropdown list
        @Override
        public View getDropDownView(final int pos, @Nullable View convertView, @NonNull ViewGroup parent) {

            View view = convertView;

            if (view == null) {

                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = layoutInflater.inflate(resource, null);

            }

            tv_spinner_categories = (TextView) view.findViewById(R.id.tv_spinner_categories);
            ib_spinner_delete_category = (ImageButton) view.findViewById(R.id.ib_spinner_delete_category);

            tv_spinner_categories.setText(expenseCategoryList.get(pos));

            if (pos < 5 || pos == customCategoryPos) {

                ib_spinner_delete_category.setVisibility(View.INVISIBLE);

            } else {

                ib_spinner_delete_category.setVisibility(View.VISIBLE);

            }

            tv_spinner_categories.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    sp_category.setSelection(pos);
                    hideSpinnerDropDown(sp_category);

                    if (pos == customCategoryPos) {

                        FragmentManager fm = getFragmentManager();
                        CustomCategoryDialogFragment customCategoryDialogFragment = new CustomCategoryDialogFragment();
                        customCategoryDialogFragment.show(fm, "");

                        Log.d(tag, "item clicked at pos " + pos);

                    }


                }
            });

            ib_spinner_delete_category.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    expenseCategoryList.remove(pos);
                    Log.d(tag, "categories post delete: " + expenseCategoryList);
                    SharedPrefs.storeExpenseCategories(context, expenseCategoryList);
                    notifyDataSetChanged();

                }
            });

            return view;

        }

        // closes custom spinner drop down when item selected
        public void hideSpinnerDropDown(Spinner spinner) {

            try {

                Method method = Spinner.class.getDeclaredMethod("onDetachedFromWindow");
                method.setAccessible(true);
                method.invoke(spinner);

            } catch (Exception e) {

                e.printStackTrace();

            }
        }

    }


}



