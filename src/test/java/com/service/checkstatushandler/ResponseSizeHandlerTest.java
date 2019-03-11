package com.service.checkstatushandler;

import static org.assertj.core.api.Assertions.assertThat;

import com.common.DataUtils;
import com.common.TestServer;
import com.common.StatusInfo;
import com.common.TimeData;
import com.common.entities.ParametersMonitoringUrl;
import com.common.entities.StatusUrl;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ResponseSizeHandlerTest {
  private static final String STATUS_OK = "OK";
  private static TestServer serverTest = new TestServer(5);
  private static String serverUrl;
  private static ResponseSizeHandler responseSizeHandler;
  private static ParametersMonitoringUrl parametersMonitoringUrl;

  private static final Logger logger = Logger.getLogger(ResponseSizeHandlerTest.class);

  @BeforeClass
  public static void setUp() {
    try {
      serverTest.setUp();
      setHandlersForRequests();
      serverUrl = serverTest.getServerUrl();
      responseSizeHandler = new ResponseSizeHandler();
      parametersMonitoringUrl = DataUtils.initValidParametersWithDateMonitoring("url",
          new TimeData(14,0,0), new TimeData(23,0,0));
      parametersMonitoringUrl.setId(1);
    } catch (Exception e) {
      logger.error("Error with set up of server");
    }
  }

  @Test
  public void testInvalidSizeResponse() throws IOException {
    StatusUrl statusUrl = new StatusUrl(parametersMonitoringUrl);
    assertThat(statusUrl.getStatus()).isEqualTo(STATUS_OK);
    StatusInfo statusInfo = new StatusInfo(parametersMonitoringUrl.getId());
    HttpClient httpClient  = HttpClients.custom().build();
    HttpGet method = new HttpGet(serverUrl + "/invalidResponse");
    HttpResponse response = httpClient.execute(method);
    responseSizeHandler.check(response, statusUrl, statusInfo);
    assertThat(statusUrl.getStatus()).isEqualTo("CRITICAL");
    assertThat(statusInfo.getCauseStatus())
        .isEqualTo("Size of response does not belong to the range of size.");
  }

  @Test
  public void testSizeResponse() throws IOException {
    StatusUrl statusUrl = new StatusUrl(parametersMonitoringUrl);
    assertThat(statusUrl.getStatus()).isEqualTo(STATUS_OK);
    StatusInfo statusInfo = new StatusInfo(parametersMonitoringUrl.getId());
    HttpClient httpClient  = HttpClients.custom().build();
    HttpGet method = new HttpGet(serverUrl + "/responseWithoutContentLength");
    HttpResponse response = httpClient.execute(method);
    responseSizeHandler.check(response, statusUrl, statusInfo);
    assertThat(statusUrl.getStatus()).isEqualTo(STATUS_OK);
    assertThat(statusInfo.getCauseStatus()).isEqualTo("");
  }

  @AfterClass
  public static void shutDownServer() {
    try {
      serverTest.shutDown();
    } catch (Exception e) {
      logger.error("Error with shut down of server");
    }
  }

  private static void setHandlersForRequests() {
    serverTest.setHandlerForRequest("/invalidResponse",
        (httpRequest, httpResponse, httpContext) ->
            httpResponse.setStatusCode(HttpStatus.SC_OK));
    serverTest.setHandlerForRequest("/responseWithoutContentLength",
        (httpRequest, httpResponse, httpContext) -> {
          BasicHttpEntity entity = new BasicHttpEntity();
          entity.setContent(new ByteArrayInputStream("Hello".getBytes()));
          httpResponse.setEntity(entity);
        }
    );
  }
}
