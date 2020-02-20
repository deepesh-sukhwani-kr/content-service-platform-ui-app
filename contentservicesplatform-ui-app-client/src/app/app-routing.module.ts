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
    canActivate: [AuthGuard]
  },
  {
    path: 'csvupload',
    component: CsvUploadComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'search',
    component: CspSearchComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'vendor',
    component: CspVendorComponent,
    canActivate: [AuthGuard]
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
