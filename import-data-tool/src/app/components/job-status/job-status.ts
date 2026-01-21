import { ChangeDetectorRef, Component } from '@angular/core';
import { ImportData, JobImport } from '../../services/import-data';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { finalize } from 'rxjs/operators';

@Component({
  selector: 'app-job-status',
  imports: [CommonModule, FormsModule],
  templateUrl: './job-status.html',
  styleUrl: './job-status.css',
})
export class JobStatus {
  jobId = '';
  job?: JobImport;
  error = '';
  loading = false;

  constructor(private importService: ImportData, private cd: ChangeDetectorRef) {}

  fetchStatus() {
    if (!this.jobId) {
      this.error = 'Job ID is required';
      return;
    }

    this.loading = true;
    this.error = '';

    this.importService.getJobStatus(this.jobId)
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
        error: err => {
          console.error(err);
          this.error = 'Job not found';
        }
      });
  }
}
