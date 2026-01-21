import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { CsvUpload } from './components/csv-upload/csv-upload';
import { JobStatus } from './components/job-status/job-status';

@Component({
  selector: 'app-root',
  imports: [CsvUpload, JobStatus],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('import-data-tool');
}
