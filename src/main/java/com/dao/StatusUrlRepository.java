/*
 * Copyright (C) 2019.
 * This file is part of project MonitoringUrls
 * Written by Sergiy Krasnikov <sergei29041996@gmail.com>
 */

package com.dao;

import com.common.entities.ParametersMonitoringUrl;
import com.common.entities.StatusUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for working with defined status of url.
 */
@Repository
public interface StatusUrlRepository extends JpaRepository<StatusUrl, Long> {
  /**
   * Find last defining status by time.
   * @param parametersMonitoringUrl url and its parameters
   * @return found last defining status by time
   */
  StatusUrl findTop1ByParametersMonitoringUrlOrderByDefiningStatusTimeDesc(
      ParametersMonitoringUrl parametersMonitoringUrl);

  /**
   * Find all defining statuses for url.
   * @param parametersMonitoringUrl url and its parameters
   * @return found last defining statuses by time
   */
  List<StatusUrl> findAllByParametersMonitoringUrl(
      ParametersMonitoringUrl parametersMonitoringUrl);
}
