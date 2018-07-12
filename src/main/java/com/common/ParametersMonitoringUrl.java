package com.common;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = "id")
@Table(name="parametersmonitoringurl")
public class ParametersMonitoringUrl {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @NotBlank(message = "Url should not be empty")
  @Size(max = 500, message = "Url cannot have length greater than 500 symbols")
  private String url;

  @Column(name = "begintimemonitoring")
  @Temporal(TemporalType.TIME)
  private Date beginTimeMonitoring;

  @Column(name = "endtimemonitoring")
  @Temporal(TemporalType.TIME)
  private Date endTimeMonitoring;

  @Positive(message = "Time for the response to ok should be greater that null")
  @Column(name = "timeresponseok")
  private long timeResponseOk;

  @Positive(message = "Time for the response to warning should be greater that null")
  @Column(name = "timeresponsewarning")
  private long timeResponseWarning;

  @Positive(message = "Time for the response to critical should be greater that null")
  @Column(name = "timeresponsecritical")
  private long timeResponseCritical;

  @Positive(message = "Expected response should be positive number")
  @Min(value = 100, message = "Min expected code of response should be greater than 100")
  @Max(value = 526, message = "Max expected code of response should be less than 527")
  @Column(name = "expectedcoderesponse")
  private int expectedCodeResponse;

  @Positive(message = "Min size of response should be greater than 0")
  @Column(name = "minsizeresponse")
  private int minSizeResponse;

  @Positive(message = "Max size of response should be greater than 0")
  @Column(name = "maxsizeresponse")
  private int maxSizeResponse;

  @Column(name = "substringresponse")
  private String substringResponse;

  public ParametersMonitoringUrl(String url, Date beginTimeMonitoring, Date endTimeMonitoring,
                                 long timeResponseOk, long timeResponseWarning, long timeResponseCritical,
                                 int expectedCodeResponse, int minSizeResponse, int maxSizeResponse) {
    this.url = url;
    this.beginTimeMonitoring = beginTimeMonitoring;
    this.endTimeMonitoring = endTimeMonitoring;
    this.timeResponseOk = timeResponseOk;
    this.timeResponseWarning = timeResponseWarning;
    this.timeResponseCritical = timeResponseCritical;
    this.expectedCodeResponse = expectedCodeResponse;
    this.minSizeResponse = minSizeResponse;
    this.maxSizeResponse = maxSizeResponse;
  }

  public ParametersMonitoringUrl(String url, Date beginTimeMonitoring, Date endTimeMonitoring,
                                 long timeResponseOk, long timeResponseWarning, long timeResponseCritical,
                                 int expectedCodeResponse, int minSizeResponse, int maxSizeResponse,
                                 String substringResponse) {
    this(url, beginTimeMonitoring, endTimeMonitoring, timeResponseOk, timeResponseWarning, timeResponseCritical,
        expectedCodeResponse, minSizeResponse, maxSizeResponse);
    this.substringResponse = substringResponse;
  }
}
