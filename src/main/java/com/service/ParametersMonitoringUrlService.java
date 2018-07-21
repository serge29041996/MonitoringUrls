package com.service;

import com.common.ParametersMonitoringUrl;
import com.common.exceptions.*;
import com.dao.ParametersMonitoringUrlRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class ParametersMonitoringUrlService implements IParametersMonitoringUrlService {

  private final ParametersMonitoringUrlRepository parametersUrlRepository;

  @Autowired
  public ParametersMonitoringUrlService(ParametersMonitoringUrlRepository parametersMonitoringUrlRepository) {
    this.parametersUrlRepository = parametersMonitoringUrlRepository;
  }

  @Override
  public ParametersMonitoringUrl getParametersByUrl(String url) throws NotFoundParametersUrlException {
    ParametersMonitoringUrl findByUrlParams = parametersUrlRepository.findByUrl(url);
    if(findByUrlParams != null) {
      return findByUrlParams;
    } else {
      throw new NotFoundParametersUrlException("Parameters for url " + url + " was not found");
    }
  }

  @Override
  public ParametersMonitoringUrl getParametersById(long id) throws NotFoundParametersUrlException {
    ParametersMonitoringUrl findByIdParams = parametersUrlRepository.findById(id);
    if(findByIdParams != null) {
      return findByIdParams;
    } else {
      throw new NotFoundParametersUrlException("Parameters was not found");
    }
  }

  @Override
  public ParametersMonitoringUrl saveParametersUrl(ParametersMonitoringUrl parameters) throws
      NullParameterException, ExistingParametersUrlException, CompareTimesException, EqualTimesException {
    isNullParameter(parameters);
    isExistSameParametersForUrl(parameters);
    compareTimes(parameters);
    return parametersUrlRepository.save(parameters);
  }

  @Override
  public void updateParametersUrl(long idUpdatedUrl, ParametersMonitoringUrl newParameters) throws
      NotFoundParametersUrlException, NullParameterException, EqualParametersException, CompareTimesException,
      EqualTimesException {
    ParametersMonitoringUrl oldParameters = getParametersById(idUpdatedUrl);
    isNullParameter(newParameters);
    isEqualParams(oldParameters, newParameters);
    compareTimes(newParameters);
    newParameters.setId(idUpdatedUrl);
    parametersUrlRepository.save(newParameters);
  }

  @Override
  public void deleteParametersMonitoringUrl(long id) throws NotFoundParametersUrlException {
    ParametersMonitoringUrl paramsForDelete = getParametersById(id);
    parametersUrlRepository.delete(paramsForDelete);
  }


  private void isNullParameter(ParametersMonitoringUrl parametersUrl) throws NullParameterException {
    if (parametersUrl == null) {
      throw new NullParameterException("No value for parameters");
    }
  }

  private void isExistSameParametersForUrl(ParametersMonitoringUrl parametersMonitoringUrl)
      throws ExistingParametersUrlException {
    ParametersMonitoringUrl foundByUrlParameters = parametersUrlRepository.findByUrl(parametersMonitoringUrl.getUrl());
    if(foundByUrlParameters != null) {
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

  private void compareTimes(ParametersMonitoringUrl parametersUrl) throws CompareTimesException, EqualTimesException {
    Date beginTime = parametersUrl.getBeginTimeMonitoring();
    Date endTime = parametersUrl.getEndTimeMonitoring();

    if (endTime.getTime() < beginTime.getTime()) {
      throw new CompareTimesException("Begin time is bigger than end time");
    } else if (endTime.getTime() == beginTime.getTime()) {
      throw new EqualTimesException("Begin and end time are equal");
    }
  }

  //Using for testing
  @Override
  public void deleteAll() {
    parametersUrlRepository.deleteAll();
  }
}
