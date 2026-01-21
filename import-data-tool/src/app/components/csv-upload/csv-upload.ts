import { ChangeDetectorRef, Component } from '@angular/core';
import { ImportData, JobImport } from '../../services/import-data';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { finalize } from 'rxjs/operators';

@Component({
  selector: 'app-csv-upload',
  imports: [CommonModule, FormsModule],
  templateUrl: './csv-upload.html',
  styleUrl: './csv-upload.css',
})
export class CsvUpload {
  entityType = '';
  selectedFile?: File;
  job?: JobImport;
  error = '';
  loading = false;

  constructor(private importService: ImportData, private cd: ChangeDetectorRef) { }

  onFileChange(event: any) {
    this.selectedFile = event.target.files[0];
  }

  upload() {
    if (!this.entityType || !this.selectedFile) {
      this.error = 'Entity and CSV file are required';
      return;
    }

    this.loading = true;
    this.error = '';

    this.importService.uploadCsv(this.entityType, this.selectedFile)
      .pipe(finalize(() => { this.loading = false, this.cd.detectChanges(); }))
      .subscribe({
        next: job => {
          this.job = job;
          if (job.errorMessages && job.errorMessages.length > 0) {
            this.error = `Job completed with ${job.errorMessages.length} error(s)`;
            console.warn('Job errors:', job.errorMessages);
          } else {
            this.error = '';
          }

          this.cd.detectChanges();
        },
        error: () => this.error = 'Upload failed'
      });
  }
}
