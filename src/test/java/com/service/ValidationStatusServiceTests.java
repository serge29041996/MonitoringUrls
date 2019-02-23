package com.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.common.DataUtils;
import com.common.TestServer;
import com.common.StatusInfo;
import com.common.TimeData;
import com.common.entities.ParametersMonitoringUrl;
import com.common.entities.StatusUrl;
import com.dao.StatusUrlRepository;
import java.io.ByteArrayInputStream;
import java.sql.Timestamp;
import java.util.Calendar;
import org.apache.http.HttpResponse;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ValidationStatusServiceTests {
  private static final String STATUS_OK = "OK";
  private static final String STATUS_WARNING = "WARNING";
  private static final String STATUS_CRITICAL = "CRITICAL";
  private static final String EXIST_URL = "/existUrl";
  private ParametersMonitoringUrl parametersMonitoringUrl;
  private static TestServer serverTest = new TestServer(5);
  private static String serverUrl;
  private static final Logger logger = Logger.getLogger(ValidationStatusServiceTests.class);

  @Mock
  private StatusUrlRepository statusUrlRepository;

  @Mock
  private ParametersMonitoringUrlService parametersMonitoringUrlService;

  @InjectMocks
  private ValidationStatusService validationStatusService;

  @BeforeClass
  public static void setUp() {
    try {
      serverTest.setUp();
      setHandlersForRequests();
      serverUrl = serverTest.getServerUrl();
    } catch (Exception e) {
      logger.error("Error with set up of server");
    }
  }

  @Test
  public void testValidTimeForDefiningStatus() {
    Calendar currentDateTime = Calendar.getInstance();
    parametersMonitoringUrl = DataUtils.initValidParametersWithDateMonitoring("url",
        new TimeData(currentDateTime.get(Calendar.HOUR_OF_DAY),
            currentDateTime.get(Calendar.MINUTE) - 1, 0),
        new TimeData(currentDateTime.get(Calendar.HOUR_OF_DAY),
            currentDateTime.get(Calendar.MINUTE) + 1, 0));
    StatusUrl statusUrl = new StatusUrl(parametersMonitoringUrl);
    assertThat(statusUrl.getStatus()).isEqualTo(STATUS_OK);
    boolean isCanDefineStatus =  validationStatusService.checkTimeMonitoring(statusUrl);
    assertThat(statusUrl.getStatus()).isEqualTo("OK");
    assertThat(isCanDefineStatus).isEqualTo(true);
  }

  @Test
  public void testInvalidTimeForDefiningStatus() {
    Calendar currentDateTime = Calendar.getInstance();
    parametersMonitoringUrl = DataUtils.initValidParametersWithDateMonitoring("url",
        new TimeData(currentDateTime.get(Calendar.HOUR_OF_DAY),
            currentDateTime.get(Calendar.MINUTE) + 10, 0),
        new TimeData(currentDateTime.get(Calendar.HOUR_OF_DAY),
            currentDateTime.get(Calendar.MINUTE) + 11, 0));
    StatusUrl statusUrl = new StatusUrl(parametersMonitoringUrl);
    assertThat(statusUrl.getStatus()).isEqualTo(STATUS_OK);
    boolean isCanDefineStatus =  validationStatusService.checkTimeMonitoring(statusUrl);
    assertThat(isCanDefineStatus).isEqualTo(false);
    assertThat(statusUrl.getStatus()).isEqualTo("UNMONITORED");
  }

  @Test
  public void testGetResponseTimeForUnexistUrl() {
    Calendar currentDateTime = Calendar.getInstance();
    parametersMonitoringUrl = DataUtils.initValidParametersWithDateMonitoring("url",
        new TimeData(currentDateTime.get(Calendar.HOUR_OF_DAY),
            currentDateTime.get(Calendar.MINUTE) - 1, 0),
        new TimeData(currentDateTime.get(Calendar.HOUR_OF_DAY),
            currentDateTime.get(Calendar.MINUTE) + 1, 0));
    StatusUrl statusUrl = new StatusUrl(parametersMonitoringUrl);
    StatusInfo statusInfo = new StatusInfo(1);
    HttpResponse response = validationStatusService.checkGetResponseTime(statusUrl, statusInfo);
    assertThat(response).isNull();
    assertThat(statusUrl.getStatus()).isEqualTo(STATUS_CRITICAL);
    assertThat(statusInfo.getCauseStatus()).isEqualTo("Cannot establish connection with url.");
  }

  @Test
  public void testGetResponseTimeForExistUrl() {
    Calendar currentDateTime = Calendar.getInstance();
    parametersMonitoringUrl = DataUtils.initValidParametersWithDateMonitoring(serverUrl +
            EXIST_URL,
        new TimeData(currentDateTime.get(Calendar.HOUR_OF_DAY),
            currentDateTime.get(Calendar.MINUTE) - 1, 0),
        new TimeData(currentDateTime.get(Calendar.HOUR_OF_DAY),
            currentDateTime.get(Calendar.MINUTE) + 1, 0));
    StatusUrl statusUrl = new StatusUrl(parametersMonitoringUrl);
    StatusInfo statusInfo = new StatusInfo(1);
    HttpResponse response = validationStatusService.checkGetResponseTime(statusUrl, statusInfo);
    assertThat(response).isNotNull();
    assertThat(statusUrl.getStatus()).isEqualTo(STATUS_OK);
    assertThat(statusInfo.getCauseStatus()).isEqualTo("");
  }

  @Test
  public void testCheckStatusForTimeResponseWarning() {
    Calendar currentDateTime = Calendar.getInstance();
    parametersMonitoringUrl = DataUtils.initValidParametersWithDateMonitoring("url",
        new TimeData(currentDateTime.get(Calendar.HOUR_OF_DAY),
            currentDateTime.get(Calendar.MINUTE) - 1, 0),
        new TimeData(currentDateTime.get(Calendar.HOUR_OF_DAY),
            currentDateTime.get(Calendar.MINUTE) + 1, 0));
    StatusUrl statusUrl = new StatusUrl(parametersMonitoringUrl);
    StatusInfo statusInfo = new StatusInfo(1);
    validationStatusService.checkStatusForTimeResponse(statusUrl, statusInfo, 2);
    assertThat(statusUrl.getStatus()).isEqualTo(STATUS_WARNING);
    assertThat(statusInfo.getCauseStatus()).isEqualTo("Time of response is greater than for status OK.");
  }

  @Test
  public void testCheckStatusForTimeResponseCritical() {
    Calendar currentDateTime = Calendar.getInstance();
    parametersMonitoringUrl = DataUtils.initValidParametersWithDateMonitoring("url",
        new TimeData(currentDateTime.get(Calendar.HOUR_OF_DAY),
            currentDateTime.get(Calendar.MINUTE) - 1, 0),
        new TimeData(currentDateTime.get(Calendar.HOUR_OF_DAY),
            currentDateTime.get(Calendar.MINUTE) + 1, 0));
    StatusUrl statusUrl = new StatusUrl(parametersMonitoringUrl);
    StatusInfo statusInfo = new StatusInfo(1);
    validationStatusService.checkStatusForTimeResponse(statusUrl, statusInfo, 3);
    assertThat(statusUrl.getStatus()).isEqualTo(STATUS_CRITICAL);
    assertThat(statusInfo.getCauseStatus()).isEqualTo("Time of response is greater than for status Warning.");
  }

  @Test
  public void testCheckResponseForUnmonitoredTime() {
    Calendar currentDateTime = Calendar.getInstance();
    parametersMonitoringUrl = DataUtils.initValidParametersWithDateMonitoring("url",
        new TimeData(currentDateTime.get(Calendar.HOUR_OF_DAY),
            currentDateTime.get(Calendar.MINUTE) + 10, 0),
        new TimeData(currentDateTime.get(Calendar.HOUR_OF_DAY),
            currentDateTime.get(Calendar.MINUTE) + 10, 0));
    Mockito.when(parametersMonitoringUrlService.getParametersById(101))
        .thenReturn(parametersMonitoringUrl);
    StatusInfo statusInfo = validationStatusService.checkResponse(101);
    assertThat(statusInfo.getStatus()).isEqualTo("UNMONITORED");
    assertThat(statusInfo.getCauseStatus()).isEqualTo("Url should not monitor now");
  }

  @Test
  public void testCheckResponseForMonitorTime() {
    Calendar currentDateTime = Calendar.getInstance();
    parametersMonitoringUrl = DataUtils.initValidParametersWithDateMonitoring(serverUrl +
            EXIST_URL,
        new TimeData(currentDateTime.get(Calendar.HOUR_OF_DAY),
            currentDateTime.get(Calendar.MINUTE) - 1, 0),
        new TimeData(currentDateTime.get(Calendar.HOUR_OF_DAY),
            currentDateTime.get(Calendar.MINUTE) + 1, 0));
    Mockito.when(parametersMonitoringUrlService.getParametersById(102))
        .thenReturn(parametersMonitoringUrl);
    StatusUrl statusUrl = new StatusUrl(parametersMonitoringUrl);
    statusUrl.setStatus(STATUS_OK);
    Calendar dateTimeWritingDefineStatus = Calendar.getInstance();
    dateTimeWritingDefineStatus.set(Calendar.MINUTE, currentDateTime.get(Calendar.MINUTE) + 1);
    statusUrl.setDefiningStatusTime(new Timestamp(dateTimeWritingDefineStatus.getTimeInMillis()));
    Mockito.when(statusUrlRepository.saveAndFlush(statusUrl)).thenReturn(statusUrl);
    StatusInfo statusInfo = validationStatusService.checkResponse(102);
    assertThat(statusInfo.getStatus()).isEqualTo("OK");
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
    serverTest.setHandlerForRequest(EXIST_URL,
        (httpRequest, httpResponse, httpContext) -> {
          BasicHttpEntity entity = new BasicHttpEntity();
          entity.setContent(new ByteArrayInputStream("Some text".getBytes()));
          httpResponse.setEntity(entity);
        });
  }
}
