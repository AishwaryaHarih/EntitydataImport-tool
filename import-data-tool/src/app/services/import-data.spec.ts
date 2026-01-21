import { TestBed } from '@angular/core/testing';

import { ImportData } from './import-data';

describe('ImportData', () => {
  let service: ImportData;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ImportData);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
