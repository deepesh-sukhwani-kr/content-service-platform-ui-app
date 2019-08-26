import { TestBed } from '@angular/core/testing';

import { CspSearchService } from './csp-search.service';

describe('CspSearchService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: CspSearchService = TestBed.get(CspSearchService);
    expect(service).toBeTruthy();
  });
});
