import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IJobTaskOrder } from 'app/shared/model/job-task-order.model';

@Component({
  selector: 'jhi-job-task-order-detail',
  templateUrl: './job-task-order-detail.component.html',
})
export class JobTaskOrderDetailComponent implements OnInit {
  jobTaskOrder: IJobTaskOrder | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ jobTaskOrder }) => (this.jobTaskOrder = jobTaskOrder));
  }

  previousState(): void {
    window.history.back();
  }
}
