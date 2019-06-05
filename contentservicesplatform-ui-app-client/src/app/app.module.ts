import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { RouterModule } from '@angular/router';

import { AppComponent } from './app.component';
import { KrogerNotificationsModule, KrogerNotificationsService } from 'kroger-notifications';
import { CoreModule } from './core/core.module';
import { HomeModule } from './home/home.module';

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
    HomeModule
  ],
  providers: [KrogerNotificationsService],
  bootstrap: [AppComponent]
})
export class AppModule { }
