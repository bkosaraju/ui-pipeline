import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IJobExecution } from 'app/shared/model/job-execution.model';

@Component({
  selector: 'jhi-job-execution-detail',
  templateUrl: './job-execution-detail.component.html',
})
export class JobExecutionDetailComponent implements OnInit {
  jobExecution: IJobExecution | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ jobExecution }) => (this.jobExecution = jobExecution));
  }

  previousState(): void {
    window.history.back();
  }
}
