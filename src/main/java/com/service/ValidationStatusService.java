/*
 * Copyright (C) 2019.
 * This file is part of project MonitoringUrls
 * Written by Sergiy Krasnikov <sergei29041996@gmail.com>
 */

package com.service;

import com.common.StatusInfo;
import com.common.entities.ParametersMonitoringUrl;
import com.common.entities.StatusUrl;
import com.common.exceptions.NotFoundParametersUrlException;
import com.dao.StatusUrlRepository;
import com.service.checkstatushandler.AbstractStatusHandler;
import com.service.checkstatushandler.ExpectedCodeResponseHandler;
import com.service.checkstatushandler.ResponseSizeHandler;
import com.service.checkstatushandler.StringInResponseHandler;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * Class for validation status of url.
 */
@Service
public class ValidationStatusService implements IValidationStatusService {
  /** Logger for saving information. */
  private static final Logger LOGGER = Logger.getLogger(ValidationStatusService.class);
  /** String representation of status ok. */
  private static final String STATUS_OK = "OK";
  /** String representation of status warning. */
  private static final String STATUS_WARNING = "WARNING";
  /** String representation of status critical. */
  private static final String STATUS_CRITICAL = "CRITICAL";
  /** String representation of status unmonitored. */
  private static final String STATUS_UNMONITORED = "UNMONITORED";
  private static final int NUMBER_MILLISECONDS_IN_SECOND = 1000;
  /** Service for managing url and its parameters. */
  private ParametersMonitoringUrlService parametersMonitoringUrlService;
  /** Repository for managing with statuses of url. */
  private StatusUrlRepository statusUrlRepository;

  /**
   * Constructor with service for url and its parameters and repository for statuses of url.
   * @param parametersMonitoringUrlService service for managing url and its parameters
   * @param statusUrlRepository repository for managing statuses of url
   */
  @Autowired
  public ValidationStatusService(
      ParametersMonitoringUrlService parametersMonitoringUrlService,
      StatusUrlRepository statusUrlRepository) {
    this.parametersMonitoringUrlService = parametersMonitoringUrlService;
    this.statusUrlRepository = statusUrlRepository;
  }

  /**
   * Checking of permission for monitoring url.
   * Compare current time with parameters begin and end time of monitoring
   * @param statusUrl url, its parameters and status
   * @return true if allowed to monitoring url, otherwise - false
   */
  @Override
  public boolean checkTimeMonitoring(StatusUrl statusUrl) {
    Calendar currentDateTime = Calendar.getInstance();
    Calendar currentTime = Calendar.getInstance();
    currentTime.clear();
    currentTime.set(Calendar.HOUR_OF_DAY, currentDateTime.get(Calendar.HOUR_OF_DAY));
    currentTime.set(Calendar.MINUTE, currentDateTime.get(Calendar.MINUTE));
    currentTime.set(Calendar.SECOND, currentDateTime.get(Calendar.SECOND));
    long currentTimeInMilliseconds = currentTime.getTimeInMillis();
    if (currentTimeInMilliseconds >= statusUrl.getParametersMonitoringUrl()
        .getBeginTimeMonitoring().getTime() && currentTimeInMilliseconds <= statusUrl
        .getParametersMonitoringUrl().getEndTimeMonitoring().getTime()) {
      return true;
    } else {
      statusUrl.setStatus(STATUS_UNMONITORED);
      return false;
    }
  }

