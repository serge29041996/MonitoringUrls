package com.common;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StatusInfo {
  private long idUrl;
  private String status;
  private String causeStatus;

  public StatusInfo(long idUrl) {
    this.idUrl = idUrl;
    this.causeStatus = "";
  }
}
