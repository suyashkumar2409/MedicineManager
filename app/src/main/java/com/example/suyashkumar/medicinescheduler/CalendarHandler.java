package com.example.suyashkumar.medicinescheduler;

import android.util.Log;

import java.util.Calendar;
import java.util.StringTokenizer;

/**
 * Created by SUYASH KUMAR on 1/9/2017.
 */

public class CalendarHandler {
    public static String numberToMonth(int num){
        String retval="";
        switch(num){
            case 0:
                retval = "Jan";
                break;
            case 1:
                retval = "Feb";
                break;
            case 2:
                retval = "Mar";
                break;
            case 3:
                retval = "Apr";
                break;
            case 4:
                retval = "May";
                break;
            case 5:
                retval = "Jun";
                break;
            case 6:
                retval = "Jul";
                break;
            case 7:
                retval = "Aug";
                break;
            case 8:
                retval = "Sep";
                break;
            case 9:
                retval = "Oct";
                break;
            case 10:
                retval = "Nov";
                break;
            case 11:
                retval = "Dec";
                break;
        }
        return retval;
    }

    public static String numToDay(int num)
    {
        String retval="";
        switch (num){
            case 1:
                retval = "Sunday";
                break;
            case 2:
                retval = "Monday";
                break;
            case 3:
                retval = "Tuesday";
                break;
            case 4:
                retval = "Wednesday";
                break;
            case 5:
                retval = "Thursday";
                break;
            case 6:
                retval = "Friday";
                break;
            case 7:
                retval = "Saturday";
                break;
        }

        return retval;
    }

    public static int getDifferenceInDays(long now,  String start)
    {
        StringTokenizer tokenizer = new StringTokenizer(start, "/");
        int date, month, year;
        date = Integer.parseInt(tokenizer.nextToken());
        month = Integer.parseInt(tokenizer.nextToken());
        year = Integer.parseInt(tokenizer.nextToken());

        Calendar c = Calendar.getInstance();
        c.set(year, month, date);
        long millDiff = c.getTimeInMillis() - now;
        return milliSecondsToDays(millDiff);
    }
    private static int milliSecondsToDays(long mil)
    {
        long val = (long)((((double)mil/100)/3600)/24);
        return (int)val;
    }

}
