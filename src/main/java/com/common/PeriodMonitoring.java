package com.common;

import java.util.Date;

/**
 * Class for saving begin and end times for monitoring.
 */
public class PeriodMonitoring {
  private Date beginTime;
  private Date endTime;

  /**
   * Constructor for period monitoring.
   * @param beginTime begin time of monitoring
   * @param endTime end time of monitoring
   */
  public PeriodMonitoring(Date beginTime, Date endTime) {
    this.beginTime = new Date(beginTime.getTime());
    this.endTime = new Date(endTime.getTime());
  }

  public Date getBeginTime() {
    return new Date(beginTime.getTime());
  }

  public Date getEndTime() {
    return new Date(endTime.getTime());
  }
}
