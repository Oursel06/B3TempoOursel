package com.example.b3tempooursel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Tools {
    public static String getNowDate(String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.FRANCE);
        Date now = new Date();
        return sdf.format(now);
    }
}
