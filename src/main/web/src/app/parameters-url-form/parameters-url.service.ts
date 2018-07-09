import { HttpClient } from '@angular/common/http';
import {catchError} from "rxjs/operators/catchError";
import {HttpErrorResponse} from "@angular/common/http";
import {ErrorObservable} from "rxjs/observable/ErrorObservable";
import {ParametersMonitoringUrl} from "./parameters-monitoring-url.model";
import {HttpHeaders} from "@angular/common/http";

export class ParametersUrlService {
  constructor(private http: HttpClient) {}

  getParametersMonitoringUrl(id: number) {
    return this.http.get('//localhost:8080/parametersMonitoringUrl/' + id)
      .pipe(
        catchError(this.handleError)
      );
  }

  saveParametersMonitoringUrl(parametersMonitoringUrl: ParametersMonitoringUrl) {
    const body = JSON.stringify(parametersMonitoringUrl);
    const header = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post('//localhost:8080/parametersMonitoringUrl', body, {headers: header})
      .pipe(
        catchError(this.handleError)
      );
  }

  updateParametersMonitoringUrl(parametersMonitoringUrl: ParametersMonitoringUrl) {
    const body = JSON.stringify(parametersMonitoringUrl);
    const header = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.put('//localhost:8080/parametersMonitoringUrl', body, {headers: header})
      .pipe(
        catchError(this.handleError)
      );
  }

  deleteParametersMonitoringUrl(parametersMonitoringUrl: ParametersMonitoringUrl) {
    return this.http.delete('//localhost:8080/parametersMonitoringUrl/' + parametersMonitoringUrl.id);
  }

  private handleError(error: HttpErrorResponse) {
    return new ErrorObservable(error.error);
  }
}
