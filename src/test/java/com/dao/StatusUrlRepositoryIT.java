package com.dao;

import static org.assertj.core.api.Assertions.assertThat;

import com.MonitoringUrlsSpringClass;
import com.common.DataUtils;
import com.common.PeriodMonitoring;
import com.common.SizeResponseData;
import com.common.TimeResponseData;
import com.common.entities.ParametersMonitoringUrl;
import com.common.entities.StatusUrl;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {MonitoringUrlsSpringClass.class})
@TestPropertySource(
    locations = "classpath:test.properties")
public class StatusUrlRepositoryIT {
  @Autowired
  private ParametersMonitoringUrlRepository parametersMonitoringUrlRepository;
  private static final String TEST_URL = "someUrl";

  @Autowired
  private StatusUrlRepository statusUrlRepository;

  @Test
  public void testSaveStatusUrl() {
    /*
    Date beginTime = DataUtils.getFormattedISOTime(12, 0, 0);
    Date endTime = DataUtils.getFormattedISOTime(23, 59, 59);
    */
    LocalTime beginTime = DataUtils.getFormattedISOTime(12, 0, 0);
    LocalTime endTime = DataUtils.getFormattedISOTime(23, 59, 59);
    ParametersMonitoringUrl parametersMonitoringUrl = new ParametersMonitoringUrl(TEST_URL,
        new PeriodMonitoring(beginTime,
            endTime),
        new TimeResponseData(1, 2, 3)
        , 200, new SizeResponseData(1, 100));
    parametersMonitoringUrl = parametersMonitoringUrlRepository.save(parametersMonitoringUrl);
    StatusUrl statusUrl = new StatusUrl(parametersMonitoringUrl);
    statusUrl.setStatus("CRITICAL");
    statusUrl.setDefiningStatusTime(DataUtils.getFormattedISODateTime(2017, 11,21,
        12,0,0));
    statusUrlRepository.save(statusUrl);
    StatusUrl needStatusUrl = statusUrlRepository.findOne(statusUrl.getId());
    assertThat(statusUrl).isEqualTo(needStatusUrl);
  }

  @Test
  public void testGetLastStatusUrl() {
    /*
    Date beginTime = DataUtils.getFormattedISOTime(12, 0, 0);
    Date endTime = DataUtils.getFormattedISOTime(23, 59, 59);
    */
    LocalTime beginTime = DataUtils.getFormattedISOTime(12, 0, 0);
    LocalTime endTime = DataUtils.getFormattedISOTime(23, 59, 59);
    ParametersMonitoringUrl parametersMonitoringUrl = new ParametersMonitoringUrl(TEST_URL,
        new PeriodMonitoring(beginTime,
            endTime),
        new TimeResponseData(1, 2, 3)
        , 200, new SizeResponseData(1, 100));
    parametersMonitoringUrl = parametersMonitoringUrlRepository.save(parametersMonitoringUrl);
    List<StatusUrl> savedStatusesUrl = saveStatusesUrl(parametersMonitoringUrl);
    StatusUrl lastStatusUrl = statusUrlRepository.
        findTop1ByParametersMonitoringUrlOrderByDefiningStatusTimeDesc(parametersMonitoringUrl);
    assertThat(savedStatusesUrl.get(2)).isEqualTo(lastStatusUrl);
  }

  @Test
  public void testCascadeDeleting() {
    /*
    Date beginTime = DataUtils.getFormattedISOTime(12, 0, 0);
    Date endTime = DataUtils.getFormattedISOTime(23, 59, 59);
    */
    LocalTime beginTime = DataUtils.getFormattedISOTime(12, 0, 0);
    LocalTime endTime = DataUtils.getFormattedISOTime(23, 59, 59);
    ParametersMonitoringUrl parametersMonitoringUrl = new ParametersMonitoringUrl(TEST_URL,
        new PeriodMonitoring(beginTime,
            endTime),
        new TimeResponseData(1, 2, 3)
        , 200, new SizeResponseData(1, 100));
    parametersMonitoringUrl = parametersMonitoringUrlRepository.save(parametersMonitoringUrl);
    List<StatusUrl> savedStatusesUrl = saveStatusesUrl(parametersMonitoringUrl);
    long numberStatuses = statusUrlRepository.count();
    assertThat(numberStatuses).isEqualTo(savedStatusesUrl.size());
    parametersMonitoringUrlRepository.delete(parametersMonitoringUrl.getId());
    List<StatusUrl> expectedStatuses = statusUrlRepository
        .findAllByParametersMonitoringUrl(parametersMonitoringUrl);
    assertThat(expectedStatuses.size()).isEqualTo(0);
  }

  private List<StatusUrl> saveStatusesUrl(ParametersMonitoringUrl parametersMonitoringUrl) {
    List<StatusUrl> statusUrls = new ArrayList<>();
    StatusUrl statusUrl1 = new StatusUrl(parametersMonitoringUrl);
    statusUrl1.setStatus("CRITICAL");
    statusUrl1.setDefiningStatusTime(DataUtils.getFormattedISODateTime(2017, 11,21,
        12,0,0));
    statusUrls.add(statusUrl1);
    statusUrlRepository.save(statusUrl1);
    StatusUrl statusUrl2 = new StatusUrl(parametersMonitoringUrl);
    statusUrl2.setStatus("WARNING");
    statusUrl2.setDefiningStatusTime(DataUtils.getFormattedISODateTime(2017, 11,21,
        11,50,0));
    statusUrls.add(statusUrl2);
    statusUrlRepository.save(statusUrl2);
    StatusUrl statusUrl3 = new StatusUrl(parametersMonitoringUrl);
    statusUrl3.setStatus("OK");
    statusUrl3.setDefiningStatusTime(DataUtils.getFormattedISODateTime(2017, 11,21,
        13,0,0));
    statusUrls.add(statusUrl3);
    statusUrlRepository.save(statusUrl3);
    return statusUrls;
  }

  @Before
  public void clearDatabase() {
    parametersMonitoringUrlRepository.deleteAll();
  }
}
