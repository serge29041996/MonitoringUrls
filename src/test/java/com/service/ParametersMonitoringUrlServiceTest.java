package com.service;

import com.common.DataUtils;
import com.common.ParametersMonitoringUrl;
import com.common.exceptions.*;
import com.dao.ParametersMonitoringUrlRepository;
import com.service.ParametersMonitoringUrlService;
import org.aspectj.weaver.ast.Not;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
/*
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {MonitoringUrlsSpringClass.class})
@TestPropertySource(
    locations = "classpath:test.properties")
*/
public class ParametersMonitoringUrlServiceTest {
  @Mock
  private ParametersMonitoringUrlRepository parametersMonitoringUrlRepository;

  @InjectMocks
  ParametersMonitoringUrlService parametersMonitoringUrlService;

  @Before
  public void initialMocks() {
    ParametersMonitoringUrl params = initParameters("someUrl", 12, 0, 0,
        23, 59, 59);

    Mockito.when(parametersMonitoringUrlRepository.findByUrl("someUrl")).thenReturn(params);
    Mockito.when(parametersMonitoringUrlRepository.findByUrl("newUrl")).thenReturn(null);
    Mockito.when(parametersMonitoringUrlRepository.findByUrl("testUrl")).thenReturn(null);
    Mockito.when(parametersMonitoringUrlRepository.findById(100)).thenReturn(null);
    Mockito.when(parametersMonitoringUrlRepository.findById(101)).thenReturn(params);

    ParametersMonitoringUrl newParams = initParameters("newUrl", 12, 0, 0,
        23, 59, 59);
    Mockito.when(parametersMonitoringUrlRepository.save(params)).thenReturn(newParams);
  }

  @Test
  public void testGetParametersByUrlExistingEntity() {
    ParametersMonitoringUrl params = initParameters("someUrl",12, 0, 0, 23,
        59, 59);

    ParametersMonitoringUrl findByUrlParams = parametersMonitoringUrlService.getParametersByUrl("someUrl");
    assertThat(findByUrlParams).isEqualTo(params);
  }

  @Test(expected = NotFoundParametersUrlException.class)
  public void testGetParametersByUrlNotExistingEntity() {
    ParametersMonitoringUrl findByUrlParams = parametersMonitoringUrlService.getParametersByUrl("testUrl");
  }

  @Test
  public void testGetParametersByIdExistingEntity() {
    ParametersMonitoringUrl params = initParameters("someUrl",12, 0, 0,
        23, 59, 59);
    ParametersMonitoringUrl findByIdParams = parametersMonitoringUrlService.getParametersById(101);
    assertThat(findByIdParams).isEqualTo(params);
  }

  @Test(expected = NotFoundParametersUrlException.class)
  public void testGetParametersByIdNotExistEntity() {
    ParametersMonitoringUrl findByIdParams = parametersMonitoringUrlService.getParametersById(100);
  }

  @Test(expected = ExistingParametersUrlException.class)
  public void testSaveExistingParams() {
    ParametersMonitoringUrl params = initParameters("someUrl",12, 0, 0, 23,
        59, 59);
    parametersMonitoringUrlService.saveParametersUrl(params);
  }

  @Test(expected = CompareTimesException.class)
  public void testSaveParamsWithBadTimesValue() {
    ParametersMonitoringUrl params = initParameters("newUrl",12, 0, 0, 11,
        59, 59);
    parametersMonitoringUrlService.saveParametersUrl(params);
  }

  @Test(expected = EqualTimesException.class)
  public void testSaveParamsWithEqualBeginAndEndTimes() {
    ParametersMonitoringUrl params = initParameters("newUrl",12,0,0,12,
        0,0);

    parametersMonitoringUrlService.saveParametersUrl(params);
  }

  @Test
  public void testSaveParamsValidParams() {
    ParametersMonitoringUrl params = initParameters("newUrl",12, 0, 0, 23,
        59, 59);
    parametersMonitoringUrlService.saveParametersUrl(params);
  }

  @Test(expected = NotFoundParametersUrlException.class)
  public void testUpdateParamsNonExistParams() {
    ParametersMonitoringUrl params = initParameters("someUrl",12, 0, 0, 23,
        59, 59);
    parametersMonitoringUrlService.updateParametersUrl(100, params);
  }

  @Test(expected = EqualParametersException.class)
  public void testUpdateParamsEqualParams() {
    ParametersMonitoringUrl params = initParameters("someUrl",12, 0, 0,
        23, 59, 59);
    parametersMonitoringUrlService.updateParametersUrl(101, params);
  }

  @Test(expected = CompareTimesException.class)
  public void testUpdateParamsWithBadTimeValues() {
    ParametersMonitoringUrl newParams = initParameters("someUrl",12, 0, 0,
        11, 59, 59);
    newParams.setMinSizeResponse(100);
    parametersMonitoringUrlService.updateParametersUrl(101, newParams);
  }

  @Test(expected = EqualTimesException.class)
  public void testUpdateParamsEqualBeginAndEndTimes() {
    ParametersMonitoringUrl newParams = initParameters("someUrl",12, 0, 0,
        12, 0, 0);
    newParams.setMinSizeResponse(100);
    parametersMonitoringUrlService.updateParametersUrl(101, newParams);
  }

  @Test
  public void testUpdateParamsValidParams() {
    ParametersMonitoringUrl newParams = initParameters("someUrl",12, 0, 0, 22,
        59, 59);
    newParams.setMinSizeResponse(100);
    parametersMonitoringUrlService.updateParametersUrl(101, newParams);
  }

  @Test(expected = NotFoundParametersUrlException.class)
  public void testDeleteNotExistParams() {
    parametersMonitoringUrlService.deleteParametersMonitoringUrl(100);
  }

  @Test
  public void testDeleteExistParams() {
    ParametersMonitoringUrl params = initParameters("someUrl",12, 0, 0, 23,
        59, 59);
    parametersMonitoringUrlService.deleteParametersMonitoringUrl(101);
  }

  private ParametersMonitoringUrl initParameters(String url, int hourBegin, int minuteBegin, int secondBegin,
                                                 int hourEnd, int minuteEnd, int secondEnd) {
    Date beginTime = DataUtils.getFormattedISOTime(hourBegin, minuteBegin, secondBegin);
    Date endTime = DataUtils.getFormattedISOTime(hourEnd, minuteEnd, secondEnd);

    ParametersMonitoringUrl webParams = new ParametersMonitoringUrl(url, beginTime,
        endTime, 1, 2, 3, 200,
        1, 100);
    return webParams;
  }
}
