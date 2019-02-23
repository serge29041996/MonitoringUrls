import { ValidateNumberFieldData } from './validate-number-field-data.model';
import {Component, OnInit, ViewChild} from '@angular/core';
import {ParametersMonitoringUrl} from './parameters-monitoring-url.model';
import {Router, ActivatedRoute} from '@angular/router';
import {NgForm} from '@angular/forms';
import {isUndefined} from 'util';
import {ParametersUrlService} from './parameters-url.service';

@Component({
  selector: 'app-parameters-url-form',
  templateUrl: './parameters-url-form.component.html',
  styleUrls: ['./parameters-url-form.component.css']
})
export class ParametersUrlFormComponent implements OnInit {
  editMode: boolean;
  parametersMonitoringUrl: ParametersMonitoringUrl;
  oldParametersMonitoringUrl: ParametersMonitoringUrl;
  @ViewChild('f') parametersForm: NgForm;
  errors;
  styleForMessage = '';
  private validHttpStatus: number[];
  resultMessage = '';
  idUpdatedUrl: number;
  isValidId = true;

  constructor(private router: Router, private route: ActivatedRoute,
              private parametersUrlService: ParametersUrlService) { }

  ngOnInit() {
    /*
    console.log(this.route.snapshot.params['id']);
    console.log(this.route.snapshot.params['operation']);
    */
    if (this.route.snapshot.params['id']) {
      if (this.route.snapshot.params['operation'] !== 'update') {
        this.router.navigate(['/not-found']);
        // console.log('Invalid operation');
      } else {
        if (!this.isNumber(this.route.snapshot.params['id'])) {
          this.router.navigate(['/not-found']);
          // console.log('Invalid id');
        } else {
          this.editMode = true;
          this.idUpdatedUrl = +this.route.snapshot.params['id'];

          this.parametersUrlService.getParametersMonitoringUrl(this.idUpdatedUrl).subscribe(
            (data: ParametersMonitoringUrl) => {
              console.log('Data from rest');
              console.log(data);
              this.oldParametersMonitoringUrl = data;
              if (data.substringResponse == null) {
                this.oldParametersMonitoringUrl.substringResponse = '';
              } else {
                this.oldParametersMonitoringUrl.substringResponse = data.substringResponse;
              }

              this.parametersMonitoringUrl = data;
              if (data.substringResponse == null) {
                this.parametersMonitoringUrl.substringResponse = '';
              } else {
                this.parametersMonitoringUrl.substringResponse = data.substringResponse;
              }

              console.log('old parameters: ');
              console.log(this.oldParametersMonitoringUrl);
              console.log('current parameters: ');
              console.log(this.parametersMonitoringUrl);

              this.initializationErrorsOfFields();
            },
            (error) => {
              this.resultMessage = 'Parameters for url with id ' + this.idUpdatedUrl + ' is not exist';
              this.isValidId = false;
            }
          );
        }
      }
    } else {
      if (this.route.snapshot.params['operation'] !== 'create') {
        // this.router.navigate(['/not-found']);
        console.log('Invalid operation for create');
      } else {
        // console.log('Id is not a parameter');
        const date = new Date();
        date.setHours(0, 0);

        const beginTimeMonitoring = date.toTimeString().substring(0, 5);
        const endTimeMonitoring = date.toTimeString().substring(0, 5);

        this.parametersMonitoringUrl = new ParametersMonitoringUrl('', beginTimeMonitoring,
         endTimeMonitoring, 1, 2, 3, 100, 1, 1000, '');
        this.initializationErrorsOfFields();
      }
    }
  }

  initializationErrorsOfFields() {
    const keysParameters = Object.keys(this.parametersMonitoringUrl);
    // console.log(keysParameters);
    const countElements = keysParameters.length;
    this.errors = [];
    for (let i = 0; i < countElements; i++) {
      if (keysParameters[i] !== 'id') {
        this.errors.push({
          'field': keysParameters[i],
          'message': ''
        });
      }
    }

    this.errors.push({
      'field': 'summaryError',
      'message': ''
    });

    // console.log(this.errors);
  }

  resetValidationErrorsOfFields() {
    for (let i = 0; i < 11; i++) {
      this.errors[i].message = '';
    }
  }

