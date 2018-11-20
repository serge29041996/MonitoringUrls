import {Time} from "@angular/common";
export class ParametersMonitoringUrl {
  public id: number;

  constructor(public url: string, public beginTimeMonitoring: String,
              public endTimeMonitoring: String, public timeResponseOk: number,
              public timeResponseWarning: number, public timeResponseCritical: number,
              public expectedCodeResponse: number, public minSizeResponse: number,
              public maxSizeResponse: number, public substringResponse: string) {}
}
