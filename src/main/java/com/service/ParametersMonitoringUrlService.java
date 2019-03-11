/*
 * Copyright (C) 2019.
 * This file is part of project MonitoringUrls
 * Written by Sergiy Krasnikov <sergei29041996@gmail.com>
 */

package com.service;

import com.common.entities.ParametersMonitoringUrl;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Class for managing with url and its parameters for defining url.
 */
@Service
public class ParametersMonitoringUrlService implements IParametersMonitoringUrlService {
  /** Set of valid http statuses. */
  private static Set<Integer> validHttpStatuses = new HashSet<>();
  /** Min number of http status 1xx. */
  private static final int MIN_NUMBER_FOR_HTTP_STATUSES_1XX = 100;
  /** Max number of http status 1xx. */
  private static final int MAX_NUMBER_FOR_HTTP_STATUSES_1XX = 104;
  /** Min number of http status 2xx. */
  private static final int MIN_NUMBER_FOR_HTTP_STATUSES_2XX = 200;
  /** Max number of http status 2xx. */
  private static final int MAX_NUMBER_FOR_HTTP_STATUSES_20X = 209;
  /** Number of http status 226. */
  private static final int HTTP_STATUS_226 = 226;
  /** Min number of http status 3xx. */
  private static final int MIN_NUMBER_FOR_HTTP_STATUSES_3XX = 300;
  /** Max number of http status 3xx. */
  private static final int MAX_NUMBER_FOR_HTTP_STATUSES_3XX = 309;
  /** Min number of http status 40x. */
  private static final int MIN_NUMBER_FOR_HTTP_STATUSES_40X = 400;
  /** Max number of http status 41x. */
  private static final int MAX_NUMBER_FOR_HTTP_STATUSES_41X = 419;
  /** Min number of http status 42x. */
  private static final int MIN_NUMBER_FOR_HTTP_STATUSES_42X = 421;
  /** Max number of http status 42x. */
  private static final int MAX_NUMBER_FOR_HTTP_STATUSES_42X = 425;
  /** Number of http status 426. */
  private static final int HTTP_STATUS_426 = 426;
  /** Number of http status 428. */
  private static final int HTTP_STATUS_428 = 428;
  /** Number of http status 429. */
  private static final int HTTP_STATUS_429 = 429;
  /** Number of http status 431. */
  private static final int HTTP_STATUS_431 = 431;
  /** Number of http status 451. */
  private static final int HTTP_STATUS_451 = 451;
  /** Min number of http status 5xx. */
  private static final int MIN_NUMBER_FOR_HTTP_STATUSES_5XX = 500;
  /** Max number of http status 5xx. */
  private static final int MAX_NUMBER_FOR_HTTP_STATUSES_5XX = 511;

  static {
    for (int i = MIN_NUMBER_FOR_HTTP_STATUSES_1XX; i < MAX_NUMBER_FOR_HTTP_STATUSES_1XX; i++) {
      validHttpStatuses.add(i);
    }

    for (int i = MIN_NUMBER_FOR_HTTP_STATUSES_2XX; i < MAX_NUMBER_FOR_HTTP_STATUSES_20X; i++) {
      validHttpStatuses.add(i);
    }

    validHttpStatuses.add(HTTP_STATUS_226);

    for (int i = MIN_NUMBER_FOR_HTTP_STATUSES_3XX; i < MAX_NUMBER_FOR_HTTP_STATUSES_3XX; i++) {
      validHttpStatuses.add(i);
    }

    for (int i = MIN_NUMBER_FOR_HTTP_STATUSES_40X; i < MAX_NUMBER_FOR_HTTP_STATUSES_41X; i++) {
      validHttpStatuses.add(i);
    }

    for (int i = MIN_NUMBER_FOR_HTTP_STATUSES_42X; i < MAX_NUMBER_FOR_HTTP_STATUSES_42X; i++) {
      validHttpStatuses.add(i);
    }

    validHttpStatuses.add(HTTP_STATUS_426);
    validHttpStatuses.add(HTTP_STATUS_428);
    validHttpStatuses.add(HTTP_STATUS_429);
    validHttpStatuses.add(HTTP_STATUS_431);
    validHttpStatuses.add(HTTP_STATUS_451);

    for (int i = MIN_NUMBER_FOR_HTTP_STATUSES_5XX; i < MAX_NUMBER_FOR_HTTP_STATUSES_5XX; i++) {
      validHttpStatuses.add(i);
    }
  }

  /** Repository for managing with urls and its parameters in database. */
  private ParametersMonitoringUrlRepository parametersUrlRepository;

  /**
   * Constructor with repository for work with url and its parameters.
   * @param parametersUrlRepository repository for work with url and its parameters.
   */
  @Autowired
  public ParametersMonitoringUrlService(ParametersMonitoringUrlRepository parametersUrlRepository) {
    this.parametersUrlRepository = parametersUrlRepository;
  }

  /**
   * Get parameters for defining status by url.
   * @param url need url
   * @return found parameters of url
   * @throws NotFoundParametersUrlException parameters for url has not found
   */
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

  /**
   * Get parameters for defining status by id.
   * @param id id of need url and its parameters
   * @return found url and its parameters
   * @throws NotFoundParametersUrlException parameters for url has not found
   */
  @Override
  public ParametersMonitoringUrl getParametersById(long id) throws NotFoundParametersUrlException {
    ParametersMonitoringUrl findByIdParams = parametersUrlRepository.findById(id);
    if (findByIdParams != null) {
      return findByIdParams;
    } else {
      throw new NotFoundParametersUrlException("Parameters with id " + id + " was not found");
    }
  }

