import { TestBed, inject } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';

import { HomeService } from './home.service';

describe('HomeService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports : [
        HttpClientTestingModule
      ],
      providers: [HomeService]
    });
  });

  it('should be created', inject([HomeService], (service: HomeService) => {
    expect(service).toBeTruthy();
  }));
});
