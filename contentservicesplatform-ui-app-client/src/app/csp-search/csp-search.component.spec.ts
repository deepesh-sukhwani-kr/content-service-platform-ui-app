import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {CspSearchComponent} from './csp-search.component';

describe('CspSearchComponent', () => {
  let component: CspSearchComponent;
  let fixture: ComponentFixture<CspSearchComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CspSearchComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CspSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
