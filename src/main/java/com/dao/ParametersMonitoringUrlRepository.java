/*
 * Copyright (C) 2019.
 * This file is part of project MonitoringUrls
 * Written by Sergiy Krasnikov <sergei29041996@gmail.com>
 */

package com.dao;

import com.common.entities.ParametersMonitoringUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for working with url and its parameters for defining status.
 */
@Repository
public interface ParametersMonitoringUrlRepository
    extends JpaRepository<ParametersMonitoringUrl, Long> {
  /**
   * Find url and its parameters by url.
   * @param url url for monitoring
   * @return found object with url and its parameters
   */
  ParametersMonitoringUrl findByUrl(String url);

  /**
   * Find url and its parameters by url.
   * @param id id of url
   * @return found object with url and its parameters
   */
  ParametersMonitoringUrl findById(long id);
}
