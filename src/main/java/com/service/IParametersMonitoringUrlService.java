package com.service;

import com.common.ParametersMonitoringUrl;
import com.common.exceptions.*;

public interface IParametersMonitoringUrlService {
  ParametersMonitoringUrl getParametersByUrl(String url) throws NotFoundParametersUrlException;

  ParametersMonitoringUrl getParametersById(long id) throws NotFoundParametersUrlException;

  ParametersMonitoringUrl saveParametersUrl(ParametersMonitoringUrl parametersUrl) throws NullParameterException,
      ExistingParametersUrlException, CompareTimesException, EqualTimesException;

  void updateParametersUrl(long idUpdatedUrl, ParametersMonitoringUrl newParameters) throws
      NotFoundParametersUrlException, NullParameterException, EqualParametersException, CompareTimesException,
      EqualTimesException;

  void deleteParametersMonitoringUrl(long id) throws NotFoundParametersUrlException;

  // For testing
  void deleteAll();
}
