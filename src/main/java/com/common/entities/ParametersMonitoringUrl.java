/*
 * Copyright (C) 2019.
 * This file is part of project MonitoringUrls
 * Written by Sergiy Krasnikov <sergei29041996@gmail.com>
 */

package com.common.entities;

import com.common.ParametersMonitoringUrlDto;
import com.common.PeriodMonitoring;
import com.common.SizeResponseData;
import com.common.TimeResponseData;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Entity for managing information about url and parameters for checking its status in.
 * the database
 * Similar object for working with database is ParametersMonitoringUrl
 */
@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = "id", doNotUseGetters = true)
@Table(name = "parametersmonitoringurl")
public class ParametersMonitoringUrl {
  /** Id of the parameters monitoring url. */
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @ApiModelProperty(notes = "The database generated parameters monitoring ID")
  private long id;

  /** Url for monitoring status. */
  @NotBlank(message = "Url should not be empty")
  @Size(message = "Url cannot have length greater than 500 symbols", max = 500)
  @ApiModelProperty(notes = "Url for monitoring")
  private String url;

  /** Begin time of monitoring url. */
  /*
  @NotNull
  @Column(name = "begintimemonitoring")
  @Temporal(TemporalType.TIME)
  @ApiModelProperty(notes = "Begin time for monitoring url")
  private Date beginTimeMonitoring;
  */
  @NotNull
  @ApiModelProperty(notes = "Begin time for monitoring url")
  @Column(name = "begintimemonitoring", columnDefinition = "TIME")
  private LocalTime beginTimeMonitoring;

  /** End time of monitoring url. */
  /*
  @NotNull
  @Column(name = "endtimemonitoring")
  @Temporal(TemporalType.TIME)
  @ApiModelProperty(notes = "End time for monitoring url")
  private Date endTimeMonitoring;
  */
  @NotNull
  @ApiModelProperty(notes = "End time for monitoring url")
  @Column(name = "endtimemonitoring", columnDefinition = "TIME")
  private LocalTime endTimeMonitoring;

  /** Max border of number seconds for status ok. */
  @Positive(message = "Time for the response to ok should be greater than 0")
  @Column(name = "timeresponseok")
  @ApiModelProperty(notes = "Max number seconds for status OK")
  private long timeResponseOk;

  /** Max border of number seconds for status warning. */
  @Positive(message = "Time for the response to warning should be greater than 0")
  @Column(name = "timeresponsewarning")
  @ApiModelProperty(notes = "Max number seconds for status Warning")
  private long timeResponseWarning;

  /** Max border of number seconds for status critical. */
  @Positive(message = "Time for the response to critical should be greater than 0")
  @Column(name = "timeresponsecritical")
  @ApiModelProperty(notes = "Max number seconds for status Critical")
  private long timeResponseCritical;

  /** Expected code for response. */
  @Positive(message = "Expected response should be positive number")
  @Min(message = "Expected code of response should be greater than 100", value = 100)
  @Max(message = "Expected code of response should be less than 527", value = 511)
  @Column(name = "expectedcoderesponse")
  @ApiModelProperty(notes = "Expected code of response")
  private int expectedCodeResponse;

  /** Min border size of response. */
  @Positive(message = "Min size of response should be greater than 0")
  @Column(name = "minsizeresponse")
  @ApiModelProperty(notes = "Min size of response")
  private int minSizeResponse;

  /** Max border size of response. */
  @Positive(message = "Max size of response should be greater than 0")
  @Column(name = "maxsizeresponse")
  @ApiModelProperty(notes = "Max size of response")
  private int maxSizeResponse;

  /** Optional parameter. String, which should be in response. */
  @Column(name = "substringresponse")
  @ApiModelProperty(notes = "String, which expect in response")
  private String substringResponse;

