import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';


import { AppComponent } from './app.component';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';
import { StatusUrlListComponent } from './status-url-list/status-url-list.component';
import { ParametersUrlFormComponent } from './parameters-url-form/parameters-url-form.component';
import {HttpClientModule} from "@angular/common/http";
import {FormsModule} from "@angular/forms";
import {AppRoutingModule} from "./app-routing.module";
import {ParametersUrlService} from "./parameters-url-form/parameters-url.service";


@NgModule({
  declarations: [
    AppComponent,
    PageNotFoundComponent,
    StatusUrlListComponent,
    ParametersUrlFormComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    AppRoutingModule
  ],
  providers: [ParametersUrlService],
  bootstrap: [AppComponent]
})
export class AppModule { }
