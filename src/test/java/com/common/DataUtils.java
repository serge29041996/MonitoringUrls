package com.common;

import com.common.entities.ParametersMonitoringUrl;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Util class for formatted data
 */
public class DataUtils {
  private DataUtils() {
  }

  /*
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
  */

  public static LocalTime getFormattedISOTime(int hour, int minute, int seconds) {
    return LocalTime.of(hour, minute, seconds);
  }

  /*
  public static Timestamp getFormattedISODateTime(int year, int month, int day, int hour,
                                                  int minute, int second) {
    Calendar calendarDateTime = Calendar.getInstance();
    calendarDateTime.clear();
    calendarDateTime.set(Calendar.YEAR, year);
    calendarDateTime.set(Calendar.MONTH, month);
    calendarDateTime.set(Calendar.DAY_OF_MONTH, day);
    initializeTime(calendarDateTime, hour, minute, second);
    return new Timestamp(calendarDateTime.getTimeInMillis());
  }
  */

  public static LocalDateTime getFormattedISODateTime(int year, int month, int day, int hour,
                                                      int minute, int second) {
    return LocalDateTime.of(year, month, day, hour, minute, second);
  }

  public static ParametersMonitoringUrl initParametersMonitoring(String url,
                                                                 PeriodMonitoring periodMonitoring,
                                                                 TimeResponseData timeResponseData,
                                                                 int expectedCodeResponse,
                                                                 SizeResponseData sizeResponseData,
                                                                 String substringResponse) {
    return new ParametersMonitoringUrl(url, periodMonitoring, timeResponseData,
        expectedCodeResponse, sizeResponseData, substringResponse);
  }

  public static ParametersMonitoringUrl initValidParametersWithDateMonitoring(
      String url, TimeData beginTimeMonitor, TimeData endTimeMonitor) {
    /*
    Date beginTime = DataUtils.getFormattedISOTime(beginTimeMonitor.getHour(),
        beginTimeMonitor.getMinute(), beginTimeMonitor.getSecond());
    Date endTime = DataUtils.getFormattedISOTime(endTimeMonitor.getHour(),
        endTimeMonitor.getMinute(), endTimeMonitor.getSecond());
        */
    LocalTime beginTime = DataUtils.getFormattedISOTime(beginTimeMonitor.getHour(),
        beginTimeMonitor.getMinute(), beginTimeMonitor.getSecond());
    LocalTime endTime = DataUtils.getFormattedISOTime(endTimeMonitor.getHour(),
        endTimeMonitor.getMinute(), endTimeMonitor.getSecond());
    return new ParametersMonitoringUrl(url, new PeriodMonitoring(beginTime, endTime),
        new TimeResponseData(1,2,3),
        200, new SizeResponseData(1,100));
  }

  public static ParametersMonitoringUrl initValidParametersWithDateMonitoringAndString(
      String url, TimeData beginTimeMonitor, TimeData endTimeMonitor, String substringResponse) {
    ParametersMonitoringUrl parametersMonitoringUrl = initValidParametersWithDateMonitoring(url,
        beginTimeMonitor, endTimeMonitor);
    parametersMonitoringUrl.setSubstringResponse(substringResponse);
    return parametersMonitoringUrl;
  }
}
