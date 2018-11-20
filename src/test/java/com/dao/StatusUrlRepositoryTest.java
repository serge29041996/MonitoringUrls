package com.dao;

import com.MonitoringUrlsSpringClass;
import com.common.DataUtils;
import com.common.ParametersMonitoringUrl;
import com.common.StatusUrl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {MonitoringUrlsSpringClass.class})
@TestPropertySource(
    locations = "classpath:test.properties")
public class StatusUrlRepositoryTest {
  @Autowired
  private ParametersMonitoringUrlRepository parametersMonitoringUrlRepository;

  @Autowired
  private StatusUrlRepository statusUrlRepository;

  @Test
  public void testSaveStatusUrl() {
    Date beginTime = DataUtils.getFormattedISOTime(12, 0, 0);
    Date endTime = DataUtils.getFormattedISOTime(23, 59, 59);

    ParametersMonitoringUrl parametersMonitoringUrl = new ParametersMonitoringUrl("someUrl", beginTime,
        endTime, 1, 2, 3, 200,
        1, 100);
    parametersMonitoringUrl = parametersMonitoringUrlRepository.save(parametersMonitoringUrl);

    StatusUrl statusUrl = new StatusUrl(parametersMonitoringUrl);
    statusUrl.setStatus("CRITICAL");
    statusUrl.setDefiningStatusTime(DataUtils.getFormattedISODateTime(2017, 11,21,
        12,0,0));
    statusUrl = statusUrlRepository.save(statusUrl);

    StatusUrl needStatusUrl = statusUrlRepository.findOne(statusUrl.getId());

    assertThat(needStatusUrl).isEqualTo(statusUrl);
  }

  @Test
  public void testGetLastStatusUrl() {
    Date beginTime = DataUtils.getFormattedISOTime(12, 0, 0);
    Date endTime = DataUtils.getFormattedISOTime(23, 59, 59);

    ParametersMonitoringUrl parametersMonitoringUrl = new ParametersMonitoringUrl("someUrl", beginTime,
        endTime, 1, 2, 3, 200,
        1, 100);
    parametersMonitoringUrl = parametersMonitoringUrlRepository.save(parametersMonitoringUrl);

    StatusUrl statusUrl1 = new StatusUrl(parametersMonitoringUrl);
    statusUrl1.setStatus("CRITICAL");
    statusUrl1.setDefiningStatusTime(DataUtils.getFormattedISODateTime(2017, 11,21,
        12,0,0));
    statusUrlRepository.save(statusUrl1);

    StatusUrl statusUrl2 = new StatusUrl(parametersMonitoringUrl);
    statusUrl2.setStatus("WARNING");
    statusUrl2.setDefiningStatusTime(DataUtils.getFormattedISODateTime(2017, 11,21,
        11,50,0));
    statusUrlRepository.save(statusUrl2);

    StatusUrl statusUrl3 = new StatusUrl(parametersMonitoringUrl);
    statusUrl3.setStatus("OK");
    statusUrl3.setDefiningStatusTime(DataUtils.getFormattedISODateTime(2017, 11,21,
        13,0,0));
    statusUrl3 = statusUrlRepository.save(statusUrl3);

    StatusUrl lastStatusUrl = statusUrlRepository.
        findTop1ByParametersMonitoringUrlOrderByDefiningStatusTimeDesc(parametersMonitoringUrl);

    assertThat(lastStatusUrl).isEqualTo(statusUrl3);
  }

  @Test
  public void testCascadeDeleting() {
    Date beginTime = DataUtils.getFormattedISOTime(12, 0, 0);
    Date endTime = DataUtils.getFormattedISOTime(23, 59, 59);

    ParametersMonitoringUrl parametersMonitoringUrl = new ParametersMonitoringUrl("someUrl", beginTime,
        endTime, 1, 2, 3, 200,
        1, 100);
    parametersMonitoringUrl = parametersMonitoringUrlRepository.save(parametersMonitoringUrl);

    StatusUrl statusUrl1 = new StatusUrl(parametersMonitoringUrl);
    statusUrl1.setStatus("CRITICAL");
    statusUrl1.setDefiningStatusTime(DataUtils.getFormattedISODateTime(2017, 11,21,
        12,0,0));
    statusUrlRepository.save(statusUrl1);

    StatusUrl statusUrl2 = new StatusUrl(parametersMonitoringUrl);
    statusUrl2.setStatus("WARNING");
    statusUrl2.setDefiningStatusTime(DataUtils.getFormattedISODateTime(2017, 11,21,
        11,50,0));
    statusUrlRepository.save(statusUrl2);

    StatusUrl statusUrl3 = new StatusUrl(parametersMonitoringUrl);
    statusUrl3.setStatus("OK");
    statusUrl3.setDefiningStatusTime(DataUtils.getFormattedISODateTime(2017, 11,21,
        13,0,0));
    statusUrlRepository.save(statusUrl3);

    long numberStatuses = statusUrlRepository.count();

    assertThat(numberStatuses).isEqualTo(3);

    parametersMonitoringUrlRepository.delete(parametersMonitoringUrl.getId());

    List<StatusUrl> expectedStatuses = statusUrlRepository
        .findAllByParametersMonitoringUrl(parametersMonitoringUrl);

    assertThat(expectedStatuses.size()).isEqualTo(0);
  }

  @Before
  public void clearDatabase() {
    parametersMonitoringUrlRepository.deleteAll();
  }
}
