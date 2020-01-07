/*
 * Copyright (C) 2019.
 * This file is part of project MonitoringUrls
 * Written by Sergiy Krasnikov <sergei29041996@gmail.com>
 */

package com.common;

import java.time.LocalTime;

/**
 * Class for saving begin and end times for monitoring.
 */
public class PeriodMonitoring {
  /** Begin time of monitoring. */
  // private Date beginTime;
  private LocalTime beginTime;
  /** End time of monitoring. */
  // private Date endTime;
  private LocalTime endTime;

  /**
   * Constructor for period monitoring.
   * @param beginTime begin time of monitoring
   * @param endTime end time of monitoring
   */
  /*
  public PeriodMonitoring(Date beginTime, Date endTime) {
    this.beginTime = new Date(beginTime.getTime());
    this.endTime = new Date(endTime.getTime());
  }
  */
  public PeriodMonitoring(LocalTime beginTime, LocalTime endTime) {
    this.beginTime = beginTime;
    this.endTime = endTime;
  }

  /**
   * Return begin time.
   * @return value of begin time
   */
  /*
  public Date getBeginTime() {
    return new Date(beginTime.getTime());
  }
  */
  public LocalTime getBeginTime() {
    return beginTime;
  }

  /**
   * Return end time.
   * @return value of end time
   */
  /*
  public Date getEndTime() {
    return new Date(endTime.getTime());
  }
  */
  public LocalTime getEndTime() {
    return endTime;
  }
}
