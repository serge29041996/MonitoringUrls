package com.service.checkstatushandler;

import static com.common.StringConverter.entityToString;

import com.common.StatusInfo;
import com.common.entities.StatusUrl;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

public class StringInResponseHandler extends BasicStatusHandler {
  @Override
  public void check(HttpResponse response, StatusUrl statusUrl, StatusInfo statusInfo) {
    HttpEntity entity = response.getEntity();
    String contentResponseString = entityToString(entity, statusUrl, statusInfo);
    if (!contentResponseString.contains(statusUrl
        .getParametersMonitoringUrl().getSubstringResponse())) {
      statusUrl.setStatus(STATUS_CRITICAL);
      statusInfo.setCauseStatus("Response does not have need string");
    }
  }
}