  onSubmit() {
    this.resetValidationErrorsOfFields();
    console.log(this.parametersForm);
    if (!this.editMode) {
      this.validUrlField();
      this.validTimeMonitoringFields();
      this.validTimeResponseFields();
      this.validateExpectedCodeResponseField();
      this.validateSizeOfResponseFields();
      if (!this.isHaveErrors()) {
        console.log(this.parametersMonitoringUrl);
        if (this.parametersMonitoringUrl.substringResponse === '') {
          this.parametersMonitoringUrl.substringResponse = null;
        }

        this.getTimeMonitoringsAsISOFormat();

        this.parametersUrlService.saveParametersMonitoringUrl(this.parametersMonitoringUrl).subscribe(
          (data: any) => {
            this.resultMessage = 'The parameters for monitoring url has saved.';
            this.outputMessage();
            this.parametersForm.resetForm();
            this.resetDataOfParametersMonitoringUrl();
          },
          (error) => {
            if (error.subErrors === null) {
              this.errors[10].message = error.message;
              this.outputMessage();
            } else if (error.subErrors.length === 0) {
              this.errors[10].message = error.message;
              this.outputMessage();
            } else {
              this.setErrorsOfResponse(error.subErrors);
            }
          }
        );
      } else {
        this.outputMessage();
      }
    }
  }

  validUrlField() {
    if (this.parametersForm.value['url'] === '') {
      this.errors[0].message = 'Field cannot be empty';
    }
  }

  validTimeMonitoringFields() {
    let isNotEmptyFields = true;
    let beginTimeMonitoring;
    let endTimeMonitoring;

    if (this.parametersForm.value['beginTimeMonitoring'] === '') {
      isNotEmptyFields = this.initializeEmptyMonitoringTimeFieldError(1);
    } else {
      beginTimeMonitoring = this.
      formateDateFromString(this.parametersForm.value['beginTimeMonitoring']);
    }

    if (this.parametersForm.value['endTimeMonitoring'] === '') {
      isNotEmptyFields = this.initializeEmptyMonitoringTimeFieldError(2);
    } else {
      endTimeMonitoring = this.
      formateDateFromString(this.parametersForm.value['endTimeMonitoring']);
    }

    if (isNotEmptyFields) {
      if (beginTimeMonitoring.getTime() > endTimeMonitoring.getTime()) {
        this.errors[10].message = this.errors[10].message.concat('Begin time is more than end time.');
      } else if (beginTimeMonitoring.getTime() === endTimeMonitoring.getTime()) {
        this.errors[10].message = this.errors[10].message.concat('Begin and end time is equal.');
      }
    }
  }

  initializeEmptyMonitoringTimeFieldError (index: number): boolean {
    this.errors[index].message = 'Field cannot be empty';
    return false;
  }

  formateDateFromString(stringDate): Date {
    const stringWithDate = stringDate;
    const formatedDate = new Date();
    formatedDate.setMinutes(stringWithDate.substring(0, 2),
    stringWithDate.substring(3, 5), 0);

    return formatedDate;
  }

  private isNumber(fieldValue: string): boolean {
    const value = parseInt(fieldValue, 10);
    return !isNaN(value) && isFinite(value);
  }

  validTimeResponseFields() {
    let isValidTimeResponseFields = true;
    let timeResponseOK;
    let timeResponseWarning;
    let timeResponseCritical;
    const timeResponseData = new ValidateNumberFieldData(0, true);
    this.validateTimeResponseField(timeResponseData, this.parametersForm.value['timeResponseOk'],
     3, 'OK');
    timeResponseOK = timeResponseData.numberData;

    this.validateTimeResponseField(timeResponseData,
      this.parametersForm.value['timeResponseWarning'], 4, 'Warning');
    timeResponseWarning = timeResponseData.numberData;

    this.validateTimeResponseField(timeResponseData,
      this.parametersForm.value['timeResponseCritical'], 5, 'Critical');
    timeResponseCritical = timeResponseData.numberData;

    isValidTimeResponseFields = timeResponseData.isValidField;

    if (isValidTimeResponseFields) {
      if (timeResponseOK === timeResponseWarning || timeResponseOK === timeResponseCritical) {
        this.errors[10].message = this.errors[10].message.concat('Time response for status OK cannot be equal to ' +
          'other statuses.');
      }
      const minTimeResponse = Math.min(Math.min(timeResponseOK, timeResponseWarning),
      timeResponseCritical);
      if (minTimeResponse !== timeResponseOK) {
        this.errors[10].message = this.errors[10].message.concat('Time response for status OK should have the ' +
          'smallest value.');
      }
      const maxTimeResponse = Math.max(Math.max(timeResponseOK, timeResponseWarning),
      timeResponseCritical);
      if (maxTimeResponse !== timeResponseCritical) {
        this.errors[10].message = this.errors[10].message.concat('Time response for status Critical should be ' +
          'the biggest value.');
      }
    }
  }

  validateTimeResponseField(data: ValidateNumberFieldData, timeResponse: string, index: number,
    nameStatus: string) {
    if (this.isNumber(timeResponse)) {
      data.numberData = +timeResponse;
      if (data.numberData < 1) {
        this.errors[index].message = 'Time response for status ' + nameStatus +
         ' cannot be less than 1';
        data.isValidField = false;
      }
    } else {
      this.errors[index].message = 'Time response for status ' + nameStatus +  ' is not number';
      data.isValidField = false;
    }
  }

