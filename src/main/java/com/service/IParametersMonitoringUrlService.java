package com.service;

import com.common.ParametersMonitoringUrl;
import com.common.exceptions.*;

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
