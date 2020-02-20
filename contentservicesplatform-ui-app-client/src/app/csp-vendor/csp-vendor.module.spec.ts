import {CspVendorModule} from './csp-vendor.module';

describe('CspVendorModule', () => {
  let cspVendorModule: CspVendorModule;

  beforeEach(() => {
    cspVendorModule = new CspVendorModule();
  });

  it('should create an instance', () => {
    expect(cspVendorModule).toBeTruthy();
  });
});
