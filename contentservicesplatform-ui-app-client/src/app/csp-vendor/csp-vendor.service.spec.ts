import { TestBed } from '@angular/core/testing';

import { CspVendorService } from './csp-vendor.service';

describe('CspVendorService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: CspVendorService = TestBed.get(CspVendorService);
    expect(service).toBeTruthy();
  });
});
