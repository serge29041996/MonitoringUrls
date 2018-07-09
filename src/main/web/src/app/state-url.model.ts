export class StateUrl {
  public id: number;

  constructor(public url: string, public status: string, public problem: string,
              public isMonitoring: boolean) {}
}
