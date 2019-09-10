import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';

import {StartingPageComponent} from './home/starting-page/starting-page.component';
import {HomePageComponent} from './home/home-page/home-page.component';
import {LogoutComponent} from './core/logout/logout.component';
import {AuthGuard} from './auth.guard';
import {CspAddComponent} from "./csp-add/csp-add.component";
import {CsvUploadComponent} from "./csv-upload/csv-upload.component";
import {CspSearchComponent} from "./csp-search/csp-search.component";
import {CspVendorComponent} from "./csp-vendor/csp-vendor.component";

const routes: Routes = [
  {
    path: '',
    component: StartingPageComponent
  },
  {
    path: 'home',
    component: HomePageComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'logout',
    component: LogoutComponent
  },
  {
    path: 'add',
    component: CspAddComponent,
  },
  {
    path: 'csvupload',
    component: CsvUploadComponent,
  },
  {
    path: 'search',
    component: CspSearchComponent
  },
  {
    path: 'vendor',
    component: CspVendorComponent
  },
  {
    path: '**',
    redirectTo: ''
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
