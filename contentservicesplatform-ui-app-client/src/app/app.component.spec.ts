import { TestBed, async } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';

import { AppComponent } from './app.component';
import { HeaderComponent } from './core/header/header.component';
import { NavbarComponent } from './core/navbar/navbar.component';
import { FooterComponent } from './core/footer/footer.component';
import { MockLoginIframeComponent, MockUserService, UserService, LoginEventService } from 'kroger-angular-oauth2';
import { KrogerNotificationsComponent, KrogerNotificationsService } from 'kroger-notifications';

describe('AppComponent', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule
      ],
      declarations: [
        AppComponent, HeaderComponent, NavbarComponent, FooterComponent, MockLoginIframeComponent, KrogerNotificationsComponent
      ],
      providers: [
        { provide: UserService, useClass: MockUserService}, LoginEventService, KrogerNotificationsService
      ]
    })
    .overrideComponent(MockLoginIframeComponent, {
      set: {
        selector: 'kr-oauth2-login'
      }
    })
    .compileComponents();
  }));
  it('should create the app', async(() => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.debugElement.componentInstance;
    expect(app).toBeTruthy();
  }));
});
