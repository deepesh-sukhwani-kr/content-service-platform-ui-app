import {Component, ElementRef, ViewChild} from '@angular/core';
import {Router} from '@angular/router';
import {KrogerNotificationsService} from 'kroger-notifications';
import {AuthService} from 'kroger-ng-oauth2';
import {MenuItem} from "primeng/api";
import {UtilService} from "./util/util.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.less'],
  styles: [`ul.ui-menu-child {
    white-space: nowrap;
    width: auto !important;
  }`],
  providers: [UtilService]
})
export class AppComponent {
  items: MenuItem[] = [];
  flag: boolean = false;
  @ViewChild('mainBody') mainBody: ElementRef;

  constructor(private authService: AuthService,
              private notify: KrogerNotificationsService,
              private router: Router,
              private utilService: UtilService) {
    this.authService.auth.subscribe((data) => {
        if (data.authData.authenticated) {
          this.utilService.getRbacConfig().then(rbac => {
              if (rbac.checkRbac) {
                rbac.searchRoles.forEach(role => {
                  if (this.authService.hasRole(role))
                    this.flag = true;
                });
                if (this.flag)
                  this.items = [{
                    label: 'Search',
                    items: [
                      {label: 'CSP Search', icon: 'pi pi-fw pi-search', routerLink: '/search'},
                      {label: 'Vendor Search', icon: 'pi pi-fw pi-cloud-upload', routerLink: '/vendor'}
                    ]
                  }];
                else
                  this.items = [{
                    label: 'Search',
                    items: [
                      {label: 'Vendor Search', icon: 'pi pi-fw pi-cloud-upload', routerLink: '/vendor'}
                    ]
                  }];
                rbac.addRoles.forEach(role => {
                  if (this.authService.hasRole(role)) {
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
                });

              }
            }
          );
        }
      }
    )
    ;
  }

  ngOnInit() {
  }
}
