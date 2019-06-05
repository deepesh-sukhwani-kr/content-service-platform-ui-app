import { Component, ElementRef, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { KrogerNotificationsService, KrogerNotification } from 'kroger-notifications';
import { AuthService } from 'kroger-ng-oauth2';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.less']
})
export class AppComponent {
  @ViewChild('mainBody') mainBody: ElementRef;
  constructor(
    private authService: AuthService,
    private notify: KrogerNotificationsService,
    private router: Router,
  ) {
    this.authService.auth.subscribe((data) => {
      if (data.authData.error && data.authData.error.type === 'http_error') {
        this.notify.error(data.authData.error.status.toString(), data.authData.error.message);
      }
    });
  }
}
