package com.example.mathew.expenseviewer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ExpenseDatabase extends SQLiteOpenHelper {

    private String tag = "ExpenseDatabase";
    private static final String DATABASE_NAME = "expenseDB";
    private static final int DATABASE_VERSION = 1;

    //table names
    private static final String TABLE_EXPENSES = "expenses";

    //column names
    private static final String ID = "id";
    private static final String CATEGORY = "category";
    private static final String EXPENSE = "expense";
    private static final String TRANS_TYPE = "transaction_type";
    private static final String DATE = "date";


    public ExpenseDatabase(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }


    // called when database is accessed but has not yet been created
    // create tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_EXPENSES_TABLE = "CREATE TABLE " + TABLE_EXPENSES + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + CATEGORY + " TEXT,"
                + EXPENSE + " TEXT," + TRANS_TYPE + " TEXT," + DATE + " TEXT" + ")";

        db.execSQL(CREATE_EXPENSES_TABLE);

    }


    // called when incrementing the database version no. Can manage migration process here.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
        onCreate(db);

    }


    //insert data
    public void insertExpense(Expense expense) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(CATEGORY, expense.getCategory());
        contentValues.put(EXPENSE, expense.getExpense());
        contentValues.put(TRANS_TYPE, expense.getTransType());
        contentValues.put(DATE, expense.getDate());

        db.insert(TABLE_EXPENSES, null, contentValues);
        db.close();

    }


    //retrieve data
    public ArrayList<String> retrieveDailyExpenses(String currentExpenseDate) {

        Cursor cursor;
        ArrayList<String> expensesList = new ArrayList<>();
        String whereArgs[] = {currentExpenseDate};

        SQLiteDatabase db = this.getReadableDatabase();

        cursor = db.query(TABLE_EXPENSES, null, DATE + "= ?", whereArgs, null,
                null, null);

        if (cursor != null && cursor.moveToFirst()) {

            do {

                expensesList.add(cursor.getString(cursor.getColumnIndex("category")));
                expensesList.add(cursor.getString(cursor.getColumnIndex("expense")));
                expensesList.add(cursor.getString(cursor.getColumnIndex("transaction_type")));

            } while (cursor.moveToNext());

        }

        cursor.close();

        return expensesList;

    }


    //retrieve a category and all its corresponding expenses for a given month in a year
    public ArrayList<String> retrieveCategoryExpenses(String category, String monthYear) {

        //wrong
        String formattedDate;
        SimpleDateFormat sdf = new SimpleDateFormat("MM-yyyy");
        formattedDate = sdf.format(new Date());

        Cursor cursor;
        ArrayList<String> categoryExpenseList = new ArrayList<>();
        String fromArgs[] = {EXPENSE};
        String whereArgs[] = {category, formattedDate};

        SQLiteDatabase db = this.getReadableDatabase();

        cursor = db.query(TABLE_EXPENSES, fromArgs, EXPENSE + "= ?", whereArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {

            do {

                categoryExpenseList.add(cursor.getString(cursor.getColumnIndex(EXPENSE)));

            } while (cursor.moveToNext());

        }

        return categoryExpenseList;

    }


    //delete data


}
