import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HomePageComponent } from './home-page.component';
import { HomeService } from '../home.service';

describe('HomePageComponent', () => {
  let component: HomePageComponent;
  let fixture: ComponentFixture<HomePageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports : [
        HttpClientTestingModule
      ],
      declarations: [ HomePageComponent ],
      providers: [ HomeService ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HomePageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display Hello onTestClick, user is anonymous', () => {
    component.onTestClick();

    const httpMock = TestBed.get(HttpTestingController);
    const request = httpMock.expectOne('/api/me');
    request.flush({'anonymous' : true});

    httpMock.verify();
    expect(component.message).toBe('Hello anonymous person');
  });
});
