import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthService } from 'kroger-ng-oauth2';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(private router: Router, private authService: AuthService) { }
  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
    if (this.authService.getUser()) {
      return true;
    }
    // Not logged in so redirect to login page or starting-page with the return url
    this.router.navigate(['/'], { queryParams: { returnUrl: state.url } });
    return false;
  }
}
