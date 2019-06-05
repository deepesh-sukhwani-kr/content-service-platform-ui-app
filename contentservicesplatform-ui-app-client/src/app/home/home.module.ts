import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomePageComponent } from './home-page/home-page.component';
import { HomeService } from './home.service';
import { ButtonModule } from 'primeng/button';
import { StartingPageComponent } from './starting-page/starting-page.component';


@NgModule({
  imports: [
    CommonModule,
    ButtonModule
  ],
  declarations: [HomePageComponent, StartingPageComponent],
  exports: [HomePageComponent],
  providers: [HomeService]
})
export class HomeModule { }
