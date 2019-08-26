import { CsvUploadModule } from './csv-upload.module';

describe('CsvUploadModule', () => {
  let csvUploadModule: CsvUploadModule;

  beforeEach(() => {
    csvUploadModule = new CsvUploadModule();
  });

  it('should create an instance', () => {
    expect(csvUploadModule).toBeTruthy();
  });
});
