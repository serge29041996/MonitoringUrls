export class ParametersMonitoringUrl {
  public id: number;

  constructor(public url: string, public beginTimeMonitoring: Date,
              public endTimeMonitoring: Date, public timeResponseOk: number,
              public timeResponseWarning: number, public timeResponseCritical: number,
              public expectedCodeResponse: number, public minSizeResponse: number,
              public maxSizeResponse: number, public substringResponse: string) {}
}
