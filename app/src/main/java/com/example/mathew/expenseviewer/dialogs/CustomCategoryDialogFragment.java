package com.example.mathew.expenseviewer.dialogs;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.mathew.expenseviewer.R;
import com.example.mathew.expenseviewer.SharedPrefs;

import java.util.ArrayList;
import java.util.Collections;


public class CustomCategoryDialogFragment extends DialogFragment {

    private Context context;
    private View view;
    private ArrayList<String> expenseCategoryList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.dialog_add_custom_category, container, false);
        context = getActivity();

        expenseCategoryList = new ArrayList<>();
        expenseCategoryList = SharedPrefs.retrieveExpenseCategories(context);

        final EditText et_custom_category = (EditText) view.findViewById(R.id.et_custom_category);
        Button btn_add_custom_category = (Button) view.findViewById(R.id.btn_add_custom_category);
        Button btn_custom_category_cancel = (Button) view.findViewById(R.id.btn_custom_category_cancel);

        btn_add_custom_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String customCategory = et_custom_category.getText().toString();
                expenseCategoryList.add(customCategory);
                Collections.swap(expenseCategoryList, expenseCategoryList.size() - 1, expenseCategoryList.size() - 2);  // add custom category option back to the end of array list
                SharedPrefs.storeExpenseCategories(context, expenseCategoryList);

                dismiss();

            }
        });

        btn_custom_category_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dismiss();

            }
        });

        return view;

    }

}
