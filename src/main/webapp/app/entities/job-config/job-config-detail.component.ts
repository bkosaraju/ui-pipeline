import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IJobConfig } from 'app/shared/model/job-config.model';

@Component({
  selector: 'jhi-job-config-detail',
  templateUrl: './job-config-detail.component.html',
})
export class JobConfigDetailComponent implements OnInit {
  jobConfig: IJobConfig | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ jobConfig }) => (this.jobConfig = jobConfig));
  }

  previousState(): void {
    window.history.back();
  }
}
