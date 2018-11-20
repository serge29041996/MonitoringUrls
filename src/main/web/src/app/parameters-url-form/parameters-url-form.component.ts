import {Component, OnInit, ViewChild} from '@angular/core';
import {ParametersMonitoringUrl} from './parameters-monitoring-url.model';
import {Router, ActivatedRoute} from '@angular/router';
import {NgForm} from "@angular/forms";
import {Time} from "@angular/common";
import {isUndefined} from "util";
import {ParametersUrlService} from "./parameters-url.service";

@Component({
  selector: 'app-parameters-url-form',
  templateUrl: './parameters-url-form.component.html',
  styleUrls: ['./parameters-url-form.component.css']
})
export class ParametersUrlFormComponent implements OnInit {
  editMode: boolean;
  parametersMonitoringUrl: ParametersMonitoringUrl;
  oldParametersMonitoringUrl: ParametersMonitoringUrl;
  @ViewChild("f") parametersForm: NgForm;
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
    if(this.route.snapshot.params['id']) {
      if (this.route.snapshot.params['operation'] !== 'update') {
        this.router.navigate(['/not-found']);
        //console.log('Invalid operation');
      } else {
        if (!this.isNumber(this.route.snapshot.params['id'])) {
          this.router.navigate(['/not-found']);
          //console.log('Invalid id');
        } else {
          this.editMode = true;
          this.idUpdatedUrl = +this.route.snapshot.params['id'];

          this.parametersUrlService.getParametersMonitoringUrl(this.idUpdatedUrl).subscribe(
            (data: ParametersMonitoringUrl) => {
              console.log("Data from rest");
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

              console.log("old parameters: ");
              console.log(this.oldParametersMonitoringUrl);
              console.log("current parameters: ");
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
        //this.router.navigate(['/not-found']);
        console.log('Invalid operation for create');
      } else {
        console.log("Id is not a parameter");
        let date = new Date();
        date.setHours(0, 0);

        let beginTimeMonitoring = date.toTimeString().substring(0,5);
        let endTimeMonitoring = date.toTimeString().substring(0,5);

        this.parametersMonitoringUrl = new ParametersMonitoringUrl('', beginTimeMonitoring, endTimeMonitoring, 1, 2,
          3, 100, 1, 1000, '');
        this.initializationErrorsOfFields();
      }
    }
  }

  initializationErrorsOfFields() {
    let keysParameters = Object.keys(this.parametersMonitoringUrl);
    console.log(keysParameters);
    let countElements = keysParameters.length;
    this.errors = [];
    for (let i = 0; i < countElements; i++) {
      if(keysParameters[i] !== 'id') {
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

    console.log(this.errors);
  }

  resetValidationErrorsOfFields() {
    for (let i = 0; i < 11; i++) {
      this.errors[i].message = '';
    }
  }

  onSubmit() {
    this.resetValidationErrorsOfFields();
    console.log(this.parametersForm);
    if(!this.editMode) {
      this.validUrlField();
      this.validTimeMonitoringFields();
      this.validTimeResponseFields();
      this.validateExpectedCodeResponseField();
      this.validateSizeOfResponseFields();
      if(!this.isHaveErrors()) {
        console.log(this.parametersMonitoringUrl);
        if (this.parametersMonitoringUrl.substringResponse === '') {
          this.parametersMonitoringUrl.substringResponse = null;
        }

        this.getDateAsISOFormat();

        this.parametersUrlService.saveParametersMonitoringUrl(this.parametersMonitoringUrl).subscribe(
          (data: any) => {
            this.resultMessage = 'The parameters for monitoring url has saved.';
            this.outputMessage();
            this.parametersForm.resetForm();
            this.resetDataOfParametersMonitoringUrl();
          },
          (error) => {
            if(error.subErrors === null){
              this.errors[10].message = error.message;
              this.outputMessage();
            }
            else if(error.subErrors.length === 0){
              this.errors[10].message = error.message;
              this.outputMessage();
            }
            else{
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
    if (this.parametersForm.value["url"] == '') {
      this.errors[0].message = "Field is empty";
    }
  }

  validTimeMonitoringFields() {
    let isNotEmptyFields = true;
    let beginTimeMonitoring;
    let endTimeMonitoring;

    if (this.parametersForm.value["beginTimeMonitoring"] === '') {
      this.errors[1].message = "Field cannot be empty";
      isNotEmptyFields = false;
    } else {
      let beginTimeMonitoringString = this.parametersForm.value["beginTimeMonitoring"];
      beginTimeMonitoring = new Date();
      beginTimeMonitoring.setMinutes(beginTimeMonitoringString.substring(0, 2),
        beginTimeMonitoringString.substring(3, 5), 0);
    }

    if (this.parametersForm.value["endTimeMonitoring"] === '') {
      this.errors[2].message = "Field cannot be empty";
      isNotEmptyFields = false;
    } else {
      let endTimeMonitoringString = this.parametersForm.value["endTimeMonitoring"];
      endTimeMonitoring = new Date();
      endTimeMonitoring.setMinutes(endTimeMonitoringString.substring(0, 2),
        endTimeMonitoringString.substring(3, 5), 0);
    }

    if (isNotEmptyFields) {
      if (beginTimeMonitoring.getTime() > endTimeMonitoring.getTime()) {
        this.errors[10].message = this.errors[10].message.concat("Begin time is more than end time.");
      } else if (beginTimeMonitoring.getTime() == endTimeMonitoring.getTime()) {
        this.errors[10].message = this.errors[10].message.concat("Begin and end time is equal.");
      }
    }
  }

  private isNumber(fieldValue: string): boolean {
    let value = parseInt(fieldValue);
    return !isNaN(value) && isFinite(value);
  }

  validTimeResponseFields() {
    let isValidTimeResponseFields = true;
    let timeResponseOK;
    let timeResponseWarning;
    let timeResponseCritical;
    if (this.isNumber(this.parametersForm.value["timeResponseOk"])) {
      timeResponseOK = +this.parametersForm.value["timeResponseOk"];
      if (timeResponseOK < 1) {
        this.errors[3].message = "Time response for status OK cannot be less than 1";
        isValidTimeResponseFields = false;
      }
    } else {
      this.errors[3].message = "Time response for status OK is not number";
      isValidTimeResponseFields = false;
    }
    if (this.isNumber(this.parametersForm.value["timeResponseWarning"])) {
      timeResponseWarning = +this.parametersForm.value["timeResponseWarning"];
      if (timeResponseWarning < 1) {
        this.errors[4].message = "Time response for status Warning cannot be less than 1";
        isValidTimeResponseFields = false;
      }
    } else {
      this.errors[4].message = "Time response for status Warning is not number";
      isValidTimeResponseFields = false;
    }
    if (this.isNumber(this.parametersForm.value["timeResponseCritical"])) {
      timeResponseCritical = +this.parametersForm.value["timeResponseCritical"];
      if (timeResponseCritical < 1) {
        this.errors[5].message = "Time response for status Critical cannot be less than 1";
        isValidTimeResponseFields = false;
      }
    } else {
      this.errors[5].message = "Time response for status Critical is not number";
      isValidTimeResponseFields = false;
    }

    if (isValidTimeResponseFields) {
      if (timeResponseOK == timeResponseWarning || timeResponseOK == timeResponseCritical) {
        this.errors[10].message = this.errors[10].message.concat("Time response for status OK cannot be equal to " +
          "other statuses.");
      }
      let minTimeResponse = Math.min(Math.min(timeResponseOK, timeResponseWarning), timeResponseCritical);
      if (minTimeResponse != timeResponseOK) {
        this.errors[10].message = this.errors[10].message.concat("Time response for status OK should have the " +
          "smallest value.");
      }
      let maxTimeResponse = Math.max(Math.max(timeResponseOK, timeResponseWarning), timeResponseCritical);
      if (maxTimeResponse != timeResponseCritical) {
        this.errors[10].message = this.errors[10].message.concat("Time response for status Critical should be " +
          "the biggest value.");
      }
    }
  }

  initializeValidHttpStatuses() {
    this.validHttpStatus = [];
    for (let i = 100; i < 104; i++) {
      this.validHttpStatus.push(i);
    }

    for (let i = 200; i < 209; i++) {
      this.validHttpStatus.push(i);
    }

    this.validHttpStatus.push(226);

    for (let i = 300; i < 309; i++) {
      this.validHttpStatus.push(i);
    }

    for (let i = 400; i < 419; i++) {
      this.validHttpStatus.push(i);
    }

    for (let i = 421; i < 425; i++) {
      this.validHttpStatus.push(i);
    }

    this.validHttpStatus.push(426);
    this.validHttpStatus.push(428);
    this.validHttpStatus.push(429);
    this.validHttpStatus.push(431);
    this.validHttpStatus.push(451);

    for (let i = 500; i < 512; i++) {
      this.validHttpStatus.push(i);
    }
  }

  validateExpectedCodeResponseField() {
    let isValidRangeCode = true;
    if (isUndefined(this.validHttpStatus)) {
      this.initializeValidHttpStatuses();
    }

    if (this.validHttpStatus.indexOf(this.parametersForm.value["expectedCodeResponse"]) === -1) {
      this.errors[6].message = "Invalid expected code of response";
    }
  }

  validateSizeOfResponseFields() {
    let isValidField = true;
    let minSizeOfResponse;
    let maxSizeOfResponse;
    if (this.isNumber(this.parametersForm.value["minSizeResponse"])) {
      minSizeOfResponse = this.parametersForm.value["minSizeResponse"];
      if (minSizeOfResponse < 1) {
        isValidField = false;
        this.errors[7].message = "Min size of response cannot be less than 1";
      }
    } else {
      this.errors[7].message = "Min size of response is not number";
      isValidField = false;
    }
    if (this.isNumber(this.parametersForm.value["maxSizeResponse"])) {
      maxSizeOfResponse = this.parametersForm.value["maxSizeResponse"];
      if (maxSizeOfResponse < 1) {
        isValidField = false;
        this.errors[8].message = "Max size of response cannot be less than 1";
      }
    } else {
      isValidField = false;
      this.errors[8].message = "Max size of response is not number";
    }

    if(isValidField) {
      if (minSizeOfResponse > maxSizeOfResponse) {
        this.errors[10].message = this.errors[10].message.concat("Min size of response cannot be more than max size " +
          "of response");
      }
      if (minSizeOfResponse == maxSizeOfResponse) {
        this.errors[10].message = this.errors[10].message.concat("Min size of response cannot be equal max size " +
          "of response");
      }
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

  getDateAsISOFormat() {
    let beginTimeMonitoringString = this.parametersForm.value["beginTimeMonitoring"];
    let beginTimeMonitoring = new Date();
    beginTimeMonitoring.setMinutes(beginTimeMonitoringString.substring(0, 2),
      beginTimeMonitoringString.substring(3, 5), 0);

    this.parametersMonitoringUrl.beginTimeMonitoring = beginTimeMonitoring.toISOString();

    let endTimeMonitoringString = this.parametersForm.value["endTimeMonitoring"];
    let endTimeMonitoring = new Date();
    endTimeMonitoring.setMinutes(endTimeMonitoringString.substring(0, 2),
      endTimeMonitoringString.substring(3, 5), 0);

    this.parametersMonitoringUrl.endTimeMonitoring = endTimeMonitoring.toISOString();
  }

  resetDataOfParametersMonitoringUrl() {
    let date = new Date();
    date.setHours(0, 0);

    let beginTimeMonitoring = date.toTimeString().substring(0,5);
    let endTimeMonitoring = date.toTimeString().substring(0,5);

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
      () => this.styleForMessage = '',3000);
  }

  setErrorsOfResponse(errors: {field: string, message: string }[]) {
    for(let i=0; i< errors.length; i++){
      let someArray = [];

      this.errors.find(
        x => x.field === errors[i].field
      ).message = errors[i].message;
    }
  }

  onReturnToList() {
    this.router.navigate(['/']);
  }

}
