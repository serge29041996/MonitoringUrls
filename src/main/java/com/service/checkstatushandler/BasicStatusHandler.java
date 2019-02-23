package com.service.checkstatushandler;

import com.common.StatusInfo;
import com.common.entities.StatusUrl;
import org.apache.http.HttpResponse;

public abstract class BasicStatusHandler {
  private BasicStatusHandler nextStatusHandler;
  protected static final String STATUS_CRITICAL = "CRITICAL";

  public BasicStatusHandler linkWithNextStatusHandler(BasicStatusHandler nextStatusHandler) {
    this.nextStatusHandler = nextStatusHandler;
    return nextStatusHandler;
  }

  public abstract void check(HttpResponse response, StatusUrl statusUrl, StatusInfo statusInfo);

  protected void checkNext(HttpResponse response, StatusUrl statusUrl, StatusInfo statusInfo) {
    if (nextStatusHandler != null && !statusUrl.getStatus().equals(STATUS_CRITICAL)) {
      nextStatusHandler.check(response, statusUrl, statusInfo);
    }
  }
}