  /**
   * Get response time.
   * Begin action is configuration of request and sending it.
   * If response has not been obtained set status of url as critical.
   * Else check which status set for url by time of response.
   * @param statusUrl url, its parameters and status
   * @param statusInfo status and cause of status
   * @return obtained http response
   */
  @Override
  public HttpResponse checkGetResponseTime(StatusUrl statusUrl, StatusInfo statusInfo) {
    int timeout = (int)statusUrl.getParametersMonitoringUrl().getTimeResponseCritical() + 1;
    long calculatedTime = 0;
    HttpResponse httpResponse = null;
    RequestConfig config = RequestConfig.custom()
        .setConnectTimeout(timeout * NUMBER_MILLISECONDS_IN_SECOND)
        .setConnectionRequestTimeout(timeout * NUMBER_MILLISECONDS_IN_SECOND)
        .setSocketTimeout(timeout * NUMBER_MILLISECONDS_IN_SECOND).build();
    CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config)
        .build();
    HttpGet getRequest = new HttpGet(statusUrl.getParametersMonitoringUrl().getUrl());
    getRequest.setHeader(HttpHeaders.ACCEPT_ENCODING, "identity");
    try {
      long startTimeRequest = System.currentTimeMillis();
      httpResponse = httpClient.execute(getRequest);
      calculatedTime = System.currentTimeMillis() - startTimeRequest;
    } catch (IOException e) {
      LOGGER.error("Cannot establish connection", e);
      statusInfo.setCauseStatus("Cannot establish connection with url.");
      statusUrl.setStatus(STATUS_CRITICAL);
    }
    calculatedTime = Math.round((double) calculatedTime / (double) NUMBER_MILLISECONDS_IN_SECOND);
    if (!statusUrl.getStatus().equals(STATUS_CRITICAL)) {
      checkStatusForTimeResponse(statusUrl, statusInfo, calculatedTime);
    }
    return httpResponse;
  }

  /**
   * Check status by time of response.
   * @param statusUrl url, its parameters and status
   * @param statusInfo status and cause of status
   * @param calculatedTime time of response in seconds
   */
  @Override
  public void checkStatusForTimeResponse(StatusUrl statusUrl, StatusInfo statusInfo,
                                         long calculatedTime) {
    if (calculatedTime <= statusUrl.getParametersMonitoringUrl().getTimeResponseOk()) {
      statusUrl.setStatus(STATUS_OK);
    } else if (
        calculatedTime <= statusUrl.getParametersMonitoringUrl().getTimeResponseWarning()) {
      statusUrl.setStatus(STATUS_WARNING);
      statusInfo.setCauseStatus("Time of response is greater than for status OK.");
    } else {
      statusUrl.setStatus(STATUS_CRITICAL);
      statusInfo.setCauseStatus("Time of response is greater than for status Warning.");
    }
  }

  /**
   * Validate status of url.
   * First action is checking opportunity of monitoring by comparing current time with
   * parameters begin and end time of monitoring
   * After that we execute sequence of handler for defining status url:
   * - check by time of response;
   * - check by expected code of response;
   * - check by size of response;
   * - (optional) check by containing string in http response
   * @param idUrl id of url
   * @return defined status of url and cause of status
   * @throws NotFoundParametersUrlException parameters for url has not found
   */
  @Override
  public StatusInfo checkResponse(long idUrl) throws NotFoundParametersUrlException {
    ParametersMonitoringUrl checkingUrl = parametersMonitoringUrlService.getParametersById(idUrl);
    StatusUrl statusUrl = new StatusUrl(checkingUrl);
    StatusInfo statusInfo = new StatusInfo(idUrl);
    if (checkTimeMonitoring(statusUrl)) {
      AbstractStatusHandler statusHandler = new ExpectedCodeResponseHandler();
      statusHandler.linkWithNextStatusHandler(new ResponseSizeHandler());
      if (statusUrl.getParametersMonitoringUrl().getSubstringResponse() != null) {
        statusHandler.linkWithNextStatusHandler(new StringInResponseHandler());
      }
      HttpResponse httpResponse;
      httpResponse = checkGetResponseTime(statusUrl, statusInfo);
      if (!statusUrl.getStatus().equals(STATUS_CRITICAL)) {
        statusHandler.check(httpResponse, statusUrl, statusInfo);
      }
      statusUrl.setDefiningStatusTime(new Timestamp(Calendar.getInstance().getTimeInMillis()));
      statusUrlRepository.saveAndFlush(statusUrl);
    } else {
      statusInfo.setCauseStatus("Url should not monitor now");
    }
    statusInfo.setStatus(statusUrl.getStatus());
    return statusInfo;
  }
}