  /**
   * Save url and its parameters for defining status.
   * @param parameters url and its parameters
   * @return saved url and its parameters
   * @throws ApiValidationException if url has already existed or caused validation problems
   */
  @Override
  public ParametersMonitoringUrl saveParametersUrl(ParametersMonitoringUrl parameters) throws
      ApiValidationException {
    isExistSameParametersForUrl(parameters);
    compareTimes(parameters);
    compareTimesResponseForDifferentStatus(parameters);
    checkExpectedCodeResponse(parameters.getExpectedCodeResponse());
    compareSizeResponse(parameters);
    return parametersUrlRepository.save(parameters);
  }

  /**
   * Update url and its parameters for defining status.
   * @param idUpdatedUrl id of url and its parameters
   * @param newParameters updated url and its parameters
   * @throws ApiValidationException if url has not been new values or caused validation problems
   */
  @Override
  public void updateParametersUrl(long idUpdatedUrl, ParametersMonitoringUrl newParameters) throws
      ApiValidationException {
    ParametersMonitoringUrl oldParameters = getParametersById(idUpdatedUrl);
    isEqualParams(oldParameters, newParameters);
    compareTimes(newParameters);
    compareTimesResponseForDifferentStatus(newParameters);
    checkExpectedCodeResponse(newParameters.getExpectedCodeResponse());
    compareSizeResponse(newParameters);
    newParameters.setId(idUpdatedUrl);
    parametersUrlRepository.save(newParameters);
  }

  /**
   * Delete url and its parameters for defining status.
   * @param id id of url and its parameters
   * @throws NotFoundParametersUrlException parameters for url has not found
   */
  @Override
  public void deleteParametersMonitoringUrl(long id) throws NotFoundParametersUrlException {
    ParametersMonitoringUrl paramsForDelete = getParametersById(id);
    parametersUrlRepository.delete(paramsForDelete);
  }

  /**
   * Check existing url and its parameters in database.
   * @param parametersMonitoringUrl current url and its parameters
   * @throws ExistingParametersUrlException if url and parameters have already existed in database
   */
  private void isExistSameParametersForUrl(ParametersMonitoringUrl parametersMonitoringUrl)
      throws ExistingParametersUrlException {
    ParametersMonitoringUrl foundByUrlParameters = parametersUrlRepository
        .findByUrl(parametersMonitoringUrl.getUrl());
    if (foundByUrlParameters != null) {
      throw new ExistingParametersUrlException(
          "Parameters for url " + parametersMonitoringUrl.getUrl() + " already exist");
    }
  }

  /**
   * Check equality of urls and their parameters.
   * @param params1 first url and its parameters
   * @param params2 second url and its parameters
   * @throws EqualParametersException if both parameters are equal
   */
  private static void isEqualParams(ParametersMonitoringUrl params1,
                                    ParametersMonitoringUrl params2)
      throws EqualParametersException {
    if (params1.equals(params2)) {
      throw new EqualParametersException("No changes in value of parameters");
    }
  }

  /**
   * Check begin and end times.
   * @param parametersUrl url and its parameters
   * @throws CompareTimesException if begin time is greater than end time and vice versa
   * @throws EqualTimesException if begin and end times are equal
   */
  private static void compareTimes(ParametersMonitoringUrl parametersUrl)
      throws CompareTimesException, EqualTimesException {
    Date beginTime = parametersUrl.getBeginTimeMonitoring();
    Date endTime = parametersUrl.getEndTimeMonitoring();

    if (endTime.getTime() < beginTime.getTime()) {
      throw new CompareTimesException("Begin time is bigger than end time");
    }

    if (endTime.getTime() == beginTime.getTime()) {
      throw new EqualTimesException("Begin and end time are equal");
    }
  }

  /**
   * Compare time response for different statuses.
   *
   * @param parametersUrl url and its parameters
   * @throws InvalidTimeResponseException if time response for status ok is greater than other
   *                                      statuses or time response for status critical is smaller
   *                                      than other statuses or have equal
   *                                      time response fordifferent statuses
   */
  private static void compareTimesResponseForDifferentStatus(ParametersMonitoringUrl parametersUrl)
      throws InvalidTimeResponseException {
    long timeResponseOk = parametersUrl.getTimeResponseOk();
    long timeResponseWarning = parametersUrl.getTimeResponseWarning();
    long timeResponseCritical = parametersUrl.getTimeResponseCritical();
    StringBuilder exceptionMessages = new StringBuilder();

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

    if (!"".equals(exceptionMessages.toString())) {
      throw new InvalidTimeResponseException(exceptionMessages.toString());
    }
  }

  /**
   * Check expected code of response.
   * @param codeResponse current http code of response
   * @throws InvalidExpectedCodeResponseException if http code of response is not valid
   */
  private static void checkExpectedCodeResponse(int codeResponse)
      throws InvalidExpectedCodeResponseException {
    if (!validHttpStatuses.contains(codeResponse)) {
      throw new InvalidExpectedCodeResponseException("Invalid expected code of response");
    }
  }

  /**
   * Check min and max size of response.
   *
   * @param parameters url and its parameters
   * @throws InvalidSizeResponseException if min and max size response are equal or min size is
   *                                      greater than max size or max size is smaller than max
   *                                      size
   */
  private static void compareSizeResponse(ParametersMonitoringUrl parameters)
      throws InvalidSizeResponseException {
    if (parameters.getMinSizeResponse() == parameters.getMaxSizeResponse()) {
      throw new InvalidSizeResponseException("Min and max size of response is equal");
    }
    if (parameters.getMinSizeResponse() > parameters.getMaxSizeResponse()) {
      throw new InvalidSizeResponseException("Max size of response is bigger than min size");
    }
  }

  /**
   * Clear all records from database.
   */
  @Override
  public void deleteAll() {
    parametersUrlRepository.deleteAll();
  }
}
