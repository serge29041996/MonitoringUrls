package com.service.checkstatushandler;

import com.common.StatusInfo;
import com.common.entities.StatusUrl;
import org.apache.http.HttpResponse;

public class ExpectedCodeResponseHandler extends BasicStatusHandler {
  @Override
  public void check(HttpResponse response, StatusUrl statusUrl, StatusInfo statusInfo) {
    int obtainedResponseStatusCode = response.getStatusLine().getStatusCode();
    if (obtainedResponseStatusCode != statusUrl
        .getParametersMonitoringUrl().getExpectedCodeResponse()) {
      statusUrl.setStatus(STATUS_CRITICAL);
      statusInfo.setCauseStatus("Response has unexpected http status code.");
    }

    checkNext(response, statusUrl, statusInfo);
  }
}
