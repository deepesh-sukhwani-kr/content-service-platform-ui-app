import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs/Subscription';
import { HomeService } from '../home.service';

@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.less']
})
export class HomePageComponent implements OnInit, OnDestroy  {

  public message = '';

  meSubscription: Subscription;
  breakSubscription: Subscription;

  constructor(private homeService: HomeService) { }

  ngOnInit() {
  }

  ngOnDestroy(): void {
    if (this.meSubscription != null) {
      this.meSubscription.unsubscribe();
    }
    if (this.breakSubscription != null) {
      this.breakSubscription.unsubscribe();
    }
  }

  onTestClick() {
    this.meSubscription = this.homeService.me().subscribe(
      response => {
        if (response['anonymous']) {
          this.message = 'Hello anonymous person';
        } else {
          this.message = `Hello ${response['username']}`;
        }
      },
      err => {
        // Handle API error response here
        console.log(err);
      });
    return false;
  }

  onTestAlert() {
    this.breakSubscription = this.homeService.break().subscribe(
      response => {
      },
      err => {
        // Handle API error response here
        console.log(err);
      });
      return false;
  }

}
