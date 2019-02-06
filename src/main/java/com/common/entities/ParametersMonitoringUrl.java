package com.common.entities;

import com.common.PeriodMonitoring;
import com.common.SizeResponseData;
import com.common.TimeResponseData;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;


@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = "id", doNotUseGetters = true)
@Table(name = "parametersmonitoringurl")
public class ParametersMonitoringUrl {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @ApiModelProperty(notes = "The database generated parameters monitoring ID")
  private long id;

  @NotBlank(message = "Url should not be empty")
  @Size(max = 500, message = "Url cannot have length greater than 500 symbols")
  @ApiModelProperty(notes = "Url for monitoring")
  private String url;

  @NotNull
  @Column(name = "begintimemonitoring")
  @Temporal(TemporalType.TIME)
  @ApiModelProperty(notes = "Begin time for monitoring url")
  private Date beginTimeMonitoring;

  @NotNull
  @Column(name = "endtimemonitoring")
  @Temporal(TemporalType.TIME)
  @ApiModelProperty(notes = "End time for monitoring url")
  private Date endTimeMonitoring;

  @Positive(message = "Time for the response to ok should be greater than 0")
  @Column(name = "timeresponseok")
  @ApiModelProperty(notes = "Max number seconds for status OK")
  private long timeResponseOk;

  @Positive(message = "Time for the response to warning should be greater than 0")
  @Column(name = "timeresponsewarning")
  @ApiModelProperty(notes = "Max number seconds for status Warning")
  private long timeResponseWarning;

  @Positive(message = "Time for the response to critical should be greater than 0")
  @Column(name = "timeresponsecritical")
  @ApiModelProperty(notes = "Max number seconds for status Critical")
  private long timeResponseCritical;

  @Positive(message = "Expected response should be positive number")
  @Min(value = 100, message = "Expected code of response should be greater than 100")
  @Max(value = 511, message = "Expected code of response should be less than 527")
  @Column(name = "expectedcoderesponse")
  @ApiModelProperty(notes = "Expected code of response")
  private int expectedCodeResponse;

  @Positive(message = "Min size of response should be greater than 0")
  @Column(name = "minsizeresponse")
  @ApiModelProperty(notes = "Min size of response")
  private int minSizeResponse;

  @Positive(message = "Max size of response should be greater than 0")
  @Column(name = "maxsizeresponse")
  @ApiModelProperty(notes = "Max size of response")
  private int maxSizeResponse;

  @Column(name = "substringresponse")
  @ApiModelProperty(notes = "String, which expect in response")
  private String substringResponse;

  /**
   * Constructor for all fields, expect substring of response.
   * @param url url for monitoring
   * @param periodMonitoring interval time of monitoring
   * @param timeResponseData time response for statuses OK, Warning, Critical
   * @param expectedCodeResponse expected code of response
   * @param sizeResponseData interval size of response
   */
  public ParametersMonitoringUrl(String url, PeriodMonitoring periodMonitoring,
                                 TimeResponseData timeResponseData, int expectedCodeResponse,
                                 SizeResponseData sizeResponseData) {
    this.url = url;
    this.beginTimeMonitoring = new Date(periodMonitoring.getBeginTime().getTime());
    this.endTimeMonitoring = new Date(periodMonitoring.getEndTime().getTime());
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
   * @param periodMonitoring interval time of monitoring
   * @param timeResponseData time response for statuses OK, Warning, Critical
   * @param expectedCodeResponse expected code of response
   * @param sizeResponseData interval size of response
   * @param substringResponse substring, which expect to find in response
   */
  public ParametersMonitoringUrl(String url, PeriodMonitoring periodMonitoring,
                                 TimeResponseData timeResponseData, int expectedCodeResponse,
                                 SizeResponseData sizeResponseData, String substringResponse) {
    this(url, periodMonitoring, timeResponseData, expectedCodeResponse, sizeResponseData);
    this.substringResponse = substringResponse;
  }

  public Date getBeginTimeMonitoring() {
    return new Date(this.beginTimeMonitoring.getTime());
  }

  public void setBeginTimeMonitoring(Date beginTimeMonitoring) {
    this.beginTimeMonitoring = new Date(beginTimeMonitoring.getTime());
  }

  public Date getEndTimeMonitoring() {
    return new Date(this.endTimeMonitoring.getTime());
  }

  public void setEndTimeMonitoring(Date endTimeMonitoring) {
    this.endTimeMonitoring = new Date(endTimeMonitoring.getTime());
  }
}
