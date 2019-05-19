package com.joomag.test.util;

import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utils {

    public static String getHourPreviewWithNames(String dateString) {
        Date date = null;
        try {
            date = new SimpleDateFormat(Constants.APP_DATE_FORMAT).parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String hours = (String) DateFormat.format("HH", date);
        String minuts = (String) DateFormat.format("mm", date);
        String aa = (String) DateFormat.format("aa", date);

        return hours + ":" + minuts + " " + aa;
    }
}
