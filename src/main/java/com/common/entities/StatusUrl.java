/*
 * Copyright (C) 2019.
 * This file is part of project MonitoringUrls
 * Written by Sergiy Krasnikov <sergei29041996@gmail.com>
 */

package com.common.entities;

import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * Entity for saving status and time defining of status for url.
 */
@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = "id", doNotUseGetters = true)
@Table(name = "statusurl")
public class StatusUrl {
  /** Id of status url. */
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @ApiModelProperty(notes = "The database generated status of url ID")
  private long id;

  /** Time of defining status for url. */
  /*
  @Column(name = "definingstatustime")
  @Temporal(TemporalType.TIMESTAMP)
  @ApiModelProperty(notes = "Date and time when status is determined")
  private Date definingStatusTime;
  */
  @Column(name = "definingstatustime", columnDefinition = "TIMESTAMP")
  @ApiModelProperty(notes = "Date and time when status is determined")
  private LocalDateTime definingStatusTime;

  /** Status of url. */
  @NotBlank(message = "Status of url should not be empty")
  @Size(message = "Status cannot have length greater than 8", max = 11)
  @ApiModelProperty(notes = "Status which is determined")
  private String status;

  /** Url and parameters for defining its status. This parameters are:
   * - begin and end times for monitoring status;
   * - max time of response for status OK, Warning and Critical
   * - expected code of http response;
   * - min and max size of http response;
   * - (optional) string, which should be in http response. */
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "urlid", nullable = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @ApiModelProperty(notes = "Url, which status is determined")
  private ParametersMonitoringUrl parametersMonitoringUrl;

  /**
   * Constructor with url and parameters for defining status.
   * Set status as status ok
   * @param parametersMonitoringUrl url and parameters for defining status
   */
  public StatusUrl(ParametersMonitoringUrl parametersMonitoringUrl) {
    this.status = "OK";
    this.parametersMonitoringUrl = parametersMonitoringUrl;
  }

  /**
   * Get time of defining status.
   * @return time of defining status
   */
  /*
  public Date getDefiningStatusTime() {
    return new Date(this.definingStatusTime.getTime());
  }
  */
  public LocalDateTime getDefiningStatusTime() {
    return definingStatusTime;
  }

  /**
   * Set time of defining status.
   * @param timeDefiningStatus new value time of defining status
   */
  /*
  public void setDefiningStatusTime(Date timeDefiningStatus) {
    this.definingStatusTime = new Date(timeDefiningStatus.getTime());
  }
  */
  public void setDefiningStatusTime(LocalDateTime timeDefiningStatus) {
    this.definingStatusTime = timeDefiningStatus;
  }
}
