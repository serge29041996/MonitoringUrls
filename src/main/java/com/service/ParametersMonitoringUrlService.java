package com.service;

import com.common.ParametersMonitoringUrl;
import com.common.exceptions.*;
import com.dao.ParametersMonitoringUrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
public class ParametersMonitoringUrlService implements IParametersMonitoringUrlService {
  private Set<Integer> validHttpStatuses;

  {
    validHttpStatuses = new HashSet<Integer>();
    for (int i = 100; i < 104; i++) {
      validHttpStatuses.add(i);
    }

    for (int i = 200; i < 209; i++) {
      validHttpStatuses.add(i);
    }

    validHttpStatuses.add(226);

    for (int i = 300; i < 309; i++) {
      validHttpStatuses.add(i);
    }

    for (int i = 400; i < 419; i++) {
      validHttpStatuses.add(i);
    }

    for (int i = 421; i < 425; i++) {
      validHttpStatuses.add(i);
    }

    validHttpStatuses.add(426);
    validHttpStatuses.add(428);
    validHttpStatuses.add(429);
    validHttpStatuses.add(431);
    validHttpStatuses.add(451);

    for (int i = 500; i < 512; i++) {
      validHttpStatuses.add(i);
    }
  }

  @Autowired
  private ParametersMonitoringUrlRepository parametersUrlRepository;

  @Override
  public ParametersMonitoringUrl getParametersByUrl(String url)
      throws NotFoundParametersUrlException {
    ParametersMonitoringUrl findByUrlParams = parametersUrlRepository.findByUrl(url);
    if (findByUrlParams != null) {
      return findByUrlParams;
    } else {
      throw new NotFoundParametersUrlException("Parameters for url " + url + " was not found");
    }
  }

  @Override
  public ParametersMonitoringUrl getParametersById(long id) throws NotFoundParametersUrlException {
    ParametersMonitoringUrl findByIdParams = parametersUrlRepository.findById(id);
    if (findByIdParams != null) {
      return findByIdParams;
    } else {
      throw new NotFoundParametersUrlException("Parameters was not found");
    }
  }

  @Override
  public ParametersMonitoringUrl saveParametersUrl(ParametersMonitoringUrl parameters) throws
      ExistingParametersUrlException, CompareTimesException, EqualTimesException,
      InvalidTimeResponseException, InvalidExpectedCodeResponseException,
      InvalidSizeResponseException {
    isExistSameParametersForUrl(parameters);
    compareTimes(parameters);
    compareTimesResponseForDifferentStatus(parameters);
    checkExpectedCodeResponse(parameters.getExpectedCodeResponse());
    compareSizeResponse(parameters);
    return parametersUrlRepository.save(parameters);
  }

  @Override
  public void updateParametersUrl(long idUpdatedUrl, ParametersMonitoringUrl newParameters) throws
      NotFoundParametersUrlException, EqualParametersException, CompareTimesException,
      EqualTimesException, InvalidTimeResponseException, InvalidExpectedCodeResponseException,
      InvalidSizeResponseException {
    ParametersMonitoringUrl oldParameters = getParametersById(idUpdatedUrl);
    isEqualParams(oldParameters, newParameters);
    compareTimes(newParameters);
    compareTimesResponseForDifferentStatus(newParameters);
    checkExpectedCodeResponse(newParameters.getExpectedCodeResponse());
    compareSizeResponse(newParameters);
    newParameters.setId(idUpdatedUrl);
    parametersUrlRepository.save(newParameters);
  }

  @Override
  public void deleteParametersMonitoringUrl(long id) throws NotFoundParametersUrlException {
    ParametersMonitoringUrl paramsForDelete = getParametersById(id);
    parametersUrlRepository.delete(paramsForDelete);
  }

  private void isExistSameParametersForUrl(ParametersMonitoringUrl parametersMonitoringUrl)
      throws ExistingParametersUrlException {
    ParametersMonitoringUrl foundByUrlParameters = parametersUrlRepository
        .findByUrl(parametersMonitoringUrl.getUrl());
    if (foundByUrlParameters != null) {
      throw new ExistingParametersUrlException(
          "Parameters for url " + parametersMonitoringUrl.getUrl() + " already exist");
    }
  }

  private void isEqualParams(ParametersMonitoringUrl params1, ParametersMonitoringUrl params2)
      throws EqualParametersException {
    if (params1.equals(params2)) {
      throw new EqualParametersException("No changes in value of parameters");
    }
  }

  private void compareTimes(ParametersMonitoringUrl parametersUrl) throws CompareTimesException,
      EqualTimesException {
    Date beginTime = parametersUrl.getBeginTimeMonitoring();
    Date endTime = parametersUrl.getEndTimeMonitoring();

    if (endTime.getTime() < beginTime.getTime()) {
      throw new CompareTimesException("Begin time is bigger than end time");
    } else if (endTime.getTime() == beginTime.getTime()) {
      throw new EqualTimesException("Begin and end time are equal");
    }
  }

  private void compareTimesResponseForDifferentStatus(ParametersMonitoringUrl parametersUrl)
      throws InvalidTimeResponseException {
    long timeResponseOk = parametersUrl.getTimeResponseOk();
    long timeResponseWarning = parametersUrl.getTimeResponseWarning();
    long timeResponseCritical = parametersUrl.getTimeResponseCritical();
    StringBuilder exceptionMessages = new StringBuilder("");

    if (timeResponseOk == timeResponseWarning || timeResponseOk == timeResponseCritical) {
      exceptionMessages.append("Time response should not be equal for status ok and other status");
    }

    long min = Math.min(Math.min(timeResponseOk, timeResponseWarning), timeResponseCritical);
    if (min != timeResponseOk) {
      exceptionMessages.append("Time response for status OK should be the least.");
    }

    long max = Math.max(Math.max(timeResponseOk, timeResponseWarning), timeResponseCritical);
    if (max != timeResponseCritical) {
      exceptionMessages.append("Time response for status Critical should be the largest");
    }

    if (!exceptionMessages.toString().equals("")) {
      throw new InvalidTimeResponseException(exceptionMessages.toString());
    }
  }

  private void checkExpectedCodeResponse(int expectedCodeResponse)
      throws InvalidExpectedCodeResponseException {
    if (!validHttpStatuses.contains(expectedCodeResponse)) {
      throw new InvalidExpectedCodeResponseException("Invalid expected code of response");
    }
  }

  private void compareSizeResponse(ParametersMonitoringUrl parameters)
      throws InvalidSizeResponseException {
    if (parameters.getMinSizeResponse() == parameters.getMaxSizeResponse()) {
      throw new InvalidSizeResponseException("Min and max size of response is equal");
    } else if (parameters.getMinSizeResponse() > parameters.getMaxSizeResponse()) {
      throw new InvalidSizeResponseException("Max size of response is bigger than min size");
    }
  }

  //Using for testing
  @Override
  public void deleteAll() {
    parametersUrlRepository.deleteAll();
  }
}
