import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CspVendorComponent } from './csp-vendor.component';

describe('CspVendorComponent', () => {
  let component: CspVendorComponent;
  let fixture: ComponentFixture<CspVendorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CspVendorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CspVendorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
