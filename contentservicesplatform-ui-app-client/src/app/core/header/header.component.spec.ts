import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';

import { HeaderComponent } from './header.component';
import { NavbarComponent } from '../navbar/navbar.component';
import { MockLoginIframeComponent, MockUserService, UserService } from 'kroger-angular-oauth2';

describe('HeaderComponent', () => {
  let component: HeaderComponent;
  let fixture: ComponentFixture<HeaderComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule
      ],
      declarations: [ HeaderComponent, NavbarComponent, MockLoginIframeComponent ],
      providers: [
        { provide: UserService, useClass: MockUserService }
      ]
    })
    .overrideComponent(MockLoginIframeComponent, {
      set: {
        selector: 'kr-oauth2-login'
      }
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
