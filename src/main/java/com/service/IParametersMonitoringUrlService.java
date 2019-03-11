/*
 * Copyright (C) 2019.
 * This file is part of project MonitoringUrls
 * Written by Sergiy Krasnikov <sergei29041996@gmail.com>
 */

package com.service;

import com.common.entities.ParametersMonitoringUrl;
import com.common.exceptions.ApiValidationException;
import com.common.exceptions.NotFoundParametersUrlException;

/**
 * Interface for working with url and its parameters for defining status.
 */
public interface IParametersMonitoringUrlService {
  /**
   * Get parameters for defining status by url.
   * @param url need url
   * @return found parameters of url
   * @throws NotFoundParametersUrlException parameters for url has not found
   */
  ParametersMonitoringUrl getParametersByUrl(String url) throws NotFoundParametersUrlException;

  /**
   * Get parameters for defining status by id.
   * @param id id of need url and its parameters
   * @return found url and its parameters
   * @throws NotFoundParametersUrlException parameters for url has not found
   */
  ParametersMonitoringUrl getParametersById(long id) throws NotFoundParametersUrlException;

  /**
   * Save url and its parameters for defining status.
   * @param parametersUrl url and its parameters
   * @return saved url and its parameters
   * @throws ApiValidationException if url has already existed or caused validation problems
   */
  ParametersMonitoringUrl saveParametersUrl(ParametersMonitoringUrl parametersUrl) throws
      ApiValidationException;

  /**
   * Update url and its parameters for defining status.
   * @param idUpdatedUrl id of url and its parameters
   * @param newParameters updated url and its parameters
   * @throws ApiValidationException if url has not been new values or caused validation problems
   */
  void updateParametersUrl(long idUpdatedUrl, ParametersMonitoringUrl newParameters) throws
      ApiValidationException;

  /**
   * Delete url and its parameters for defining status.
   * @param id id of url and its parameters
   * @throws NotFoundParametersUrlException parameters for url has not found
   */
  void deleteParametersMonitoringUrl(long id) throws NotFoundParametersUrlException;

  /**
   * Clear all records from database.
   */
  void deleteAll();
}
