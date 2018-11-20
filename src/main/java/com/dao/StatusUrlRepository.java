package com.dao;

import com.common.ParametersMonitoringUrl;
import com.common.StatusUrl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StatusUrlRepository extends JpaRepository<StatusUrl, Long> {
  StatusUrl findTop1ByParametersMonitoringUrlOrderByDefiningStatusTimeDesc(
                                                                           ParametersMonitoringUrl
                                                                               parametersMonitoringUrl);
  List<StatusUrl> findAllByParametersMonitoringUrl(ParametersMonitoringUrl parametersMonitoringUrl);
}