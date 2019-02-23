package com.service;

import com.common.entities.ParametersMonitoringUrl;
import com.common.exceptions.CompareTimesException;
import com.common.exceptions.EqualParametersException;
import com.common.exceptions.EqualTimesException;
import com.common.exceptions.ExistingParametersUrlException;
import com.common.exceptions.InvalidExpectedCodeResponseException;
import com.common.exceptions.InvalidSizeResponseException;
import com.common.exceptions.InvalidTimeResponseException;
import com.common.exceptions.NotFoundParametersUrlException;

public interface IParametersMonitoringUrlService {
  ParametersMonitoringUrl getParametersByUrl(String url) throws NotFoundParametersUrlException;

  ParametersMonitoringUrl getParametersById(long id) throws NotFoundParametersUrlException;

  ParametersMonitoringUrl saveParametersUrl(ParametersMonitoringUrl parametersUrl) throws
      ExistingParametersUrlException, CompareTimesException, EqualTimesException,
      InvalidTimeResponseException, InvalidExpectedCodeResponseException,
      InvalidSizeResponseException;

  void updateParametersUrl(long idUpdatedUrl, ParametersMonitoringUrl newParameters) throws
      NotFoundParametersUrlException, EqualParametersException, CompareTimesException,
      EqualTimesException, InvalidTimeResponseException, InvalidExpectedCodeResponseException,
      InvalidSizeResponseException;

  void deleteParametersMonitoringUrl(long id) throws NotFoundParametersUrlException;

  // For testing
  void deleteAll();
}
