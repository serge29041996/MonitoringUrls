import { Component, OnInit } from '@angular/core';
import { StateUrl } from '../state-url.model';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-status-url-list',
  templateUrl: './status-url-list.component.html',
  styleUrls: ['./status-url-list.component.css']
})
export class StatusUrlListComponent implements OnInit {
  stateUrls: StateUrl[];

  constructor(private route: ActivatedRoute, private router: Router) { }

  ngOnInit() {
    this.stateUrls = [
      new StateUrl('web.telegram.com', 'OK', '', true)
    ];
    this.stateUrls[0].id = 0;
  }

  onDisableMonitoringUrl(id: number) {
    this.stateUrls[id].isMonitoring = !this.stateUrls[id].isMonitoring;
    console.log(this.stateUrls[id].isMonitoring);
  }

  onDeleteUrlMonitoring(urlStateData: StateUrl) {
    const result = confirm('Do you want to delete url with name ' + urlStateData.url + '?');
    if (result) {
      this.stateUrls.splice(this.stateUrls.indexOf(urlStateData), 1);
    }
  }

  onAddUrlMonitoring() {
    this.router.navigate(['urls/create'], {relativeTo: this.route});
  }

}
