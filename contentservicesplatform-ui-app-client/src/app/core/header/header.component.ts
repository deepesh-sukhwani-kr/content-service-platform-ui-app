import { Component, ChangeDetectorRef, OnInit, NgZone } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService, User } from 'kroger-ng-oauth2';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.less']
})
export class HeaderComponent implements OnInit {

  public baseUrl = '';
  public isAuthenticated = false;
  public currentUser: User = {};

  public authConfig = {
    baseUrl: this.baseUrl,
    meEndpoint: this.baseUrl + '/api/me',
    logoutUrl: this.baseUrl + '/oauth/logout',
    gatewayUrl: this.baseUrl
  };

  constructor(
    private ref: ChangeDetectorRef,
    private ngZone: NgZone,
    private router: Router,
    private authService: AuthService
  ) {
  }

  ngOnInit() {
    this.authService.auth.subscribe((data) => {
      if (data.authData.authenticated) {
        this.isAuthenticated = data.authData.authenticated;
        this.currentUser = data.authData.user;
      } else {
        this.isAuthenticated = false;
        this.currentUser = null;
      }
    });
  }

  doLogin() {
    this.authService.login();
  }

  doLogout() {
    this.authService.logout().subscribe(data => {
      setTimeout(() => {
        window.location.href = window.location.origin + '/';
      }, 500);
    });
  }

}
