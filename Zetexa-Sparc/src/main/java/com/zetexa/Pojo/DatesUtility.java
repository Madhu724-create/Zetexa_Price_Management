package com.zetexa.Pojo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


public class DatesUtility {
    public static Date parseDates(String date) {
        if (date == null) return null;

        List<String> formats = List.of("yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd");
        for (String format : formats) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                return sdf.parse(date);
            } catch (ParseException ignored) {}
        }
        throw new RuntimeException("Invalid date format in criteria: " + date);
    }
}
