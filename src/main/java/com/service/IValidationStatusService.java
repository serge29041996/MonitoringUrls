package com.service;

import com.common.StatusInfo;
import com.common.entities.StatusUrl;
import org.apache.http.HttpResponse;

public interface IValidationStatusService {
  boolean checkTimeMonitoring(StatusUrl statusUrl);

  HttpResponse checkGetResponseTime(StatusUrl statusUrl, StatusInfo statusInfo);

  void checkStatusForTimeResponse(StatusUrl statusUrl, StatusInfo statusInfo, long calculatedTime);

  StatusInfo checkResponse(long idUrl);
}
