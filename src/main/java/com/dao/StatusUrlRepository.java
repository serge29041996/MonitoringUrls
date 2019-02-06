package com.dao;

import com.common.entities.ParametersMonitoringUrl;
import com.common.entities.StatusUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatusUrlRepository extends JpaRepository<StatusUrl, Long> {
  StatusUrl findTop1ByParametersMonitoringUrlOrderByDefiningStatusTimeDesc(
      ParametersMonitoringUrl parametersMonitoringUrl);

  List<StatusUrl> findAllByParametersMonitoringUrl(
      ParametersMonitoringUrl parametersMonitoringUrl);
}