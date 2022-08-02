package com.ivanfranchin.axoneventcommons.util;

public final class MyStringUtils {

    public static String getTrimmedValueOrElse(String value, String fallbackValue) {
        String newValue = value == null ? fallbackValue : value.trim();
        return "".equals(newValue) ? fallbackValue : newValue;
    }
}
