package com.common;

import java.util.Calendar;
import java.util.Date;

/**
 * Util class for formatted data
 */
public class DataUtils {
  private DataUtils() {
  }

  public static Date getFormattedISOTime(int hour, int minute, int second) {
    Calendar calendarTime = Calendar.getInstance();

    calendarTime.clear();
    calendarTime.set(Calendar.HOUR_OF_DAY, hour);
    calendarTime.set(Calendar.MINUTE, minute);
    calendarTime.set(Calendar.SECOND, second);

    return calendarTime.getTime();
  }
}
