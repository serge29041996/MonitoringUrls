package com.service;

import com.common.ParametersMonitoringUrl;
import com.dao.ParametersMonitoringUrlRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class ParametersMonitoringUrlService implements IParametersMonitoringUrlService {
  @Autowired
  private ParametersMonitoringUrlRepository parametersUrl;

  @Override
  public ParametersMonitoringUrl getParametersByUrl(String url) {
    return parametersUrl.findByUrl(url);
  }

  @Override
  public ParametersMonitoringUrl getParametersById(long id) {
    return parametersUrl.findById(id);
  }

  @Override
  public ParametersMonitoringUrl saveParametersUrl(ParametersMonitoringUrl parameters) {
    return null;
  }

  @Override
  public void updateParametersUrl(long idUpdatedUrl, ParametersMonitoringUrl newParameters) {

  }

  @Override
  public void deleteParametersMonitoringUrl(long id) {

  }

  //Using for testing
  @Override
  public void deleteAll() {

  }

  private boolean isExistSameParametersForUrl(long id) {
    ParametersMonitoringUrl parametersMonitoringUrl = getParametersById(id);
      return parametersMonitoringUrl != null;
  }
}
