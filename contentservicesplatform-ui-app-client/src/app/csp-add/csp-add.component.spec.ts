import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {CspAddComponent} from './csp-add.component';

describe('CspAddComponent', () => {
  let component: CspAddComponent;
  let fixture: ComponentFixture<CspAddComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CspAddComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CspAddComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
