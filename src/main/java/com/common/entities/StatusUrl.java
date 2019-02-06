package com.common.entities;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = "id", doNotUseGetters = true)
@Table(name = "statusurl")
public class StatusUrl {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @ApiModelProperty(notes = "The database generated status of url ID")
  private long id;

  @Column(name = "definingstatustime")
  @Temporal(TemporalType.TIMESTAMP)
  @ApiModelProperty(notes = "Date and time when status is determined")
  private Date definingStatusTime;

  @NotBlank(message = "Status of url should not be empty")
  @Size(max = 8, message = "Status cannot have length greater than 8")
  @ApiModelProperty(notes = "Status which is determined")
  private String status;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "urlid", nullable = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @ApiModelProperty(notes = "Url, which status is determined")
  ParametersMonitoringUrl parametersMonitoringUrl;

  public StatusUrl(ParametersMonitoringUrl parametersMonitoringUrl) {
    this.status = "OK";
    this.parametersMonitoringUrl = parametersMonitoringUrl;
  }

  public Date getDefiningStatusTime() {
    return new Date(this.definingStatusTime.getTime());
  }

  public void setDefiningStatusTime(Date timeDefiningStatus) {
    this.definingStatusTime = new Date(timeDefiningStatus.getTime());
  }

}
