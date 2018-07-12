package com.service;

import com.common.ParametersMonitoringUrl;

public interface IParametersMonitoringUrlService {
  ParametersMonitoringUrl getParametersByUrl(String url);

  ParametersMonitoringUrl getParametersById(long id);

  ParametersMonitoringUrl saveParametersUrl(ParametersMonitoringUrl parameters);

  void updateParametersUrl(long idUpdatedUrl, ParametersMonitoringUrl newParameters);

  void deleteParametersMonitoringUrl(long id);

  // For testing
  void deleteAll();
}
