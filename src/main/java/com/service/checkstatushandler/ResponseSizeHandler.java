package com.service.checkstatushandler;

import static com.common.StringConverter.entityToString;

import com.common.StatusInfo;
import com.common.entities.StatusUrl;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

public class ResponseSizeHandler extends BasicStatusHandler {
  @Override
  public void check(HttpResponse response, StatusUrl statusUrl, StatusInfo statusInfo) {
    String contentResponseString;
    HttpEntity entity = response.getEntity();
    long responseSize = entity.getContentLength();
    if (responseSize < 0) {
      contentResponseString = entityToString(entity, statusUrl, statusInfo);
      if (contentResponseString != null) {
        responseSize = contentResponseString.length();
      }
    }
    if (responseSize > statusUrl.getParametersMonitoringUrl().getMaxSizeResponse()
        || responseSize < statusUrl.getParametersMonitoringUrl().getMinSizeResponse()) {
      statusUrl.setStatus(STATUS_CRITICAL);
      statusInfo.setCauseStatus("Size of response does not belong to the range of size.");
    }
  }
}