  /**
   * Constructor for all fields, expect optional parameter substring of response.
   * @param url url for monitoring
   * @param periodMonitoring interval time of monitoring which contains begin and end time
   *                         monitoring
   * @param timeResponseData time of getting response for statuses OK, Warning, Critical
   * @param expectedCodeResponse expected code of http response
   * @param sizeResponseData interval size of response which contains min and max size of http
   *                         response
   */
  public ParametersMonitoringUrl(String url, PeriodMonitoring periodMonitoring,
                                 TimeResponseData timeResponseData, int expectedCodeResponse,
                                 SizeResponseData sizeResponseData) {
    this.url = url;
    /*
    this.beginTimeMonitoring = new Date(periodMonitoring.getBeginTime().getTime());
    this.endTimeMonitoring = new Date(periodMonitoring.getEndTime().getTime());
    */
    this.beginTimeMonitoring = periodMonitoring.getBeginTime();
    this.endTimeMonitoring = periodMonitoring.getEndTime();
    this.timeResponseOk = timeResponseData.getTimeResponseOk();
    this.timeResponseWarning = timeResponseData.getTimeResponseWarning();
    this.timeResponseCritical = timeResponseData.getTimeResponseCritical();
    this.expectedCodeResponse = expectedCodeResponse;
    this.minSizeResponse = sizeResponseData.getMinSizeResponse();
    this.maxSizeResponse = sizeResponseData.getMaxSizeResponse();
  }

  /**
   * Constructor for all fields.
   * @param url url for monitoring
   * @param periodMonitoring interval time of monitoring which contains begin and end time
   *                         monitoring
   * @param timeResponseData time of getting response for statuses OK, Warning, Critical
   * @param expectedCodeResponse expected code of http response
   * @param sizeResponseData interval size of response which contains min and max size of http
   *                         response
   * @param substringResponse string, which expect to find in response
   */
  public ParametersMonitoringUrl(String url, PeriodMonitoring periodMonitoring,
                                 TimeResponseData timeResponseData, int expectedCodeResponse,
                                 SizeResponseData sizeResponseData, String substringResponse) {
    this(url, periodMonitoring, timeResponseData, expectedCodeResponse, sizeResponseData);
    this.substringResponse = substringResponse;
  }

  /**
   * Constructor for saving data from DTO object.
   * @param parametersMonitoringUrlDto DTO representation of entity
   */
  public ParametersMonitoringUrl(ParametersMonitoringUrlDto parametersMonitoringUrlDto) {
    this.id = parametersMonitoringUrlDto.getId();
    this.url = parametersMonitoringUrlDto.getUrl();
    this.beginTimeMonitoring = parametersMonitoringUrlDto.getBeginTimeMonitoring();
    this.endTimeMonitoring = parametersMonitoringUrlDto.getEndTimeMonitoring();
    this.timeResponseOk = parametersMonitoringUrlDto.getTimeResponseOk();
    this.timeResponseWarning = parametersMonitoringUrlDto.getTimeResponseWarning();
    this.timeResponseCritical = parametersMonitoringUrlDto.getTimeResponseCritical();
    this.expectedCodeResponse = parametersMonitoringUrlDto.getExpectedCodeResponse();
    this.minSizeResponse = parametersMonitoringUrlDto.getMinSizeResponse();
    this.maxSizeResponse = parametersMonitoringUrlDto.getMaxSizeResponse();
    this.substringResponse = parametersMonitoringUrlDto.getSubstringResponse();
  }

  /**
   * Get begin time for monitoring.
   * @return begin time for monitoring
   */
  /*
  public Date getBeginTimeMonitoring() {
    return new Date(this.beginTimeMonitoring.getTime());
  }*/
  public LocalTime getBeginTimeMonitoring() {
    return beginTimeMonitoring;
  }

  /**
   * Set begin time for monitoring.
   * @param beginTimeMonitoring new value of begin time for monitoring
   */
  /*
  public void setBeginTimeMonitoring(Date beginTimeMonitoring) {
    this.beginTimeMonitoring = new Date(beginTimeMonitoring.getTime());
  }
  */
  public void setBeginTimeMonitoring(LocalTime beginTimeMonitoring) {
    this.beginTimeMonitoring = beginTimeMonitoring;
  }

  /**
   * Get end time for monitoring.
   * @return end time for monitoring
   */
  /*
  public Date getEndTimeMonitoring() {
    return new Date(this.endTimeMonitoring.getTime());
  }
  */
  public LocalTime getEndTimeMonitoring() {
    return endTimeMonitoring;
  }

  /**
   * Set end time for monitoring.
   * @param endTimeMonitoring new value of end time for monitoring
   */
  /*
  public void setEndTimeMonitoring(Date endTimeMonitoring) {
    this.endTimeMonitoring = new Date(endTimeMonitoring.getTime());
  }
  */
  public void setEndTimeMonitoring(LocalTime endTimeMonitoring) {
    this.endTimeMonitoring = endTimeMonitoring;
  }
}
