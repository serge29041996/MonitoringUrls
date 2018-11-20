package com.dao;

import com.common.ParametersMonitoringUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParametersMonitoringUrlRepository
    extends JpaRepository<ParametersMonitoringUrl, Long> {
  ParametersMonitoringUrl findByUrl(String url);

  ParametersMonitoringUrl findById(long id);
}
