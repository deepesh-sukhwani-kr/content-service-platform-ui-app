import {CspSearchModule} from './csp-search.module';

describe('CspSearchModule', () => {
  let cspSearchModule: CspSearchModule;

  beforeEach(() => {
    cspSearchModule = new CspSearchModule();
  });

  it('should create an instance', () => {
    expect(cspSearchModule).toBeTruthy();
  });
});
