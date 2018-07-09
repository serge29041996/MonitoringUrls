import { Component, OnInit } from '@angular/core';
import {ParametersMonitoringUrl} from './parameters-monitoring-url.model';
import {Router, ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-parameters-url-form',
  templateUrl: './parameters-url-form.component.html',
  styleUrls: ['./parameters-url-form.component.css']
})
export class ParametersUrlFormComponent implements OnInit {
  editMode: boolean;
  parametersMonitoringUrl: ParametersMonitoringUrl;

  constructor(private router: Router) { }

  ngOnInit() {
    this.editMode = false;
    this.parametersMonitoringUrl = new ParametersMonitoringUrl('', new Date(), new Date(), 1, 2, 3, 100, 1, 1000, '');
    console.log(this.parametersMonitoringUrl);
  }

  onSubmit() {
    console.log(this.parametersMonitoringUrl);
  }

  onReturnToList() {
    this.router.navigate(['/']);
  }

}
