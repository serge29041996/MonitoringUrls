package com.common;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

/**
 * Util class for formatted data
 */
public class DataUtils {
  private DataUtils() {
  }

  private static void initializeTime(Calendar calendarTime, int hour, int minute, int second) {
    calendarTime.set(Calendar.HOUR_OF_DAY, hour);
    calendarTime.set(Calendar.MINUTE, minute);
    calendarTime.set(Calendar.SECOND, second);
  }

  public static Date getFormattedISOTime(int hour, int minute, int second) {
    Calendar calendarTime = Calendar.getInstance();

    calendarTime.clear();
    initializeTime(calendarTime, hour, minute, second);

    return calendarTime.getTime();
  }

  public static Timestamp getFormattedISODateTime(int year, int month, int day, int hour, int minute, int second) {
    Calendar calendarDateTime = Calendar.getInstance();

    calendarDateTime.clear();
    calendarDateTime.set(Calendar.YEAR, year);
    calendarDateTime.set(Calendar.MONTH, month);
    calendarDateTime.set(Calendar.DAY_OF_MONTH, day);
    initializeTime(calendarDateTime, hour, minute, second);

    return new Timestamp(calendarDateTime.getTimeInMillis());
  }
}
