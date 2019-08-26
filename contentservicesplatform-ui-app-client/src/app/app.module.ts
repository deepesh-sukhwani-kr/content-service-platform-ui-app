import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { RouterModule } from '@angular/router';

import { AppComponent } from './app.component';
import { KrogerNotificationsModule, KrogerNotificationsService } from 'kroger-notifications';
import { CoreModule } from './core/core.module';
import { HomeModule } from './home/home.module';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {InputTextModule} from 'primeng/inputtext';
import {AccordionModule} from 'primeng/accordion';

import {NgbModule} from "@ng-bootstrap/ng-bootstrap";
import {CspSearchModule} from "./csp-search/csp-search.module";
import {MenuModule} from 'primeng/menu';
import {CspAddModule} from "./csp-add/csp-add.module";
import {CsvUploadModule} from "./csv-upload/csv-upload.module";


@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    RouterModule,
    KrogerNotificationsModule.forRoot(),
    CoreModule,
    HomeModule,
    FormsModule,
    ReactiveFormsModule,
    InputTextModule,
    CspSearchModule,
    CspAddModule,
    CsvUploadModule,
    AccordionModule,
    MenuModule,
    NgbModule.forRoot()
  ],
  providers: [KrogerNotificationsService],
  bootstrap: [AppComponent]
})
export class AppModule { }
