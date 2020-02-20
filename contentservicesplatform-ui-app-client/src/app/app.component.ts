import {Component, ElementRef, ViewChild} from '@angular/core';
import {Router} from '@angular/router';
import {KrogerNotificationsService} from 'kroger-notifications';
import {AuthService} from 'kroger-ng-oauth2';
import {MenuItem} from "primeng/api";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.less'],
  styles: [`ul.ui-menu-child {
    white-space: nowrap;
    width: auto !important;
  }`]
})
export class AppComponent {
  items: MenuItem[];
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
      if (data.authData.authenticated) {
        if (this.authService.hasRole("oa-dap-add-user-5420")/* || this.authService.hasRole("oa-cspux-supp-center-5420") || this.authService.hasRole("oa-cspux-taxonomy-5420")*/) {
          this.items.unshift(
            {
              label: 'Add',
              items: [
                {label: 'Add new Images', icon: 'pi pi-fw pi-plus', routerLink: '/add'},
                {label: 'Upload From CSV', icon: 'pi pi-fw pi-folder-open', routerLink: '/csvupload'}
              ]
            }
          )
        }
      }
    });
  }
  ngOnInit() {
    this.items = [{
        label: 'Search',
        items: [
          {label: 'CSP Search', icon: 'pi pi-fw pi-search', routerLink: '/search'},
          {label: 'Vendor Search', icon: 'pi pi-fw pi-cloud-upload', routerLink: '/vendor'}
        ]
      }];
  }
}
