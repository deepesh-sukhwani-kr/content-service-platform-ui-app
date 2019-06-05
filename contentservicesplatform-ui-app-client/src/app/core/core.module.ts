import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { HeaderComponent } from './header/header.component';
import { NavbarComponent } from './navbar/navbar.component';
import { FooterComponent } from './footer/footer.component';
import { LogoutComponent } from './logout/logout.component';
import { KrogerNgAuthModule } from 'kroger-ng-oauth2';

@NgModule({
  imports: [
    CommonModule,
    RouterModule,
    KrogerNgAuthModule
  ],
  declarations: [HeaderComponent, NavbarComponent, FooterComponent, LogoutComponent],
  exports: [HeaderComponent, NavbarComponent, FooterComponent, LogoutComponent]
})
export class CoreModule { }
