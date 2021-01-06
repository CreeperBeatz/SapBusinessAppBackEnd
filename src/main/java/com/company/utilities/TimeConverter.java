package com.company.utilities;

import com.company.exceptions.WrapperException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeConverter {


    public static String MillisToString(long input){
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        Date date = new Date(input);
        return sdf.format(date);
    }

    /**
     * Converts String to time in millis
     * @param input expects string in format "dd.mm.yyyy"
     * @return time in millis
     * @throws WrapperException if parsing went wrong
     */
    public static long StringToMillis(String input) throws WrapperException {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            Date date = sdf.parse(input);
            return date.getTime();
        } catch (ParseException e) {
            throw new WrapperException(e, "Couldn't parse time correctly");
        }
    }
}
