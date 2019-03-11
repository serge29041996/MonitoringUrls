package com.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNull;

import com.common.DataUtils;
import com.common.entities.ParametersMonitoringUrl;
import com.common.TimeData;
import com.common.exceptions.ApiValidationException;
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
public class ParametersMonitoringUrlServiceTest {
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
    Mockito.when(parametersMonitoringUrlRepository.save(newParams)).thenReturn(newParams);
  }

  @Test
  public void testGetParametersByUrlExistingEntity() throws ApiValidationException {
    ParametersMonitoringUrl params = DataUtils.initValidParametersWithDateMonitoring(URL_TEST,
        new TimeData(12, 0, 0),
        new TimeData(23, 59, 59));
    ParametersMonitoringUrl findByUrlParams =
        parametersMonitoringUrlService.getParametersByUrl(URL_TEST);
    assertThat(findByUrlParams).isEqualTo(params);
  }

  @Test(expected = NotFoundParametersUrlException.class)
  public void testGetParametersByUrlNotExistingEntity() throws ApiValidationException {
    parametersMonitoringUrlService.getParametersByUrl("testUrl");
  }

  @Test
  public void testGetParametersByIdExistingEntity() throws ApiValidationException {
    ParametersMonitoringUrl params = DataUtils.initValidParametersWithDateMonitoring(URL_TEST,
        new TimeData(12, 0, 0),
        new TimeData(23, 59, 59));
    ParametersMonitoringUrl findByIdParams =
        parametersMonitoringUrlService.getParametersById(101);
    assertThat(findByIdParams).isEqualTo(params);
  }

  @Test(expected = NotFoundParametersUrlException.class)
  public void testGetParametersByIdNotExistEntity() throws ApiValidationException {
    parametersMonitoringUrlService.getParametersById(100);
  }

  @Test(expected = ExistingParametersUrlException.class)
  public void testSaveExistingParams() throws ApiValidationException {
    ParametersMonitoringUrl params = DataUtils.initValidParametersWithDateMonitoring(URL_TEST,
        new TimeData(12, 0, 0),
        new TimeData(23, 59, 59));
    parametersMonitoringUrlService.saveParametersUrl(params);
  }

  @Test(expected = CompareTimesException.class)
  public void testSaveParamsWithBadTimesValue() throws ApiValidationException {
    ParametersMonitoringUrl params = DataUtils.initValidParametersWithDateMonitoring(NEW_URL_TEST,
        new TimeData(12, 0, 0),
        new TimeData(11, 59, 59));
    parametersMonitoringUrlService.saveParametersUrl(params);
  }

  @Test(expected = EqualTimesException.class)
  public void testSaveParamsWithEqualBeginAndEndTimes() throws ApiValidationException {
    ParametersMonitoringUrl params = DataUtils.initValidParametersWithDateMonitoring(NEW_URL_TEST,
        new TimeData(12,0,0),
        new TimeData(12,0,0));
    parametersMonitoringUrlService.saveParametersUrl(params);
  }

  @Test(expected = InvalidTimeResponseException.class)
  public void testSaveParamsWithEqualTimeResponseForDifferentStatus()
      throws ApiValidationException {
    ParametersMonitoringUrl params = DataUtils.initValidParametersWithDateMonitoring(NEW_URL_TEST,
        new TimeData(12,0,0),
        new TimeData(23,0,0));
    params.setTimeResponseOk(2);
    params.setTimeResponseWarning(2);
    params.setTimeResponseCritical(3);
    parametersMonitoringUrlService.saveParametersUrl(params);
  }

  @Test(expected = InvalidTimeResponseException.class)
  public void testSaveParamsWithTheBiggerValueForStatusOk() throws ApiValidationException {
    ParametersMonitoringUrl params = DataUtils.initValidParametersWithDateMonitoring(NEW_URL_TEST,
        new TimeData(12,0,0),
        new TimeData(23,0,0));
    params.setTimeResponseOk(3);
    params.setTimeResponseWarning(2);
    params.setTimeResponseCritical(4);
    parametersMonitoringUrlService.saveParametersUrl(params);
  }

  @Test(expected = InvalidTimeResponseException.class)
  public void testSaveParamsWithTheLeastValueForStatusCritical() throws ApiValidationException {
    ParametersMonitoringUrl params = DataUtils.initValidParametersWithDateMonitoring(NEW_URL_TEST,
        new TimeData(12,0,0),
        new TimeData(23,0,0));
    params.setTimeResponseOk(2);
    params.setTimeResponseWarning(3);
    params.setTimeResponseCritical(1);
    parametersMonitoringUrlService.saveParametersUrl(params);
  }

  @Test(expected = InvalidExpectedCodeResponseException.class)
  public void testSaveParamsWithInvalidExpectedCodeResponse() throws ApiValidationException {
    ParametersMonitoringUrl params = DataUtils.initValidParametersWithDateMonitoring(NEW_URL_TEST,
        new TimeData(12,0,0),
        new TimeData(23,0,0));
    params.setExpectedCodeResponse(499);
    parametersMonitoringUrlService.saveParametersUrl(params);
  }

  @Test(expected = InvalidSizeResponseException.class)
  public void testSaveParamsWithEqualMinAndMaxSizeResponse() throws ApiValidationException {
    ParametersMonitoringUrl params = DataUtils.initValidParametersWithDateMonitoring(NEW_URL_TEST,
        new TimeData(12,0,0),
        new TimeData(23,0,0));
    params.setMinSizeResponse(100);
    params.setMaxSizeResponse(100);
    parametersMonitoringUrlService.saveParametersUrl(params);
  }

  @Test(expected = InvalidSizeResponseException.class)
  public void testSaveParamsWithBiggerMinSizeResponse() throws ApiValidationException {
    ParametersMonitoringUrl params = DataUtils.initValidParametersWithDateMonitoring(NEW_URL_TEST,
        new TimeData(12,0,0),
        new TimeData(23,0,0));
    params.setMinSizeResponse(1000);
    params.setMaxSizeResponse(100);
    parametersMonitoringUrlService.saveParametersUrl(params);
  }

  @Test
  public void testSaveParamsValidParams() throws ApiValidationException {
    ParametersMonitoringUrl params = DataUtils.initValidParametersWithDateMonitoring(NEW_URL_TEST,
        new TimeData(12,0,0),
        new TimeData(23,59,59));
    ParametersMonitoringUrl params2 = DataUtils.initValidParametersWithDateMonitoring(NEW_URL_TEST,
        new TimeData(12,0,0),
        new TimeData(23,59,59));
    if (params.equals(params2)) {
      int i = 0;
    }
    ParametersMonitoringUrl savedParametersUrl =
        parametersMonitoringUrlService.saveParametersUrl(params);
    assertThat(savedParametersUrl.getUrl()).isEqualTo(NEW_URL_TEST);
  }

  @Test(expected = NotFoundParametersUrlException.class)
  public void testUpdateParamsNonExistParams() throws ApiValidationException {
    ParametersMonitoringUrl params = DataUtils.initValidParametersWithDateMonitoring(URL_TEST,
        new TimeData(12,0,0),
        new TimeData(23,0,0));
    parametersMonitoringUrlService.updateParametersUrl(100, params);
  }

  @Test(expected = EqualParametersException.class)
  public void testUpdateParamsEqualParams() throws ApiValidationException {
    ParametersMonitoringUrl params = DataUtils.initValidParametersWithDateMonitoring(URL_TEST,
        new TimeData(12,0,0),
        new TimeData(23,59,59));
    parametersMonitoringUrlService.updateParametersUrl(101, params);
  }

  @Test(expected = CompareTimesException.class)
  public void testUpdateParamsWithBadTimeValues() throws ApiValidationException {
    ParametersMonitoringUrl newParams = DataUtils.initValidParametersWithDateMonitoring(URL_TEST,
        new TimeData(23,0,0),
        new TimeData(12,0,0));
    newParams.setMinSizeResponse(100);
    parametersMonitoringUrlService.updateParametersUrl(101, newParams);
  }

  @Test(expected = EqualTimesException.class)
  public void testUpdateParamsEqualBeginAndEndTimes() throws ApiValidationException {
    ParametersMonitoringUrl newParams = DataUtils.initValidParametersWithDateMonitoring(URL_TEST,
        new TimeData(12,0,0),
        new TimeData(12,0,0));
    newParams.setMinSizeResponse(100);
    parametersMonitoringUrlService.updateParametersUrl(101, newParams);
  }

  @Test
  public void testUpdateParamsValidParams() throws ApiValidationException {
    ParametersMonitoringUrl newParams = DataUtils.initValidParametersWithDateMonitoring(URL_TEST,
        new TimeData(12,0,0),
        new TimeData(22,59,59));
    newParams.setMinSizeResponse(10);
    parametersMonitoringUrlService.updateParametersUrl(101, newParams);
    assertThat(newParams.getMinSizeResponse()).isEqualTo(10);
  }

  @Test(expected = NotFoundParametersUrlException.class)
  public void testDeleteNotExistParams() throws ApiValidationException {
    parametersMonitoringUrlService.deleteParametersMonitoringUrl(100);
  }
}
