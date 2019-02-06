package com.dao;

import com.MonitoringUrlsSpringClass;
import com.common.DataUtils;
import com.common.PeriodMonitoring;
import com.common.SizeResponseData;
import com.common.TimeResponseData;
import com.common.entities.ParametersMonitoringUrl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {MonitoringUrlsSpringClass.class})
@TestPropertySource(
    locations = "classpath:test.properties")
public class ParametersMonitoringUrlRepositoryIT {
  @Autowired
  private ParametersMonitoringUrlRepository parametersMonitoringUrlRepository;
  private static final String TEST_URL = "test-url";

  @Test
  public void testFindById() {
    Date beginTime = DataUtils.getFormattedISOTime(12, 0, 0);
    Date endTime = DataUtils.getFormattedISOTime(23, 59, 59);
    ParametersMonitoringUrl parametersMonitoringUrl = new ParametersMonitoringUrl("someUrl",
        new PeriodMonitoring(beginTime,
            endTime),
        new TimeResponseData(1, 2, 3)
        , 200, new SizeResponseData(1, 100));
    parametersMonitoringUrl = parametersMonitoringUrlRepository.save(parametersMonitoringUrl);
    ParametersMonitoringUrl findByIdParams = parametersMonitoringUrlRepository
        .findById(parametersMonitoringUrl.getId());

    assertThat(findByIdParams).isEqualTo(parametersMonitoringUrl);
  }

  @Test
  public void testFindByUrl() {
    Date beginTime = DataUtils.getFormattedISOTime(12, 0, 0);
    Date endTime = DataUtils.getFormattedISOTime(23, 59, 59);
    ParametersMonitoringUrl parametersMonitoringUrl = new ParametersMonitoringUrl(TEST_URL,
        new PeriodMonitoring(beginTime,
            endTime),
        new TimeResponseData(1, 2, 3)
        , 200, new SizeResponseData(1, 1000), "test");
    parametersMonitoringUrlRepository.save(parametersMonitoringUrl);
    ParametersMonitoringUrl findByUrlParams = parametersMonitoringUrlRepository
        .findByUrl(parametersMonitoringUrl.getUrl());
    assertThat(findByUrlParams).isEqualTo(parametersMonitoringUrl);
  }

  @Test
  public void testDeleteEntity() {
    Date beginTime = DataUtils.getFormattedISOTime(12, 0, 0);
    Date endTime = DataUtils.getFormattedISOTime(23, 59, 59);
    ParametersMonitoringUrl parametersMonitoringUrl = new ParametersMonitoringUrl(TEST_URL,
        new PeriodMonitoring(beginTime,
            endTime),
        new TimeResponseData(1, 2, 3)
        , 200, new SizeResponseData(1, 1000), "test");
    parametersMonitoringUrl = parametersMonitoringUrlRepository.save(parametersMonitoringUrl);
    parametersMonitoringUrlRepository.delete(parametersMonitoringUrl.getId());
    ParametersMonitoringUrl findByIdParams = parametersMonitoringUrlRepository
        .findById(parametersMonitoringUrl.getId());
    assertThat(findByIdParams).isNull();
  }

  @Test
  public void testUpdateEntity() {
    Date beginTime = DataUtils.getFormattedISOTime(12, 0, 0);
    Date endTime = DataUtils.getFormattedISOTime(23, 59, 59);
    ParametersMonitoringUrl parametersMonitoringUrl = new ParametersMonitoringUrl(TEST_URL,
        new PeriodMonitoring(beginTime,
            endTime),
        new TimeResponseData(1, 2, 3)
        , 200, new SizeResponseData(1, 1000), "test");
    parametersMonitoringUrl = parametersMonitoringUrlRepository.save(parametersMonitoringUrl);
    Date newEndTime = DataUtils.getFormattedISOTime(23,0,0);
    parametersMonitoringUrl.setEndTimeMonitoring(newEndTime);
    parametersMonitoringUrlRepository.save(parametersMonitoringUrl);
    ParametersMonitoringUrl findByIdParams = parametersMonitoringUrlRepository
        .findById(parametersMonitoringUrl.getId());
    long countParams = parametersMonitoringUrlRepository.count();
    assertThat(findByIdParams).isEqualTo(parametersMonitoringUrl);
    assertThat(countParams).isEqualTo(1);
  }

  @Test
  public void testCountEntity() {
    Date beginTime = DataUtils.getFormattedISOTime(12, 0, 0);
    Date endTime = DataUtils.getFormattedISOTime(23, 59, 59);
    ParametersMonitoringUrl parametersMonitoringUrl = new ParametersMonitoringUrl(TEST_URL,
        new PeriodMonitoring(beginTime,
            endTime),
        new TimeResponseData(1, 2, 3)
        , 200, new SizeResponseData(1, 1000), "test");
    parametersMonitoringUrlRepository.save(parametersMonitoringUrl);
    beginTime = DataUtils.getFormattedISOTime(10, 0, 0);
    endTime = DataUtils.getFormattedISOTime(13, 59, 59);
    parametersMonitoringUrl = new ParametersMonitoringUrl(TEST_URL,
        new PeriodMonitoring(beginTime,
            endTime),
        new TimeResponseData(1, 2, 3)
        , 200, new SizeResponseData(1, 1000), "test");
    parametersMonitoringUrlRepository.save(parametersMonitoringUrl);
    long numberEntities = parametersMonitoringUrlRepository.count();
    assertThat(numberEntities).isEqualTo(2);
  }

  @Before
  public void clearDatabase() {
    parametersMonitoringUrlRepository.deleteAll();
  }
}
