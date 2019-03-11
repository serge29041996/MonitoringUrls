/*
 * Copyright (C) 2019.
 * This file is part of project MonitoringUrls
 * Written by Sergiy Krasnikov <sergei29041996@gmail.com>
 */

package com.service;

import com.common.StatusInfo;
import com.common.entities.StatusUrl;
import com.common.exceptions.NotFoundParametersUrlException;
import org.apache.http.HttpResponse;

/**
 * Interface for validation status for url.
 */
public interface IValidationStatusService {
  /**
   * Checking of permission for monitoring url.
   * @param statusUrl url, its parameters and status
   * @return true if allowed to monitoring url, otherwise - false
   */
  boolean checkTimeMonitoring(StatusUrl statusUrl);

  /**
   * Get response time.
   * @param statusUrl url, its parameters and status
   * @param statusInfo status and cause of status
   * @return obtained http response
   */
  HttpResponse checkGetResponseTime(StatusUrl statusUrl, StatusInfo statusInfo);

  /**
   * Check status by time of response.
   * @param statusUrl url, its parameters and status
   * @param statusInfo status and cause of status
   * @param calculatedTime time of response in seconds
   */
  void checkStatusForTimeResponse(StatusUrl statusUrl, StatusInfo statusInfo, long calculatedTime);

  /**
   * Validate status of url.
   * @param idUrl id of url
   * @return defined status of url and cause of status
   * @throws NotFoundParametersUrlException parameters for url has not found
   */
  StatusInfo checkResponse(long idUrl) throws NotFoundParametersUrlException;
}
