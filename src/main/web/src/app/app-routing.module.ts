import { StatusUrlListComponent } from './status-url-list/status-url-list.component';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';
import { ParametersUrlFormComponent } from './parameters-url-form/parameters-url-form.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  { path: '', component: StatusUrlListComponent },
  { path: 'urls/:operation', component: ParametersUrlFormComponent },
  { path: 'urls/:operation/:id', component: ParametersUrlFormComponent },
  { path: 'not-found', component: PageNotFoundComponent},
  { path: '**', redirectTo: '/not-found'}
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes)
  ],
  exports: [
    RouterModule
  ]
})
export class AppRoutingModule {}
