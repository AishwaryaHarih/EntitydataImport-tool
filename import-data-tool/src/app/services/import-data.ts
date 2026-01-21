import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ImportError {
  rowNumber: number;
  message: string;
}

export interface JobImport {
  jobId: string;
  entity: string;
  fileName: string;
  status: 'PENDING' | 'PROCESSING' | 'COMPLETED' | 'FAILED';
  totalRecords: number;
  processedRecords: number;
  createdDateTime: string;
  completedDateTime?: string;
  errorMessages: ImportError[];
}

@Injectable({ providedIn: 'root' })
export class ImportData {

  private baseUrl = 'http://localhost:8080/api/dataimport';

  constructor(private http: HttpClient) {}

  uploadCsv(entityType: string, file: File): Observable<JobImport> {
    const formData = new FormData();
    formData.append('entityType', entityType);
    formData.append('file', file);

    return this.http.post<JobImport>(`${this.baseUrl}/csv`, formData);
  }

  getJobStatus(jobId: string): Observable<JobImport> {
    return this.http.get<JobImport>(`${this.baseUrl}/details/${jobId}`);
  }
}