  initializeValidHttpStatuses() {
    this.validHttpStatus = [];
    this.initializeStatusesInCycle(100, 104);
    this.initializeStatusesInCycle(200, 209);
    this.validHttpStatus.push(226);
    this.initializeStatusesInCycle(300, 309);
    this.initializeStatusesInCycle(400, 419);
    this.initializeStatusesInCycle(421, 425);
    this.validHttpStatus.push(426);
    this.validHttpStatus.push(428);
    this.validHttpStatus.push(429);
    this.validHttpStatus.push(431);
    this.validHttpStatus.push(451);
    this.initializeStatusesInCycle(500, 512);
  }

  initializeStatusesInCycle(beginIndex: number, endIndex: number) {
    for (let i = beginIndex; i < endIndex; i++) {
      this.validHttpStatus.push(i);
    }
  }

  validateExpectedCodeResponseField() {
    if (isUndefined(this.validHttpStatus)) {
      this.initializeValidHttpStatuses();
    }

    if (this.validHttpStatus.indexOf(this.parametersForm.value['expectedCodeResponse']) === -1) {
      this.errors[6].message = 'Invalid expected code of response';
    }
  }

  validateSizeOfResponseFields() {
    let isValidField = true;
    let minSizeOfResponse;
    let maxSizeOfResponse;
    const sizeResponseData = new ValidateNumberFieldData(0, isValidField);

    this.validateSizeResponseField(sizeResponseData,
      this.parametersForm.value['minSizeResponse'], 7, 'Min');
    minSizeOfResponse = sizeResponseData.numberData;
    this.validateSizeResponseField(sizeResponseData,
      this.parametersForm.value['maxSizeResponse'], 8, 'Max');
    maxSizeOfResponse = sizeResponseData.numberData;

    isValidField = sizeResponseData.isValidField;

    if (isValidField) {
      if (minSizeOfResponse > maxSizeOfResponse) {
        this.errors[10].message = this.errors[10].message.concat('Min size of response cannot be more than max size ' +
          'of response');
      }
      if (minSizeOfResponse === maxSizeOfResponse) {
        this.errors[10].message = this.errors[10].message.concat('Min size of response cannot be equal max size ' +
          'of response');
      }
    }
  }

  validateSizeResponseField(sizeResponseData: ValidateNumberFieldData, data: string,
    index: number, nameField: string) {
      if (this.isNumber(data)) {
        sizeResponseData.numberData = +data;
        if (sizeResponseData.numberData < 1) {
          sizeResponseData.isValidField = false;
          this.errors[index].message = nameField + ' size of response cannot be less than 1';
        }
      } else {
        this.errors[index].message = nameField + ' size of response is not number';
        sizeResponseData.isValidField = false;
      }
  }


  isHaveErrors() {
    let isNotHaveErrors = true;
    for (let i = 0; i < 11; i++) {
      if (this.errors[i].message !== '') {
        isNotHaveErrors = false;
        break;
      }
    }
    return !isNotHaveErrors;
  }

  getTimeMonitoringsAsISOFormat() {
    const beginTimeMonitoring = this.
    formateDateFromString(this.parametersForm.value['beginTimeMonitoring']);

    this.parametersMonitoringUrl.beginTimeMonitoring = beginTimeMonitoring.toISOString();

    const endTimeMonitoring = this.formateDateFromString(
      this.parametersForm.value['endTimeMonitoring']);

    this.parametersMonitoringUrl.endTimeMonitoring = endTimeMonitoring.toISOString();
  }

  resetDataOfParametersMonitoringUrl() {
    const date = new Date();
    date.setHours(0, 0);

    const beginTimeMonitoring = date.toTimeString().substring(0, 5);
    const endTimeMonitoring = date.toTimeString().substring(0, 5);

    this.parametersMonitoringUrl.url = '';
    this.parametersMonitoringUrl.beginTimeMonitoring = beginTimeMonitoring;
    this.parametersMonitoringUrl.endTimeMonitoring = endTimeMonitoring;
    this.parametersMonitoringUrl.timeResponseOk = 1;
    this.parametersMonitoringUrl.timeResponseWarning = 2;
    this.parametersMonitoringUrl.timeResponseCritical = 3;
    this.parametersMonitoringUrl.expectedCodeResponse = 100;
    this.parametersMonitoringUrl.minSizeResponse = 1;
    this.parametersMonitoringUrl.maxSizeResponse = 1000;
    this.parametersMonitoringUrl.substringResponse = '';
  }

  outputMessage() {
    this.styleForMessage = 'output-message';
    setTimeout(
      () => this.styleForMessage = '', 3000);
  }

  setErrorsOfResponse(errors: {field: string, message: string }[]) {
    for (let i = 0; i < errors.length; i++) {
      const someArray = [];

      this.errors.find(
        x => x.field === errors[i].field
      ).message = errors[i].message;
    }
  }

  onReturnToList() {
    this.router.navigate(['/']);
  }
}
