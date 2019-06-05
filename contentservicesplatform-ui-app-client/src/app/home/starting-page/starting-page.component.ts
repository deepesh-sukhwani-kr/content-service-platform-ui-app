import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, RouterStateSnapshot, RouterState } from '@angular/router';
import { AuthService } from 'kroger-ng-oauth2';

@Component({
  selector: 'app-starting-page',
  templateUrl: './starting-page.component.html',
  styleUrls: ['./starting-page.component.less']
})
export class StartingPageComponent implements OnInit {
  private returnUrl: string;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService
  ) { }

  ngOnInit() {
    this.authService.auth.subscribe((data) => {
      if (data.authData.authenticated) {
        // Doesn't need to go to /home, can go to any starting page you want
        this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/home';
        this.router.navigate([this.returnUrl]);
      }
    });
  }

}
