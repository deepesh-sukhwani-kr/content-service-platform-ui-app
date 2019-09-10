import {TestBed} from '@angular/core/testing';

import {CspAddService} from './csp-add.service';

describe('CspAddService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: CspAddService = TestBed.get(CspAddService);
    expect(service).toBeTruthy();
  });
});
