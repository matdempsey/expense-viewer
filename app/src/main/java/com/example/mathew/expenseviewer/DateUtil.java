package com.example.mathew.expenseviewer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class DateUtil {

    private static int num = 0;


    public static String getCurrentMonthAndYear() {

        String currentMonth;

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM YYYY");

        if (num != 0) { // then it's not the current month (set by device date settings)

            calendar.add(Calendar.MONTH, num); // increment or decrement month by value held in num

        }

        currentMonth = sdf.format(calendar.getTime());

        return currentMonth;

    }


    public static ArrayList getDaysOfCurrentMonth() {

        int currentMonthMaxDays;
        Date date;

        ArrayList<String> daysOfMonthList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("EE dd");

        Calendar calendar = Calendar.getInstance();

        // if current month has changed a new date must be set to get correct max days in new current month
        calendar.add(Calendar.MONTH, num);
        currentMonthMaxDays = calendar.getActualMaximum(Calendar.DATE);

        for (int i = 1; i <= currentMonthMaxDays; i++) {

            calendar.set(Calendar.DAY_OF_MONTH, i);
            date = calendar.getTime();
            daysOfMonthList.add(sdf.format(date));

        }

        return daysOfMonthList;

    }


    // the date of when an expense was added. pos = position in list view
    public static String getSelectedDate(int pos) {

        String selectedDate;
        Date date;

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        if (num != 0) { // then it's not the current month (set by device date settings)

            calendar.add(Calendar.MONTH, num); //increment or decrement month by one
        }

        calendar.set(Calendar.DAY_OF_MONTH, pos + 1);

        date = calendar.getTime();
        selectedDate = sdf.format(date);

        return selectedDate;

    }


    public static String changeCurrentMonth() {

        Date date;
        String month;

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM YYYY");

        calendar.add(Calendar.MONTH, num);            //adds or subtracts i(1) month from the current month
        calendar.set(Calendar.DAY_OF_MONTH, 1);    // sets calender to first day of new month (for cleanness)
       // calendar.getActualMaximum(Calendar.DATE);

        date = calendar.getTime();
        month = sdf.format(date);

        return month;

    }


    public static int setNum(int i) {

        num = i;
        return i;

    }

    public static int getNum() {

        return num;

    }

}
