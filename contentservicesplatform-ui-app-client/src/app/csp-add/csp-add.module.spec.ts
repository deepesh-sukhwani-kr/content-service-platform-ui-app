import {CspAddModule} from './csp-add.module';

describe('CspAddModule', () => {
  let cspAddModule: CspAddModule;

  beforeEach(() => {
    cspAddModule = new CspAddModule();
  });

  it('should create an instance', () => {
    expect(cspAddModule).toBeTruthy();
  });
});
