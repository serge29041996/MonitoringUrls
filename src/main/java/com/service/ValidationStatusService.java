package com.service;

import com.common.entities.ParametersMonitoringUrl;
import com.common.StatusInfo;
import com.common.entities.StatusUrl;
import com.dao.StatusUrlRepository;
import com.service.checkstatushandler.BasicStatusHandler;
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

@Service
public class ValidationStatusService implements IValidationStatusService {
  private static final Logger logger = Logger.getLogger(ValidationStatusService.class);
  private static final String STATUS_OK = "OK";
  private static final String STATUS_WARNING = "WARNING";
  private static final String STATUS_CRITICAL = "CRITICAL";
  private static final String STATUS_UNMONITORED = "UNMONITORED";
  private ParametersMonitoringUrlService parametersMonitoringUrlService;
  private StatusUrlRepository statusUrlRepository;

  @Autowired
  public ValidationStatusService(
      ParametersMonitoringUrlService parametersMonitoringUrlService,
      StatusUrlRepository statusUrlRepository) {
    this.parametersMonitoringUrlService = parametersMonitoringUrlService;
    this.statusUrlRepository = statusUrlRepository;
  }

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

  @Override
  public HttpResponse checkGetResponseTime(StatusUrl statusUrl, StatusInfo statusInfo) {
    int timeout = (int)statusUrl.getParametersMonitoringUrl().getTimeResponseCritical() + 1;
    long calculatedTime = 0;
    HttpResponse httpResponse = null;
    RequestConfig config = RequestConfig.custom().setConnectTimeout(timeout * 1000)
        .setConnectionRequestTimeout(timeout * 1000).setSocketTimeout(timeout * 1000).build();
    CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config)
        .build();
    HttpGet getRequest = new HttpGet(statusUrl.getParametersMonitoringUrl().getUrl());
    getRequest.setHeader(HttpHeaders.ACCEPT_ENCODING, "identity");
    try {
      long startTimeRequest = System.currentTimeMillis();
      httpResponse = httpClient.execute(getRequest);
      calculatedTime = System.currentTimeMillis() - startTimeRequest;
    } catch (IOException e) {
      logger.error("Cannot establish connection", e);
      statusInfo.setCauseStatus("Cannot establish connection with url.");
      statusUrl.setStatus(STATUS_CRITICAL);
    }
    calculatedTime = Math.round((double) calculatedTime / 1000.0D);
    if (!statusUrl.getStatus().equals(STATUS_CRITICAL)) {
      checkStatusForTimeResponse(statusUrl, statusInfo, calculatedTime);
    }
    return httpResponse;
  }

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

  @Override
  public StatusInfo checkResponse(long idUrl) {
    ParametersMonitoringUrl checkingUrl = parametersMonitoringUrlService.getParametersById(idUrl);
    StatusUrl statusUrl = new StatusUrl(checkingUrl);
    StatusInfo statusInfo = new StatusInfo(idUrl);
    if (checkTimeMonitoring(statusUrl)) {
      BasicStatusHandler statusHandler = new ExpectedCodeResponseHandler();
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
