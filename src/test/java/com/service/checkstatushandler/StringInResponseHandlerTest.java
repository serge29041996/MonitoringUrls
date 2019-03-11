package com.service.checkstatushandler;

import static org.assertj.core.api.Assertions.assertThat;

import com.common.DataUtils;
import com.common.StatusInfo;
import com.common.TestServer;
import com.common.TimeData;
import com.common.entities.ParametersMonitoringUrl;
import com.common.entities.StatusUrl;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StringInResponseHandlerTest {
  private static final String STATUS_OK = "OK";
  private static TestServer serverTest = new TestServer(5);
  private static String serverUrl;
  private static StringInResponseHandler stringInResponseHandler;
  private static ParametersMonitoringUrl parametersMonitoringUrl;

  private static final Logger logger = Logger.getLogger(StringInResponseHandlerTest.class);

  @BeforeClass
  public static void setUp() {
    try {
      serverTest.setUp();
      setHandlersForRequests();
      serverUrl = serverTest.getServerUrl();
      stringInResponseHandler = new StringInResponseHandler();
      parametersMonitoringUrl = DataUtils.initValidParametersWithDateMonitoringAndString("url",
          new TimeData(14,0,0), new TimeData(23,0,0),
          "test");
      parametersMonitoringUrl.setId(1);
    } catch (Exception e) {
      logger.error("Error with set up of server");
    }
  }

  @Test
  public void testResponseWithExistString() throws IOException {
    StatusUrl statusUrl = new StatusUrl(parametersMonitoringUrl);
    assertThat(statusUrl.getStatus()).isEqualTo(STATUS_OK);
    StatusInfo statusInfo = new StatusInfo(parametersMonitoringUrl.getId());
    HttpClient httpClient  = HttpClients.custom().build();
    HttpGet method = new HttpGet(serverUrl + "/responseWithString");
    HttpResponse response = httpClient.execute(method);
    stringInResponseHandler.check(response, statusUrl, statusInfo);
    assertThat(statusUrl.getStatus()).isEqualTo(STATUS_OK);
    assertThat(statusInfo.getCauseStatus()).isEqualTo("");
  }

  @Test
  public void testResponseWithoutString() throws IOException {
    StatusUrl statusUrl = new StatusUrl(parametersMonitoringUrl);
    assertThat(statusUrl.getStatus()).isEqualTo(STATUS_OK);
    StatusInfo statusInfo = new StatusInfo(parametersMonitoringUrl.getId());
    HttpClient httpClient  = HttpClients.custom().build();
    HttpGet method = new HttpGet(serverUrl + "/responseWithoutString");
    HttpResponse response = httpClient.execute(method);
    stringInResponseHandler.check(response, statusUrl, statusInfo);
    assertThat(statusUrl.getStatus()).isEqualTo("CRITICAL");
    assertThat(statusInfo.getCauseStatus()).isEqualTo("Response does not have need string");
  }

  @Test
  public void testResponseWithStringAndWithoutCharset() throws IOException {
    StatusUrl statusUrl = new StatusUrl(parametersMonitoringUrl);
    assertThat(statusUrl.getStatus()).isEqualTo(STATUS_OK);
    StatusInfo statusInfo = new StatusInfo(parametersMonitoringUrl.getId());
    HttpClient httpClient  = HttpClients.custom().build();
    HttpGet method = new HttpGet(serverUrl + "/responseWithoutCharset");
    HttpResponse response = httpClient.execute(method);
    stringInResponseHandler.check(response, statusUrl, statusInfo);
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
    serverTest.setHandlerForRequest("/responseWithString",
        (httpRequest, httpResponse, httpContext) ->
            httpResponse.setEntity(new StringEntity("This is a test")));
    serverTest.setHandlerForRequest("/responseWithoutString",
        (httpRequest, httpResponse, httpContext) ->
            httpResponse.setEntity(new StringEntity("This is some response")));
    serverTest.setHandlerForRequest("/responseWithoutCharset",
        (httpRequest, httpResponse, httpContext) -> {
          BasicHttpEntity entity = new BasicHttpEntity();
          entity.setContent(new ByteArrayInputStream("This is a test".getBytes()));
          httpResponse.setEntity(entity);
          httpResponse.removeHeader(new BasicHeader(HttpHeaders.CONTENT_TYPE, "ISO-8859-1"));
        });
  }
}
