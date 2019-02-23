export class ParametersMonitoringUrl {
  public id: number;

  constructor(public url: string, public beginTimeMonitoring: string,
              public endTimeMonitoring: string, public timeResponseOk: number,
              public timeResponseWarning: number, public timeResponseCritical: number,
              public expectedCodeResponse: number, public minSizeResponse: number,
              public maxSizeResponse: number, public substringResponse: string) {}
}
