package com.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.common.DataUtils;
import com.common.entities.ParametersMonitoringUrl;
import com.common.TimeData;
import com.common.exceptions.CompareTimesException;
import com.common.exceptions.EqualParametersException;
import com.common.exceptions.EqualTimesException;
import com.common.exceptions.ExistingParametersUrlException;
import com.common.exceptions.InvalidExpectedCodeResponseException;
import com.common.exceptions.InvalidSizeResponseException;
import com.common.exceptions.InvalidTimeResponseException;
import com.common.exceptions.NotFoundParametersUrlException;
import com.dao.ParametersMonitoringUrlRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ParametersMonitoringUrlServiceTests {
  @Mock
  private ParametersMonitoringUrlRepository parametersMonitoringUrlRepository;
  private static final String URL_TEST = "someUrl";
  private static final String NEW_URL_TEST = "newUrl";

  @InjectMocks
  ParametersMonitoringUrlService parametersMonitoringUrlService;

  @Before
  public void initialMocks() {
    ParametersMonitoringUrl params = DataUtils.initValidParametersWithDateMonitoring(URL_TEST,
        new TimeData(12, 0, 0),
        new TimeData(23, 59, 59));
    Mockito.when(parametersMonitoringUrlRepository.findByUrl(URL_TEST)).thenReturn(params);
    Mockito.when(parametersMonitoringUrlRepository.findByUrl(NEW_URL_TEST)).thenReturn(null);
    Mockito.when(parametersMonitoringUrlRepository.findByUrl("testUrl")).thenReturn(null);
    Mockito.when(parametersMonitoringUrlRepository.findById(100)).thenReturn(null);
    Mockito.when(parametersMonitoringUrlRepository.findById(101)).thenReturn(params);
    ParametersMonitoringUrl newParams = DataUtils.initValidParametersWithDateMonitoring(NEW_URL_TEST,
        new TimeData(12, 0, 0),
        new TimeData(23, 59, 59));
    Mockito.when(parametersMonitoringUrlRepository.save(params)).thenReturn(newParams);
  }

  @Test
  public void testGetParametersByUrlExistingEntity() {
    ParametersMonitoringUrl params = DataUtils.initValidParametersWithDateMonitoring(URL_TEST,
        new TimeData(12, 0, 0),
        new TimeData(23, 59, 59));
    ParametersMonitoringUrl findByUrlParams = parametersMonitoringUrlService.getParametersByUrl(URL_TEST);
    assertThat(findByUrlParams).isEqualTo(params);
  }

  @Test(expected = NotFoundParametersUrlException.class)
  public void testGetParametersByUrlNotExistingEntity() {
    parametersMonitoringUrlService.getParametersByUrl("testUrl");
  }

  @Test
  public void testGetParametersByIdExistingEntity() {
    ParametersMonitoringUrl params = DataUtils.initValidParametersWithDateMonitoring(URL_TEST,
        new TimeData(12, 0, 0),
        new TimeData(23, 59, 59));
    ParametersMonitoringUrl findByIdParams = parametersMonitoringUrlService.getParametersById(101);
    assertThat(findByIdParams).isEqualTo(params);
  }

  @Test(expected = NotFoundParametersUrlException.class)
  public void testGetParametersByIdNotExistEntity() {
    parametersMonitoringUrlService.getParametersById(100);
  }

  @Test(expected = ExistingParametersUrlException.class)
  public void testSaveExistingParams() {
    ParametersMonitoringUrl params = DataUtils.initValidParametersWithDateMonitoring(URL_TEST,
        new TimeData(12, 0, 0),
        new TimeData(23, 59, 59));
    parametersMonitoringUrlService.saveParametersUrl(params);
  }

  @Test(expected = CompareTimesException.class)
  public void testSaveParamsWithBadTimesValue() {
    ParametersMonitoringUrl params = DataUtils.initValidParametersWithDateMonitoring(NEW_URL_TEST,
        new TimeData(12, 0, 0),
        new TimeData(11, 59, 59));
    parametersMonitoringUrlService.saveParametersUrl(params);
  }

  @Test(expected = EqualTimesException.class)
  public void testSaveParamsWithEqualBeginAndEndTimes() {
    ParametersMonitoringUrl params = DataUtils.initValidParametersWithDateMonitoring(NEW_URL_TEST,
        new TimeData(12,0,0),
        new TimeData(12,0,0));
    parametersMonitoringUrlService.saveParametersUrl(params);
  }

  @Test(expected = InvalidTimeResponseException.class)
  public void testSaveParamsWithEqualTimeResponseForDifferentStatus() {
    ParametersMonitoringUrl params = DataUtils.initValidParametersWithDateMonitoring(NEW_URL_TEST,
        new TimeData(12,0,0),
        new TimeData(23,0,0));
    params.setTimeResponseOk(2);
    params.setTimeResponseWarning(2);
    params.setTimeResponseCritical(3);
    parametersMonitoringUrlService.saveParametersUrl(params);
  }

  @Test(expected = InvalidTimeResponseException.class)
  public void testSaveParamsWithTheBiggerValueForStatusOk() {
    ParametersMonitoringUrl params = DataUtils.initValidParametersWithDateMonitoring(NEW_URL_TEST,
        new TimeData(12,0,0),
        new TimeData(23,0,0));
    params.setTimeResponseOk(3);
    params.setTimeResponseWarning(2);
    params.setTimeResponseCritical(4);
    parametersMonitoringUrlService.saveParametersUrl(params);
  }

  @Test(expected = InvalidTimeResponseException.class)
  public void testSaveParamsWithTheLeastValueForStatusCritical() {
    ParametersMonitoringUrl params = DataUtils.initValidParametersWithDateMonitoring(NEW_URL_TEST,
        new TimeData(12,0,0),
        new TimeData(23,0,0));
    params.setTimeResponseOk(2);
    params.setTimeResponseWarning(3);
    params.setTimeResponseCritical(1);
    parametersMonitoringUrlService.saveParametersUrl(params);
  }

  @Test(expected = InvalidExpectedCodeResponseException.class)
  public void testSaveParamsWithInvalidExpectedCodeResponse() {
    ParametersMonitoringUrl params = DataUtils.initValidParametersWithDateMonitoring(NEW_URL_TEST,
        new TimeData(12,0,0),
        new TimeData(23,0,0));
    params.setExpectedCodeResponse(499);
    parametersMonitoringUrlService.saveParametersUrl(params);
  }

  @Test(expected = InvalidSizeResponseException.class)
  public void testSaveParamsWithEqualMinAndMaxSizeResponse() {
    ParametersMonitoringUrl params = DataUtils.initValidParametersWithDateMonitoring(NEW_URL_TEST,
        new TimeData(12,0,0),
        new TimeData(23,0,0));
    params.setMinSizeResponse(100);
    params.setMaxSizeResponse(100);
    parametersMonitoringUrlService.saveParametersUrl(params);
  }

  @Test(expected = InvalidSizeResponseException.class)
  public void testSaveParamsWithBiggerMinSizeResponse() {
    ParametersMonitoringUrl params = DataUtils.initValidParametersWithDateMonitoring(NEW_URL_TEST,
        new TimeData(12,0,0),
        new TimeData(23,0,0));
    params.setMinSizeResponse(1000);
    params.setMaxSizeResponse(100);
    parametersMonitoringUrlService.saveParametersUrl(params);
  }

  @Test
  public void testSaveParamsValidParams() {
    ParametersMonitoringUrl params = DataUtils.initValidParametersWithDateMonitoring(NEW_URL_TEST,
        new TimeData(12,0,0),
        new TimeData(23,0,0));
    parametersMonitoringUrlService.saveParametersUrl(params);
  }

  @Test(expected = NotFoundParametersUrlException.class)
  public void testUpdateParamsNonExistParams() {
    ParametersMonitoringUrl params = DataUtils.initValidParametersWithDateMonitoring(URL_TEST,
        new TimeData(12,0,0),
        new TimeData(23,0,0));
    parametersMonitoringUrlService.updateParametersUrl(100, params);
  }

  @Test(expected = EqualParametersException.class)
  public void testUpdateParamsEqualParams() {
    ParametersMonitoringUrl params = DataUtils.initValidParametersWithDateMonitoring(URL_TEST,
        new TimeData(12,0,0),
        new TimeData(23,59,59));
    parametersMonitoringUrlService.updateParametersUrl(101, params);
  }

  @Test(expected = CompareTimesException.class)
  public void testUpdateParamsWithBadTimeValues() {
    ParametersMonitoringUrl newParams = DataUtils.initValidParametersWithDateMonitoring(URL_TEST,
        new TimeData(23,0,0),
        new TimeData(12,0,0));
    newParams.setMinSizeResponse(100);
    parametersMonitoringUrlService.updateParametersUrl(101, newParams);
  }

  @Test(expected = EqualTimesException.class)
  public void testUpdateParamsEqualBeginAndEndTimes() {
    ParametersMonitoringUrl newParams = DataUtils.initValidParametersWithDateMonitoring(URL_TEST,
        new TimeData(12,0,0),
        new TimeData(12,0,0));
    newParams.setMinSizeResponse(100);
    parametersMonitoringUrlService.updateParametersUrl(101, newParams);
  }

  @Test
  public void testUpdateParamsValidParams() {
    ParametersMonitoringUrl newParams = DataUtils.initValidParametersWithDateMonitoring(URL_TEST,
        new TimeData(12,0,0),
        new TimeData(22,59,59));
    newParams.setMinSizeResponse(10);
    parametersMonitoringUrlService.updateParametersUrl(101, newParams);
  }

  @Test(expected = NotFoundParametersUrlException.class)
  public void testDeleteNotExistParams() {
    parametersMonitoringUrlService.deleteParametersMonitoringUrl(100);
  }

  @Test
  public void testDeleteExistParams() {
    parametersMonitoringUrlService.deleteParametersMonitoringUrl(101);
  }
}
