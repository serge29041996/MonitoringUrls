package com.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = "id", doNotUseGetters = true)
@Table(name = "statusurl")
public class StatusUrl {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @NotNull
  @Column(name = "definingstatustime")
  @Temporal(TemporalType.TIMESTAMP)
  private Date definingStatusTime;

  @NotBlank(message = "Status of url should not be empty")
  @Size(max = 8, message = "Status cannot have length greater than 8")
  private String status;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "urlid", nullable = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @ApiModelProperty(notes = "Url, which url defined")
  ParametersMonitoringUrl parametersMonitoringUrl;

  public StatusUrl(ParametersMonitoringUrl parametersMonitoringUrl) {
    this.status = "CRITICAL";
    this.parametersMonitoringUrl = parametersMonitoringUrl;
  }

  public Date getTimeDefiningStatus() {
    return new Date(this.definingStatusTime.getTime());
  }

  public void setTimeDefiningStatus(Date timeDefiningStatus) {
    this.definingStatusTime = new Date(timeDefiningStatus.getTime());
  }
}
